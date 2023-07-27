package com.example.hireme.Controller.Admin;

import com.example.hireme.Model.Entity.City;
import com.example.hireme.Model.Entity.Country;
import com.example.hireme.Model.Entity.Support;
import com.example.hireme.Model.Entity.User;
import com.example.hireme.MultiLanguages.LanguageConfig;
import com.example.hireme.Requests.Admin.CreateUpdateSupportRequest;
import com.example.hireme.Service.CityService;
import com.example.hireme.Service.CountryService;
import com.example.hireme.Service.SupportService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Controller
@AllArgsConstructor
@RequestMapping("/admin/countries")
public class LocationsManagementController {

    private final CountryService countryService;
    private final CityService cityService;
    private final LanguageConfig languageConfig;


    @GetMapping()
    public String getCountriesPage(Authentication authentication, Model model){
        User user = (User) authentication.getPrincipal();
        List<Country> countries = countryService.getAll();
        model.addAttribute("user",user);
        model.addAttribute("countries",countries);
        model.addAttribute("type","dashboard");
        model.addAttribute("selected", "settings");
        return "Admin/countries";
    }

    @GetMapping("/{country_id}/cities")
    public String getCitiesPage(@PathVariable("country_id") Long country_id,Authentication authentication, Model model){
        User user = (User) authentication.getPrincipal();
        List<City> cities = cityService.getCitiesByCountry(country_id);
        model.addAttribute("user",user);
        model.addAttribute("cities",cities);
        model.addAttribute("type","dashboard");
        model.addAttribute("selected", "settings");
        return "Admin/cities";
    }



    @GetMapping("/change_state/{country_id}")
    public String changeCountryState(@PathVariable("country_id") Long country_id,
                                RedirectAttributes redirectAttributes,Locale locale,Model model){
        Optional<Country> country = countryService.findById(country_id);
        model.addAttribute("selected", "settings");
        if (country.isPresent()){
            countryService.changeCountryState(country.get());
            redirectAttributes.addFlashAttribute("successMessage",languageConfig.messageSource().getMessage("update",new Object[] {}, locale));
        }
        return "redirect:/admin/countries";
    }

    @GetMapping("/{country_id}/cities/change_state/{city_id}")
    public String changeCityState(@PathVariable("country_id") Long country_id,@PathVariable("city_id") Long city_id,
                                     RedirectAttributes redirectAttributes,Locale locale,Model model){
        Optional<City> city = cityService.findById(city_id);
        model.addAttribute("selected", "settings");
        if (city.isPresent()){
            cityService.changeCityState(city.get());
            redirectAttributes.addFlashAttribute("successMessage",languageConfig.messageSource().getMessage("update",new Object[] {}, locale));
        }
        return "redirect:/admin/countries/"+country_id+"/cities";
    }


}
