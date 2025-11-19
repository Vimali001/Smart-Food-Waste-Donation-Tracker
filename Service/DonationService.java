package com.foodtracker.service;

import com.foodtracker.model.Donation;
import com.foodtracker.model.FoodItem;
import com.foodtracker.model.NGO;
import com.foodtracker.model.User;
import com.foodtracker.repository.DonationRepository;
import com.foodtracker.repository.FoodItemRepository;
import com.foodtracker.repository.NGORepository;
import com.foodtracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class DonationService {

    @Autowired
    private DonationRepository donationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FoodItemRepository foodItemRepository;

    @Autowired
    private NGORepository ngoRepository;

    public Donation createDonationRequest(Long userId, Long foodId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        FoodItem foodItem = foodItemRepository.findById(foodId)
                .orElseThrow(() -> new RuntimeException("Food item not found"));

        if (!"ACTIVE".equals(foodItem.getStatus())) {
            throw new RuntimeException("Food item is not available for donation");
        }

        Donation donation = new Donation(user, foodItem);
        donation.setStatus("PENDING");
        
        // Mark food item as being donated
        foodItem.setStatus("DONATED");
        foodItemRepository.save(foodItem);

        return donationRepository.save(donation);
    }

    public Donation acceptDonation(Long donationId, Long ngoId) {
        Donation donation = donationRepository.findById(donationId)
                .orElseThrow(() -> new RuntimeException("Donation not found"));
        
        NGO ngo = ngoRepository.findById(ngoId)
                .orElseThrow(() -> new RuntimeException("NGO not found"));

        if (!"APPROVED".equals(ngo.getStatus())) {
            throw new RuntimeException("NGO is not approved");
        }

        donation.setNgo(ngo);
        donation.setStatus("ACCEPTED");
        
        return donationRepository.save(donation);
    }

    public Donation updateDonationStatus(Long donationId, String status) {
        Donation donation = donationRepository.findById(donationId)
                .orElseThrow(() -> new RuntimeException("Donation not found"));

        donation.setStatus(status);

        if ("COLLECTED".equals(status)) {
            donation.setPickupDate(LocalDate.now());
        } else if ("DELIVERED".equals(status) || "COMPLETED".equals(status)) {
            donation.setDeliveryDate(LocalDate.now());
        }

        if ("COMPLETED".equals(status)) {
            FoodItem foodItem = donation.getFoodItem();
            foodItem.setStatus("DONATED");
            foodItemRepository.save(foodItem);
        }

        return donationRepository.save(donation);
    }

    public List<Donation> getUserDonations(Long userId) {
        return donationRepository.findByUserId(userId);
    }

    public List<Donation> getNGODonations(Long ngoId) {
        return donationRepository.findByNgoId(ngoId);
    }

    public List<Donation> getPendingDonations() {
        return donationRepository.findByStatus("PENDING");
    }

    public List<Donation> getPendingDonationsForNGO(Long ngoId) {
        return donationRepository.findByNgoIdAndStatus(ngoId, "PENDING");
    }

    public Donation getDonationById(Long id) {
        return donationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Donation not found"));
    }
}
