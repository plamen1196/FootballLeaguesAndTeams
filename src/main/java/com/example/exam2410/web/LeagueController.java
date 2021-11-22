package com.example.exam2410.web;

import com.example.exam2410.model.binding.AddLeagueBindingModel;
import com.example.exam2410.model.service.LeagueServiceModel;
import com.example.exam2410.service.FootballLeagueUser;
import com.example.exam2410.service.LeagueService;
import com.example.exam2410.service.TeamService;
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
@RequestMapping("/league")
public class LeagueController {

    private final LeagueService leagueService;
    private final TeamService teamService;

    public LeagueController(LeagueService leagueService, TeamService teamService) {
        this.leagueService = leagueService;
        this.teamService = teamService;
    }

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
                            @AuthenticationPrincipal FootballLeagueUser user) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("addLeagueBindingModel", addLeagueBindingModel)
                    .addFlashAttribute("org.springframework.validation.BindingResult.addLeagueBindingModel", bindingResult);
            return "redirect:add";
        }

        LeagueServiceModel leagueByLevel = leagueService.findLeagueByLevel(addLeagueBindingModel.getLevel());

        if (leagueByLevel != null){
            redirectAttributes.addFlashAttribute("bad_credentials", true);
            return "redirect:add";
        }

        LeagueServiceModel leagueServiceModel = leagueService.addLeague(addLeagueBindingModel, user.getUserIdentifier());
        return "redirect:details/" + leagueServiceModel.getId();
    }

    @GetMapping("/edit/{id}")
    public String editLeague(@PathVariable Long id, Model model) {

        if(!model.containsAttribute("bad_credentials")) {
            model.addAttribute("bad_credentials", false);
        }
        if(!model.containsAttribute("league")) {
            model.addAttribute("league", leagueService.findLeagueViewById(id));
        }

        return "edit-league";
    }

    @GetMapping("/edit/{id}/errors")
    public String editLeagueErrors(@PathVariable Long id, Model model) {

        model.addAttribute("league", leagueService.findLeagueViewById(id));

        return "edit-league";
    }

    @PatchMapping("/edit/{id}")
    public String editLeague(@PathVariable Long id, @Valid AddLeagueBindingModel addLeagueBindingModel,
                            BindingResult bindingResult,
                            RedirectAttributes redirectAttributes,
                            @AuthenticationPrincipal FootballLeagueUser user) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("addLeagueBindingModel", addLeagueBindingModel)
                    .addFlashAttribute("org.springframework.validation.BindingResult.addLeagueBindingModel", bindingResult);
            return "redirect:" + id + "/errors";
        }

        LeagueServiceModel leagueByLevel = leagueService.findLeagueByLevel(addLeagueBindingModel.getLevel());

        if (leagueByLevel != null && !Objects.equals(leagueByLevel.getId(), id)){
            redirectAttributes.addFlashAttribute("bad_credentials", true);
            return "redirect:" + id;
        }

        LeagueServiceModel leagueServiceModel = leagueService.editLeague(addLeagueBindingModel, id);
        return "redirect:/league/details/" + leagueServiceModel.getId();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public String deleteLeague(@PathVariable Long id) {
        leagueService.deleteLeague(id);

        return "redirect:/league/allleagues";
    }

    @GetMapping("/details/{id}")
    public String leagueDetails(@PathVariable Long id, Model model) {

        model.addAttribute("league", leagueService.findLeagueViewById(id));
        model.addAttribute("teamsByPoints", teamService.findTeamsByPoints(id));

        return "league-details";
    }

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
