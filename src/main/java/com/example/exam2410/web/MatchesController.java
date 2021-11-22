package com.example.exam2410.web;

import com.example.exam2410.model.binding.SelectLeagueBindingModel;
import com.example.exam2410.model.binding.SelectTeamsBindingModel;

import com.example.exam2410.service.LeagueService;
import com.example.exam2410.service.TeamService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;


@Controller
@RequestMapping("/matches")
public class MatchesController {

    private final LeagueService leagueService;
    private final TeamService teamService;

    public MatchesController(LeagueService leagueService, TeamService teamService) {
        this.leagueService = leagueService;
        this.teamService = teamService;
    }
    @GetMapping()
    public String selectLeague(Model model) {

        if (!model.containsAttribute("selectLeagueBindingModel")){
            model.addAttribute("selectLeagueBindingModel", selectLeagueBindingModel());
            model.addAttribute("matchResult", selectTeamsBindingModel());
            model.addAttribute("showMatchResult", false);
        }

        model.addAttribute("leagues", leagueService.findAllLeagues());

        return "select-league";
    }

    @PostMapping()
    public String selectLeague(@Valid SelectLeagueBindingModel selectLeagueBindingModel,
                            BindingResult bindingResult,
                            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("selectLeagueBindingModel", selectLeagueBindingModel)
                    .addFlashAttribute("org.springframework.validation.BindingResult.selectLeagueBindingModel", bindingResult);
            return "redirect:matches";
        }

        return "redirect:/matches/" + selectLeagueBindingModel.getId();
    }

    @GetMapping("/{id}")
    public String selectTeams(@PathVariable Long id, Model model) {

        if (!model.containsAttribute("selectTeamsBindingModel")){
            model.addAttribute("bad_credentials", false);
            model.addAttribute("selectTeamsBindingModel", selectTeamsBindingModel());
        }

        model.addAttribute("teams", teamService.findTeamsByPoints(id));
        model.addAttribute("leagueId", id);

        return "play-match";
    }

    @GetMapping("/play/{id}/errors")
    public String playMatchErrors(@PathVariable Long id, Model model) {

        model.addAttribute("teams", teamService.findTeamsByPoints(id));
        model.addAttribute("leagueId", id);

        return "play-match";
    }

    @PatchMapping("/play/{id}")
    public String playMatch(@Valid SelectTeamsBindingModel selectTeamsBindingModel,
                            BindingResult bindingResult,
                            @PathVariable Long id,
                            RedirectAttributes redirectAttributes) {


        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("selectTeamsBindingModel", selectTeamsBindingModel)
                    .addFlashAttribute("org.springframework.validation.BindingResult.selectTeamsBindingModel", bindingResult);
            return "redirect:" + id + "/errors";
        }

        if (selectTeamsBindingModel.getHomeTeamName().equals(selectTeamsBindingModel.getAwayTeamName())){
            redirectAttributes.addFlashAttribute("bad_credentials", true);
            return "redirect:" + id + "/errors";
        }

        teamService.playMatch(selectTeamsBindingModel);

        redirectAttributes.addFlashAttribute("matchResult", selectTeamsBindingModel);
        redirectAttributes.addFlashAttribute("showMatchResult", true);

        return "redirect:/matches";
    }

    @ModelAttribute
    SelectLeagueBindingModel selectLeagueBindingModel() {
        return new SelectLeagueBindingModel();
    }

    @ModelAttribute
    SelectTeamsBindingModel selectTeamsBindingModel() {
        return new SelectTeamsBindingModel();
    }
}
