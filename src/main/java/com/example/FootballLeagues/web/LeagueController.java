package com.example.FootballLeagues.web;

import com.example.FootballLeagues.model.binding.AddLeagueBindingModel;
import com.example.FootballLeagues.model.service.LeagueServiceModel;
import com.example.FootballLeagues.model.view.TeamDetailsView;
import com.example.FootballLeagues.service.LeagueService;
import com.example.FootballLeagues.service.TeamService;
import com.example.FootballLeagues.service.impl.FootballLeagueUserImpl;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@EnableGlobalMethodSecurity(prePostEnabled = true)
@Controller
@RequestMapping("/league")
public class LeagueController {

    private final LeagueService leagueService;
    private final TeamService teamService;

    public LeagueController(LeagueService leagueService, TeamService teamService) {
        this.leagueService = leagueService;
        this.teamService = teamService;
    }

    //ADD
    @GetMapping("/add")
    public String addLeague(Model model) {

        if (!model.containsAttribute("addLeagueBindingModel")) {
            model.addAttribute("addLeagueBindingModel", addLeagueBindingModel());
            model.addAttribute("bad_credentials", false);
        }

        return "add-league";
    }

    @PostMapping("/add")
    public String addLeague(@Valid AddLeagueBindingModel addLeagueBindingModel,
                            BindingResult bindingResult,
                            RedirectAttributes redirectAttributes,
                            @AuthenticationPrincipal FootballLeagueUserImpl user) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("addLeagueBindingModel", addLeagueBindingModel)
                    .addFlashAttribute("org.springframework.validation.BindingResult.addLeagueBindingModel", bindingResult);
            return "redirect:add";
        }

        LeagueServiceModel leagueByLevel = leagueService.findLeagueByLevel(addLeagueBindingModel.getLevel());

        if (leagueByLevel != null) {
            redirectAttributes.addFlashAttribute("bad_credentials", true);
            return "redirect:add";
        }

        LeagueServiceModel leagueServiceModel =
                leagueService.addLeague(addLeagueBindingModel, user.getUserIdentifier());
        return "redirect:details/" + leagueServiceModel.getId();
    }

    //EDIT
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/edit/{id}")
    public String editLeague(@PathVariable Long id, Model model) {

        if (!model.containsAttribute("bad_credentials")) {
            model.addAttribute("bad_credentials", false);
        }
        if (!model.containsAttribute("tooSmallCapacity")) {
            model.addAttribute("tooSmallCapacity", false);
        }
        if (!model.containsAttribute("league")) {
            model.addAttribute("league", leagueService.findLeagueViewById(id));
        }

        return "edit-league";
    }

    //ERRORS
    @GetMapping("/edit/{id}/errors")
    public String editLeagueErrors(@PathVariable Long id, Model model) {

        model.addAttribute("league", leagueService.findLeagueViewById(id));

        return "edit-league";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/edit/{id}")
    public String editLeague(@PathVariable Long id, @Valid AddLeagueBindingModel addLeagueBindingModel,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes,
                             @AuthenticationPrincipal FootballLeagueUserImpl user) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("addLeagueBindingModel", addLeagueBindingModel)
                    .addFlashAttribute("org.springframework.validation.BindingResult.addLeagueBindingModel", bindingResult);
            return "redirect:" + id + "/errors";
        }

        LeagueServiceModel leagueByLevel = leagueService.findLeagueByLevel(addLeagueBindingModel.getLevel());

        if (leagueByLevel != null && !Objects.equals(leagueByLevel.getId(), id)) {
            redirectAttributes.addFlashAttribute("bad_credentials", true);
            return "redirect:" + id;
        }

        List<TeamDetailsView> teams = teamService.findTeamsByPointsWithLeagueId(id, user.getUserIdentifier());

        if (addLeagueBindingModel.getCapacity() < teams.size()) {
            redirectAttributes.addFlashAttribute("tooSmallCapacity", true);
            return "redirect:" + id;
        }

        LeagueServiceModel leagueServiceModel = leagueService.editLeague(addLeagueBindingModel, id);
        return "redirect:/league/details/" + leagueServiceModel.getId();
    }

    //DELETE
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public String deleteLeague(@PathVariable Long id) {
        leagueService.deleteLeague(id);

        return "redirect:/league/allleagues";
    }

    //DETAILS
    @GetMapping("/details/{id}")
    public String leagueDetails(@PathVariable Long id, Model model, @AuthenticationPrincipal FootballLeagueUserImpl user) {

        model.addAttribute("league", leagueService.findLeagueViewById(id));
        model.addAttribute("teamsByPoints", teamService.findTeamsByPointsWithLeagueId(id, user.getUserIdentifier()));

        return "details-league";
    }

    //ALL LEAGUES
    @GetMapping("/allleagues")
    public String allLeagues(Model model) {

        model.addAttribute("leagues", leagueService.findAllLeagues());

        return "all-leagues";
    }

    @ModelAttribute
    AddLeagueBindingModel addLeagueBindingModel() {
        return new AddLeagueBindingModel();
    }
}
