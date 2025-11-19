package com.foodtracker.controller;

import com.foodtracker.model.Donation;
import com.foodtracker.service.DonationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/donations")
@CrossOrigin(origins = "*")
public class DonationController {

    @Autowired
    private DonationService donationService;

    @PostMapping
    public ResponseEntity<?> createDonationRequest(
            @RequestBody Map<String, Long> request,
            HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Please login first"
            ));
        }
        try {
            Long foodId = request.get("foodId");
            Donation donation = donationService.createDonationRequest(userId, foodId);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Donation request created successfully",
                "donation", donation
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }

    @PostMapping("/{donationId}/accept")
    public ResponseEntity<?> acceptDonation(
            @PathVariable Long donationId,
            @RequestBody(required = false) Map<String, Long> request,
            HttpSession session) {
        Long ngoId = (Long) session.getAttribute("ngoId");
        if (ngoId == null) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Please login as NGO first"
            ));
        }
        try {
            Donation donation = donationService.acceptDonation(donationId, ngoId);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Donation accepted successfully",
                "donation", donation
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }

    @PutMapping("/{donationId}/status")
    public ResponseEntity<?> updateDonationStatus(
            @PathVariable Long donationId,
            @RequestBody Map<String, String> request,
            HttpSession session) {
        Long ngoId = (Long) session.getAttribute("ngoId");
        if (ngoId == null) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Please login as NGO first"
            ));
        }
        try {
            String status = request.get("status");
            Donation donation = donationService.updateDonationStatus(donationId, status);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Status updated successfully",
                "donation", donation
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUserDonations(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Please login first"
            ));
        }
        try {
            List<Donation> donations = donationService.getUserDonations(userId);
            return ResponseEntity.ok(donations);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }

    @GetMapping("/ngo")
    public ResponseEntity<?> getNGODonations(HttpSession session) {
        Long ngoId = (Long) session.getAttribute("ngoId");
        if (ngoId == null) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Please login as NGO first"
            ));
        }
        try {
            List<Donation> donations = donationService.getNGODonations(ngoId);
            return ResponseEntity.ok(donations);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }

    @GetMapping("/pending")
    public ResponseEntity<?> getPendingDonations(HttpSession session) {
        Long ngoId = (Long) session.getAttribute("ngoId");
        try {
            // If NGO is logged in, return all pending donations (they can see all)
            List<Donation> donations = donationService.getPendingDonations();
            return ResponseEntity.ok(donations);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }

    @GetMapping("/nearby")
    public ResponseEntity<?> getNearbyDonations(@RequestParam String pincode) {
        try {
            List<Donation> donations = donationService.getPendingDonations();
            // Filter by pincode if needed
            return ResponseEntity.ok(donations);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDonationById(@PathVariable Long id) {
        try {
            Donation donation = donationService.getDonationById(id);
            return ResponseEntity.ok(donation);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }
}
