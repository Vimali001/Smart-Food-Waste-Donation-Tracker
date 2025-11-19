package com.foodtracker.service;

import com.foodtracker.model.NGO;
import com.foodtracker.repository.NGORepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class NGOService {

    @Autowired
    private NGORepository ngoRepository;

    public NGO registerNGO(NGO ngo) {
        if (ngoRepository.existsByEmail(ngo.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        return ngoRepository.save(ngo);
    }

    public NGO loginNGO(String email, String password) {
        Optional<NGO> ngo = ngoRepository.findByEmailAndPassword(email, password);
        if (ngo.isPresent()) {
            return ngo.get();
        }
        throw new RuntimeException("Invalid email or password");
    }

    public NGO getNGOById(Long id) {
        return ngoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("NGO not found"));
    }

    public List<NGO> getAllNGOs() {
        return ngoRepository.findAll();
    }

    public List<NGO> getApprovedNGOs() {
        return ngoRepository.findByStatus("APPROVED");
    }

    public List<NGO> getPendingNGOs() {
        return ngoRepository.findByStatus("PENDING");
    }

    public NGO approveNGO(Long id) {
        NGO ngo = getNGOById(id);
        ngo.setStatus("APPROVED");
        return ngoRepository.save(ngo);
    }

    public NGO rejectNGO(Long id) {
        NGO ngo = getNGOById(id);
        ngo.setStatus("REJECTED");
        return ngoRepository.save(ngo);
    }

    public List<NGO> findNearbyNGOs(String pincode) {
        return ngoRepository.findByPincodeAndStatus(pincode, "APPROVED");
    }

    public NGO updateNGO(NGO ngo) {
        return ngoRepository.save(ngo);
    }

    public void deleteNGO(Long id) {
        ngoRepository.deleteById(id);
    }
}
