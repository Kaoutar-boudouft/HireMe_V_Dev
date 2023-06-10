package com.example.hireme.Controller;

import com.example.hireme.Model.Entity.Country;
import com.example.hireme.Model.Entity.OfferCategory;
import com.example.hireme.Model.Entity.User;
import com.example.hireme.Service.CountryService;
import com.example.hireme.Service.OfferCategoryService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@AllArgsConstructor
public class HomeController {
    private final CountryService countryService;
    private final OfferCategoryService offerCategoryService;
    @GetMapping("/")
    public String getHomePage(Authentication authentication, Model model){
        List<Country> countries = countryService.getActiveCountries();
        List<OfferCategory> categories = offerCategoryService.getAllCategories();
        User user;
        if (authentication!=null){
            user = (User) authentication.getPrincipal();
            model.addAttribute("role",user.getRole().toString());
        }
        else user = null;
        model.addAttribute("user",user);
        model.addAttribute("countries",countries);
        model.addAttribute("categories",categories);
        model.addAttribute("type","home");
        return "home";
    }

    @GetMapping("/error")
    public String getErrorPage(){
        return "error";
    }
}
