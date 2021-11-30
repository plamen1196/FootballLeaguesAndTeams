package com.example.FootballLeagues.web;

import com.example.FootballLeagues.model.binding.ChangeUserRole;
import com.example.FootballLeagues.model.entity.User;
import com.example.FootballLeagues.service.UserService;
import com.example.FootballLeagues.service.impl.FootballLeagueUserImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    //GET VIEW FOR CHANGING ROLES
    @GetMapping("/user/roles")
    public String selectUser(Model model) {

        if (!model.containsAttribute("changeUserRole")){
            model.addAttribute("changeUserRole", changeUserRole());
        }
        if (!model.containsAttribute("bad_credentials")){
            model.addAttribute("bad_credentials", false);
        }

        model.addAttribute("users", userService.findAllUsersExceptAdmin());

        return "change-user-roles";
    }

    //CHANGE ROLE
    @PatchMapping("/user/roles")
    public String changeUserRole(@Valid ChangeUserRole changeUserRole,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes,
                                 @AuthenticationPrincipal FootballLeagueUserImpl user) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("changeUserRole", changeUserRole)
                    .addFlashAttribute("org.springframework.validation.BindingResult.changeUserRole", bindingResult);
            return "redirect:roles";
        }

        User userWithNewRole = userService.changeRoleOfUser(changeUserRole, user.getUserIdentifier());

        if (userWithNewRole == null) {

            redirectAttributes
                    .addFlashAttribute("bad_credentials", true);

            return "redirect:roles";
        }

        return "redirect:/";
    }

    @ModelAttribute
    public ChangeUserRole changeUserRole(){
        return new ChangeUserRole();
    }
}
