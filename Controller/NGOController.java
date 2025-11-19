package com.foodtracker.controller;

import com.foodtracker.model.NGO;
import com.foodtracker.service.NGOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ngos")
@CrossOrigin(origins = "*")
public class NGOController {

    @Autowired
    private NGOService ngoService;

    @PostMapping("/register")
    public ResponseEntity<?> registerNGO(@RequestBody NGO ngo) {
        try {
            NGO newNGO = ngoService.registerNGO(ngo);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "NGO registered successfully. Waiting for admin approval.",
                "ngo", newNGO
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginNGO(@RequestBody Map<String, String> credentials, HttpSession session) {
        try {
            String email = credentials.get("email");
            String password = credentials.get("password");
            NGO ngo = ngoService.loginNGO(email, password);
            
            if (!"APPROVED".equals(ngo.getStatus())) {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Your NGO is not approved yet. Please wait for admin approval."
                ));
            }
            
            session.setAttribute("ngoId", ngo.getId());
            session.setAttribute("userRole", "NGO");
            session.setAttribute("ngoName", ngo.getNgoName());
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Login successful",
                "ngo", ngo
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllNGOs() {
        try {
            List<NGO> ngos = ngoService.getAllNGOs();
            return ResponseEntity.ok(ngos);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }

    @GetMapping("/approved")
    public ResponseEntity<?> getApprovedNGOs() {
        try {
            List<NGO> ngos = ngoService.getApprovedNGOs();
            return ResponseEntity.ok(ngos);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }

    @GetMapping("/nearby")
    public ResponseEntity<?> findNearbyNGOs(@RequestParam String pincode) {
        try {
            List<NGO> ngos = ngoService.findNearbyNGOs(pincode);
            return ResponseEntity.ok(ngos);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getNGOById(@PathVariable Long id) {
        try {
            NGO ngo = ngoService.getNGOById(id);
            return ResponseEntity.ok(ngo);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutNGO(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok(Map.of(
            "success", true,
            "message", "Logout successful"
        ));
    }
}
