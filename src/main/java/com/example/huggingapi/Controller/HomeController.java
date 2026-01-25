package com.example.huggingapi.Controller;
/**
 * @author Emilija Sankauskaitė
 * Controllers are used for API endpoints.
 */
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController
{
    @GetMapping("/")
    public String home()
    {
        return "redirect:/simple.html";
    }
}
