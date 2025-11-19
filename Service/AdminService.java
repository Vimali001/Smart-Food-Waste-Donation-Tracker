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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NGORepository ngoRepository;

    @Autowired
    private FoodItemRepository foodItemRepository;

    @Autowired
    private DonationRepository donationRepository;

    public User loginAdmin(String email, String password) {
        return userRepository.findByEmailAndPassword(email, password)
                .orElseThrow(() -> new RuntimeException("Invalid admin credentials"));
    }

    public Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();

        // Total users
        long totalUsers = userRepository.count();
        stats.put("totalUsers", totalUsers);

        // Total NGOs
        long totalNGOs = ngoRepository.count();
        stats.put("totalNGOs", totalNGOs);

        // Approved NGOs
        long approvedNGOs = ngoRepository.findByStatus("APPROVED").size();
        stats.put("approvedNGOs", approvedNGOs);

        // Total food items saved
        long totalFoodItems = foodItemRepository.count();
        stats.put("totalFoodItems", totalFoodItems);

        // Total donations
        long totalDonations = donationRepository.count();
        stats.put("totalDonations", totalDonations);

        // Completed donations
        long completedDonations = donationRepository.findByStatus("COMPLETED").size();
        stats.put("completedDonations", completedDonations);

        // Pending donations
        long pendingDonations = donationRepository.findByStatus("PENDING").size();
        stats.put("pendingDonations", pendingDonations);

        // Food items donated
        long donatedItems = foodItemRepository.findByStatus("DONATED").size();
        stats.put("donatedItems", donatedItems);

        // Food items expired
        long expiredItems = foodItemRepository.findByStatus("EXPIRED").size();
        stats.put("expiredItems", expiredItems);

        // Calculate waste reduced (assuming each donation saves food from waste)
        stats.put("wasteReduced", completedDonations);

        return stats;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<NGO> getAllNGOs() {
        return ngoRepository.findAll();
    }

    public List<Donation> getAllDonations() {
        return donationRepository.findAll();
    }

    public List<NGO> getPendingNGOs() {
        return ngoRepository.findByStatus("PENDING");
    }

    public NGO approveNGO(Long id) {
        NGO ngo = ngoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("NGO not found"));
        ngo.setStatus("APPROVED");
        return ngoRepository.save(ngo);
    }

    public void rejectNGO(Long id) {
        NGO ngo = ngoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("NGO not found"));
        ngo.setStatus("REJECTED");
        ngoRepository.save(ngo);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
