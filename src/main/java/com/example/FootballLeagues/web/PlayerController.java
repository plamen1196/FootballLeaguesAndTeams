package com.example.FootballLeagues.web;

import com.example.FootballLeagues.model.binding.AddPlayerBindingModel;
import com.example.FootballLeagues.model.service.PlayerServiceModel;
import com.example.FootballLeagues.service.*;
import com.example.FootballLeagues.service.impl.FootballLeagueUserImpl;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@RequestMapping("/player")
public class PlayerController {

    private final PlayerService playerService;
    private final StatService statService;
    private final TeamService teamService;

    public PlayerController(PlayerService playerService,
                            StatService statService,
                            TeamService teamService) {
        this.playerService = playerService;
        this.statService = statService;
        this.teamService = teamService;
    }

    //ADD PLAYER
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

        PlayerServiceModel player = playerService.addPlayer(addPlayerBindingModel, user.getUserIdentifier());

        return "redirect:details/" + player.getId();
    }

    //EDIT PLAYER
    @PreAuthorize("@playerServiceImpl.isOwner(#user.userIdentifier, #id)")
    @GetMapping("/edit/{id}")
    public String editPlayer(@PathVariable Long id, Model model, @AuthenticationPrincipal FootballLeagueUserImpl user) {


        if (!model.containsAttribute("player")) {
            model.addAttribute("player", playerService.findPlayerById(id, user.getUserIdentifier()));
        }

        if (!model.containsAttribute("stat")) {
            model.addAttribute("stat", statService.findStatByPlayerId(id));
        }

        if (!model.containsAttribute("teams")) {
            model.addAttribute("teams", teamService.findTeamsByUserName(user.getUserIdentifier()));
        }

        return "edit-player";
    }

    //ERRORS
    @GetMapping("/edit/{id}/errors")
    public String editPlayerErrors(@PathVariable Long id, Model model, @AuthenticationPrincipal FootballLeagueUserImpl user) {

        model.addAttribute("player", playerService.findPlayerById(id, user.getUserIdentifier()));
        model.addAttribute("teams", teamService.findTeamsByUserName(user.getUserIdentifier()));
        model.addAttribute("stat", statService.findStatByPlayerId(id));

        return "edit-player";
    }

    @PreAuthorize("@playerServiceImpl.isOwner(#user.userIdentifier, #id)")
    @PatchMapping("/edit/{id}")
    public String editPlayer(@PathVariable Long id, @Valid AddPlayerBindingModel addPlayerBindingModel,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes,
                             @AuthenticationPrincipal FootballLeagueUserImpl user) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("addPlayerBindingModel", addPlayerBindingModel)
                    .addFlashAttribute("org.springframework.validation.BindingResult.addPlayerBindingModel", bindingResult);
            return "redirect:" + id + "/errors";
        }

        PlayerServiceModel playerServiceModel = playerService.editPlayer(addPlayerBindingModel, id);

        return "redirect:/player/details/" + playerServiceModel.getId();
    }

    //DELETE PLAYER
    @PreAuthorize("@playerServiceImpl.isOwner(#user.userIdentifier, #id)")
    @DeleteMapping("/delete/{id}")
    public String deletePlayer(@PathVariable Long id,
                               @AuthenticationPrincipal FootballLeagueUserImpl user) {

        playerService.deletePlayer(id);

        return "redirect:/team/myteams";
    }

    //PLAYER DETAILS
    @GetMapping("/details/{id}")
    public String detailsPlayer(@PathVariable Long id, Model model,
                                @AuthenticationPrincipal FootballLeagueUserImpl user) {

        model.addAttribute("player", playerService.findPlayerById(id, user.getUserIdentifier()));
        model.addAttribute("stats", statService.findStatByPlayerId(id));

        return "details-player";
    }

    @ModelAttribute
    AddPlayerBindingModel addPlayerBindingModel() {
        return new AddPlayerBindingModel();
    }
}
