package com.ticketing.controller;

import com.ticketing.model.Driver;
import com.ticketing.model.Fine;
import com.ticketing.service.DriverService;
import com.ticketing.service.FineService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/driver")
@RequiredArgsConstructor
public class DriverControll {
    
    private final DriverService driverService;
    private final FineService fineService;
    
    @GetMapping
    public String showLoginPage() {
        return "driver/login";
    }
    
    @PostMapping("/login")
    public String login(@RequestParam String pesel, RedirectAttributes redirectAttributes) {
        Optional<Driver> driverOpt = driverService.findByPesel(pesel);
        
        if (driverOpt.isPresent()) {
            redirectAttributes.addAttribute("pesel", pesel);
            return "redirect:/driver/fines";
        } else {
            redirectAttributes.addFlashAttribute("error", "Invalid PESEL. Please try again.");
            return "redirect:/driver";
        }
    }
    
    @GetMapping("/fines")
    public String showFines(@RequestParam String pesel, Model model) {
        Optional<Driver> driverOpt = driverService.findByPesel(pesel);
        
        if (driverOpt.isPresent()) {
            Driver driver = driverOpt.get();
            List<Fine> fines = fineService.findByDriverPesel(pesel);
            Integer totalPoints = driverService.getTotalPenaltyPoints(pesel);
            
            model.addAttribute("driver", driver);
            model.addAttribute("fines", fines);
            model.addAttribute("totalPoints", totalPoints != null ? totalPoints : 0);
            
            return "driver/fines";
        } else {
            return "redirect:/driver";
        }
    }
    
    @GetMapping("/fine-details")
    public String showFineDetails(@RequestParam String fineNumber, @RequestParam String pesel, Model model) {
        Optional<Fine> fineOpt = fineService.findByFineNumber(fineNumber);
        Optional<Driver> driverOpt = driverService.findByPesel(pesel);
        
        if (fineOpt.isPresent() && driverOpt.isPresent()) {
            Fine fine = fineOpt.get();
            Driver driver = driverOpt.get();
            
            // Security check - only allow viewing own fines
            if (!fine.getDriver().getId().equals(driver.getId())) {
                return "redirect:/driver/fines?pesel=" + pesel;
            }
            
            model.addAttribute("fine", fine);
            model.addAttribute("driver", driver);
            
            return "driver/fine-details";
        } else {
            return "redirect:/driver/fines?pesel=" + pesel;
        }
    }
    
    @PostMapping("/accept-fine")
    public String acceptFine(@RequestParam String fineNumber, @RequestParam String pesel, RedirectAttributes redirectAttributes) {
        Optional<Fine> fineOpt = fineService.findByFineNumber(fineNumber);
        Optional<Driver> driverOpt = driverService.findByPesel(pesel);
        
        if (fineOpt.isPresent() && driverOpt.isPresent()) {
            Fine fine = fineOpt.get();
            Driver driver = driverOpt.get();
            
            // Security check - only allow accepting own fines
            if (!fine.getDriver().getId().equals(driver.getId())) {
                redirectAttributes.addFlashAttribute("error", "You can only accept your own fines.");
                return "redirect:/driver/fines?pesel=" + pesel;
            }
            
            try {
                fineService.acceptFine(fineNumber);
                redirectAttributes.addFlashAttribute("success", "Fine accepted successfully.");
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("error", "Could not accept fine: " + e.getMessage());
            }
        } else {
            redirectAttributes.addFlashAttribute("error", "Fine or driver not found.");
        }
        
        return "redirect:/driver/fines?pesel=" + pesel;
    }
}