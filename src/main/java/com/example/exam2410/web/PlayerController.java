package com.example.exam2410.web;

import com.example.exam2410.model.binding.AddPlayerBindingModel;
import com.example.exam2410.model.binding.AddTeamBindingModel;
import com.example.exam2410.model.service.PlayerServiceModel;
import com.example.exam2410.model.service.TeamServiceModel;
import com.example.exam2410.model.view.PlayerDetailsView;
import com.example.exam2410.service.*;
import com.example.exam2410.service.impl.FootballLeagueUserImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Objects;

@Controller
@RequestMapping("/player")
public class PlayerController {

    private final PlayerService playerService;
    private final StatService statService;
    private final TeamService teamService;
    private final UserService userService;

    public PlayerController(PlayerService playerService, StatService statService, TeamService teamService, UserService userService) {
        this.playerService = playerService;
        this.statService = statService;
        this.teamService = teamService;
        this.userService = userService;
    }

    @GetMapping("/add")
    public String addPlayer(Model model, @AuthenticationPrincipal FootballLeagueUserImpl user) {

        if (!model.containsAttribute("addPlayerBindingModel")) {
            model.addAttribute("addPlayerBindingModel", addPlayerBindingModel());
        }

        model.addAttribute("teamsByUser", teamService.findTeamsByUserName(user.getUserIdentifier()));

        return "add-player";
    }

    @PostMapping("/add")
    public String addPlayer(@Valid AddPlayerBindingModel addPlayerBindingModel,
                            BindingResult bindingResult,
                            RedirectAttributes redirectAttributes, @AuthenticationPrincipal FootballLeagueUserImpl user) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("addPlayerBindingModel", addPlayerBindingModel)
                    .addFlashAttribute("org.springframework.validation.BindingResult.addPlayerBindingModel", bindingResult);
            return "redirect:add";
        }

        PlayerServiceModel player = playerService.addPlayer(addPlayerBindingModel, user);

        return "redirect:details/" + player.getId();
    }

    @GetMapping("/edit/{id}")
    public String editPlayer(@PathVariable Long id, Model model, @AuthenticationPrincipal FootballLeagueUserImpl user) {


        if (!model.containsAttribute("player")) {
            model.addAttribute("player", playerService.findPlayerById(id));
        }

        if (!model.containsAttribute("stat")) {
            model.addAttribute("stat", statService.findStatByPlayerId(id));
        }

        if (!model.containsAttribute("teams")) {
            model.addAttribute("teams", teamService.findTeamsByUserName(user.getUserIdentifier()));
        }

        return "edit-player";
    }

    @GetMapping("/edit/{id}/errors")
    public String editPlayerErrors(@PathVariable Long id, Model model, @AuthenticationPrincipal FootballLeagueUserImpl user) {

        model.addAttribute("player", playerService.findPlayerById(id));
        model.addAttribute("teams", teamService.findTeamsByUserName(user.getUserIdentifier()));
        model.addAttribute("stat", statService.findStatByPlayerId(id));

        return "edit-player";
    }

    @PatchMapping("/edit/{id}")
    public String editPlayer(@PathVariable Long id, @Valid AddPlayerBindingModel addPlayerBindingModel,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes,
                             @AuthenticationPrincipal FootballLeagueUser user) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("addPlayerBindingModel", addPlayerBindingModel)
                    .addFlashAttribute("org.springframework.validation.BindingResult.addPlayerBindingModel", bindingResult);
            return "redirect:" + id + "/errors";
        }

        PlayerServiceModel playerServiceModel = playerService.editPlayer(addPlayerBindingModel, id);

        return "redirect:/player/details/" + playerServiceModel.getId();
    }

    @DeleteMapping("/delete/{id}")
    public String deletePlayer(@PathVariable Long id, HttpServletRequest request
            , @AuthenticationPrincipal FootballLeagueUserImpl user, RedirectAttributes redirectAttributes) {

        if (!request.isUserInRole("ROLE_ADMIN") && !playerService.findPlayerById(id).getUsername().equals(user.getUsername())) {
            redirectAttributes.addFlashAttribute("wrongAuthorities", true);
            return "redirect:/player/details/" + id;
        }

        playerService.deletePlayer(id);

        return "redirect:/team/myteams";
    }

    @GetMapping("/details/{id}")
    public String detailsPlayer(@PathVariable Long id, Model model) {

        if (!model.containsAttribute("wrongAuthorities")) {
            model.addAttribute("wrongAuthorities", false);
        }

        model.addAttribute("player", playerService.findPlayerById(id));
        model.addAttribute("stats", statService.findStatByPlayerId(id));

        return "player-details";
    }

    @ModelAttribute
    AddPlayerBindingModel addPlayerBindingModel() {
        return new AddPlayerBindingModel();
    }
}
