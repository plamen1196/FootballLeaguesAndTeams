package com.example.exam2410.web;

import com.example.exam2410.model.binding.AddLeagueBindingModel;
import com.example.exam2410.model.binding.AddTeamBindingModel;
import com.example.exam2410.model.service.LeagueServiceModel;
import com.example.exam2410.model.service.TeamServiceModel;
import com.example.exam2410.service.FootballLeagueUser;
import com.example.exam2410.service.LeagueService;
import com.example.exam2410.service.PlayerService;
import com.example.exam2410.service.TeamService;
import com.example.exam2410.service.impl.FootballLeagueUserImpl;
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

    public TeamController(TeamService teamService, LeagueService leagueService, PlayerService playerService) {
        this.teamService = teamService;
        this.leagueService = leagueService;
        this.playerService = playerService;
    }

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

        if (!leagueService.checkCapacity(addTeamBindingModel.getLeague())){
            redirectAttributes.addFlashAttribute("notEnoughCapacity", true);
            return "redirect:add";
        }

        TeamServiceModel teamServiceModel = teamService.addTeam(addTeamBindingModel, user.getUserIdentifier());

        return "redirect:details/" + teamServiceModel.getId();
    }

    @GetMapping("/edit/{id}")
    public String editTeam(@PathVariable Long id, Model model) {

        if(!model.containsAttribute("bad_credentials")) {
            model.addAttribute("bad_credentials", false);
        }
        if(!model.containsAttribute("team")) {
            model.addAttribute("team", teamService.findTeamById(id));
        }

        model.addAttribute("leagues", leagueService.findAllLeagues());

        return "edit-team";
    }

    @GetMapping("/edit/{id}/errors")
    public String editTeamErrors(@PathVariable Long id, Model model) {

        model.addAttribute("team", teamService.findTeamById(id));
        model.addAttribute("leagues", leagueService.findAllLeagues());

        return "edit-team";
    }

    @PatchMapping("/edit/{id}")
    public String editTeam(@PathVariable Long id, @Valid AddTeamBindingModel addTeamBindingModel,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes,
                             @AuthenticationPrincipal FootballLeagueUser user) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("addTeamBindingModel", addTeamBindingModel)
                    .addFlashAttribute("org.springframework.validation.BindingResult.addTeamBindingModel", bindingResult);
            return "redirect:" + id + "/errors";
        }

        TeamServiceModel team = teamService.findTeamByName(addTeamBindingModel.getName());

        if (team != null && !Objects.equals(team.getId(), id)){
            redirectAttributes.addFlashAttribute("bad_credentials", true);
            return "redirect:" + id;
        }

        TeamServiceModel teamServiceModel = teamService.editTeam(addTeamBindingModel, id);

        return "redirect:/team/details/" + teamServiceModel.getId();
    }

    @DeleteMapping("/delete/{id}")
    public String deleteTeam(@PathVariable Long id) {
        teamService.deleteTeam(id);

        return "redirect:/team/myteams";
    }

    @GetMapping("/details/{id}")
    public String detailsTeam(@PathVariable Long id, Model model) {

        model.addAttribute("team", teamService.findTeamById(id));
        model.addAttribute("players", playerService.findPlayersByTeamId(id));

        return "team-details";
    }

    @GetMapping("/ranking")
    public String rankingTeams(Model model) {

        model.addAttribute("teams", teamService.findAllTeamsByPoints());

        return "ranking";
    }

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
