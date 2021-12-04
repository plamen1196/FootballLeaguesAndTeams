package com.example.FootballLeagues.web;

import com.example.FootballLeagues.model.binding.SelectLeagueBindingModel;
import com.example.FootballLeagues.model.binding.SelectTeamsBindingModel;
import com.example.FootballLeagues.service.LeagueService;
import com.example.FootballLeagues.service.ResultService;
import com.example.FootballLeagues.service.TeamService;
import com.example.FootballLeagues.service.impl.FootballLeagueUserImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    private final ResultService resultService;

    public MatchesController(LeagueService leagueService, TeamService teamService, ResultService resultService) {
        this.leagueService = leagueService;
        this.teamService = teamService;
        this.resultService = resultService;
    }

    //SELECT IN WHICH LEAGUE THE MATCH WILL BE PLAYED
    @GetMapping()
    public String selectLeague(Model model) {

        if (!model.containsAttribute("selectLeagueBindingModel")) {
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

    //SELECT THE HOME TEAM, THE AWAY TEAM AND THE RESULT
    @GetMapping("/{id}")
    public String selectTeams(@PathVariable Long id, Model model, @AuthenticationPrincipal FootballLeagueUserImpl user) {

        if (!model.containsAttribute("selectTeamsBindingModel")) {
            model.addAttribute("bad_credentials", false);
            model.addAttribute("selectTeamsBindingModel", selectTeamsBindingModel());
        }

        leagueService.findLeagueViewById(id);

        model.addAttribute("teams", teamService.findTeamsByPointsWithLeagueId(id, user.getUserIdentifier()));
        model.addAttribute("leagueId", id);

        return "play-match";
    }

    //ERRORS
    @GetMapping("/play/{id}/errors")
    public String playMatchErrors(@PathVariable Long id, Model model, @AuthenticationPrincipal FootballLeagueUserImpl user) {

        model.addAttribute("teams", teamService.findTeamsByPointsWithLeagueId(id, user.getUserIdentifier()));
        model.addAttribute("leagueId", id);

        return "play-match";
    }

    //PLAY MATCH AND PATCH THE WINS, LOSES, DRAWS, POINTS, MATCHES
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

        if (selectTeamsBindingModel.getHomeTeamName().equals(selectTeamsBindingModel.getAwayTeamName())) {
            redirectAttributes.addFlashAttribute("bad_credentials", true);
            return "redirect:" + id + "/errors";
        }

        teamService.playMatch(selectTeamsBindingModel);

        redirectAttributes.addFlashAttribute("matchResult", selectTeamsBindingModel);
        redirectAttributes.addFlashAttribute("showMatchResult", true);

        return "redirect:/matches";
    }

    //ALL RESULTS WITH DATES AND LEAGUES
    @GetMapping("/results")
    public String showMatchResults(Model model){

        model.addAttribute("results", resultService.findAllResultsByLeague());

        return "match-result";
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
