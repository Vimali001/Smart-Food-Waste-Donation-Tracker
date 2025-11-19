package com.foodtracker.repository;

import com.foodtracker.model.NGO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface NGORepository extends JpaRepository<NGO, Long> {
    Optional<NGO> findByEmail(String email);
    boolean existsByEmail(String email);
    Optional<NGO> findByEmailAndPassword(String email, String password);
    List<NGO> findByStatus(String status);
    List<NGO> findByPincodeAndStatus(String pincode, String status);
}
