package cz.upce.webapp.controller;

import cz.upce.webapp.dao.users.model.User;
import cz.upce.webapp.service.SecurityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Jan Houžvička
 */

@Controller
public class LoginController
{
    private final SecurityServiceImpl securityService;

    @Autowired
    public LoginController(SecurityServiceImpl securityService)
    {
        this.securityService = securityService;
    }

    @RequestMapping(value = "/loginafter", method = RequestMethod.POST)
    public String login(Model model, @ModelAttribute("user") User user, HttpServletResponse response)
    {
        try
        {
            securityService.login(user.getEmail(), user.getPassword());
            response.sendRedirect("/dashboard");
        }
        catch (AuthenticationException | IOException e)
        {
            model.addAttribute("error", "Invalid login credentials!");
        }

        return "/homepage/login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginGet(Model model)
    {
        User user = new User();
        model.addAttribute("user", user);
        return "homepage/login";
    }
}
