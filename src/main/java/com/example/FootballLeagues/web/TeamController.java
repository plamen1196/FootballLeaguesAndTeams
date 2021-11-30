package com.example.FootballLeagues.web;

import com.example.FootballLeagues.model.binding.AddTeamBindingModel;
import com.example.FootballLeagues.model.service.TeamServiceModel;
import com.example.FootballLeagues.service.LeagueService;
import com.example.FootballLeagues.service.PlayerService;
import com.example.FootballLeagues.service.TeamService;
import com.example.FootballLeagues.service.impl.FootballLeagueUserImpl;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Objects;

@Controller
@RequestMapping("/team")
public class TeamController {

    private final TeamService teamService;
    private final LeagueService leagueService;
    private final PlayerService playerService;

    public TeamController(TeamService teamService,
                          LeagueService leagueService, PlayerService playerService) {
        this.teamService = teamService;
        this.leagueService = leagueService;
        this.playerService = playerService;
    }

    //ADD TEAM
    @GetMapping("/add")
    public String addTeam(Model model) {

        if (!model.containsAttribute("addTeamBindingModel")) {
            model.addAttribute("addTeamBindingModel", addTeamBindingModel());
            model.addAttribute("bad_credentials", false);
            model.addAttribute("notEnoughCapacity", true);
        }

        model.addAttribute("leagues", leagueService.findAllLeagues());

        return "add-team";
    }

    @PostMapping("/add")
    public String addTeam(@Valid AddTeamBindingModel addTeamBindingModel,
                          BindingResult bindingResult,
                          RedirectAttributes redirectAttributes,
                          @AuthenticationPrincipal FootballLeagueUserImpl user) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("addTeamBindingModel", addTeamBindingModel)
                    .addFlashAttribute("org.springframework.validation.BindingResult.addTeamBindingModel", bindingResult);
            return "redirect:add";
        }

        TeamServiceModel team = teamService.findTeamByName(addTeamBindingModel.getName());

        if (team != null) {
            redirectAttributes.addFlashAttribute("bad_credentials", true);
            return "redirect:add";
        }

        if (!leagueService.checkCapacity(addTeamBindingModel.getLeague(), user.getUserIdentifier())) {
            redirectAttributes.addFlashAttribute("notEnoughCapacity", true);
            return "redirect:add";
        }

        TeamServiceModel teamServiceModel = teamService.addTeam(addTeamBindingModel, user.getUserIdentifier());

        return "redirect:details/" + teamServiceModel.getId();
    }

    //EDIT TEAM
    @PreAuthorize("@teamServiceImpl.isOwner(#user.userIdentifier, #id)")
    @GetMapping("/edit/{id}")
    public String editTeam(@PathVariable Long id, Model model, @AuthenticationPrincipal FootballLeagueUserImpl user) {

        if (!model.containsAttribute("bad_credentials")) {
            model.addAttribute("bad_credentials", false);
        }
        if (!model.containsAttribute("team")) {
            model.addAttribute("team", teamService.findTeamById(id, user.getUserIdentifier()));
        }
        if (!model.containsAttribute("notEnoughCapacity")) {
            model.addAttribute("notEnoughCapacity", false);
        }

        model.addAttribute("leagues", leagueService.findAllLeagues());

        return "edit-team";
    }

    //ERRORS
    @GetMapping("/edit/{id}/errors")
    public String editTeamErrors(@PathVariable Long id, Model model, @AuthenticationPrincipal FootballLeagueUserImpl user) {

        model.addAttribute("team", teamService.findTeamById(id, user.getUserIdentifier()));
        model.addAttribute("leagues", leagueService.findAllLeagues());

        return "edit-team";
    }

    @PreAuthorize("@teamServiceImpl.isOwner(#user.userIdentifier, #id)")
    @PatchMapping("/edit/{id}")
    public String editTeam(@PathVariable Long id, @Valid AddTeamBindingModel addTeamBindingModel,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes,
                           @AuthenticationPrincipal FootballLeagueUserImpl user) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("addTeamBindingModel", addTeamBindingModel)
                    .addFlashAttribute("org.springframework.validation.BindingResult.addTeamBindingModel", bindingResult);
            return "redirect:" + id + "/errors";
        }

        TeamServiceModel team = teamService.findTeamByName(addTeamBindingModel.getName());

        if (team != null) {
            redirectAttributes.addFlashAttribute("bad_credentials", true);
            return "redirect:" + id;
        }

        TeamServiceModel teamServiceModel;

        if (leagueService.checkCapacity(addTeamBindingModel.getLeague(), user.getUserIdentifier())) {
            teamServiceModel = teamService.editTeam(addTeamBindingModel, id);
        } else {
            redirectAttributes.addFlashAttribute("notEnoughCapacity", true);
            return "redirect:" + id;
        }

        return "redirect:/team/details/" + teamServiceModel.getId();
    }

    //DELETE TEAM
    @PreAuthorize("@teamServiceImpl.isOwner(#user.userIdentifier, #id)")
    @DeleteMapping("/delete/{id}")
    public String deleteTeam(@PathVariable Long id, @AuthenticationPrincipal FootballLeagueUserImpl user) {
        teamService.deleteTeam(id);

        return "redirect:/team/myteams";
    }

    //DETAILS TEAM
    @GetMapping("/details/{id}")
    public String detailsTeam(@PathVariable Long id, Model model, @AuthenticationPrincipal FootballLeagueUserImpl user) {

        model.addAttribute("team", teamService.findTeamById(id, user.getUserIdentifier()));
        model.addAttribute("players", playerService.findPlayersByTeamId(id, user.getUserIdentifier()));

        return "details-team";
    }

    //RANKING OF ALL TEAMS
    @GetMapping("/ranking")
    public String rankingTeams(Model model, @AuthenticationPrincipal FootballLeagueUserImpl user) {

        model.addAttribute("teams", teamService.findAllTeamsByPoints(user.getUserIdentifier()));

        return "ranking";
    }

    //CURRENT USER TEAMS
    @GetMapping("/myteams")
    public String myTeams(Model model, @AuthenticationPrincipal FootballLeagueUserImpl user) {

        model.addAttribute("myTeams", teamService.findTeamsByUserName(user.getUserIdentifier()));

        return "my-teams";
    }

    @ModelAttribute
    AddTeamBindingModel addTeamBindingModel() {
        return new AddTeamBindingModel();
    }
}
