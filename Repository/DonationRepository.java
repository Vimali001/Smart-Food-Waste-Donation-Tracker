package com.foodtracker.repository;

import com.foodtracker.model.Donation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DonationRepository extends JpaRepository<Donation, Long> {
    List<Donation> findByUserId(Long userId);
    List<Donation> findByNgoId(Long ngoId);
    List<Donation> findByNgoIdAndStatus(Long ngoId, String status);
    List<Donation> findByStatus(String status);
}
