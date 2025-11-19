package com.foodtracker.controller;

import com.foodtracker.model.NGO;
import com.foodtracker.model.User;
import com.foodtracker.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/login")
    public ResponseEntity<?> loginAdmin(@RequestBody Map<String, String> credentials, HttpSession session) {
        try {
            String email = credentials.get("email");
            String password = credentials.get("password");
            
            // Check if admin exists (default admin: admin@foodtracker.com / admin123)
            if ("admin@foodtracker.com".equals(email) && "admin123".equals(password)) {
                session.setAttribute("userRole", "ADMIN");
                session.setAttribute("adminId", 1L);
                return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Admin login successful"
                ));
            }
            
            // Also check if user exists with ADMIN role
            User admin = adminService.loginAdmin(email, password);
            session.setAttribute("userRole", "ADMIN");
            session.setAttribute("adminId", admin.getId());
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Admin login successful",
                "admin", admin
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }

    @GetMapping("/statistics")
    public ResponseEntity<?> getStatistics(HttpSession session) {
        String role = (String) session.getAttribute("userRole");
        if (!"ADMIN".equals(role)) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Unauthorized. Admin access required."
            ));
        }
        try {
            Map<String, Object> stats = adminService.getStatistics();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers(HttpSession session) {
        String role = (String) session.getAttribute("userRole");
        if (!"ADMIN".equals(role)) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Unauthorized. Admin access required."
            ));
        }
        try {
            return ResponseEntity.ok(adminService.getAllUsers());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }

    @GetMapping("/ngos")
    public ResponseEntity<?> getAllNGOs(HttpSession session) {
        String role = (String) session.getAttribute("userRole");
        if (!"ADMIN".equals(role)) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Unauthorized. Admin access required."
            ));
        }
        try {
            return ResponseEntity.ok(adminService.getAllNGOs());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }

    @GetMapping("/ngos/pending")
    public ResponseEntity<?> getPendingNGOs(HttpSession session) {
        String role = (String) session.getAttribute("userRole");
        if (!"ADMIN".equals(role)) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Unauthorized. Admin access required."
            ));
        }
        try {
            return ResponseEntity.ok(adminService.getPendingNGOs());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }

    @PutMapping("/ngos/{id}/approve")
    public ResponseEntity<?> approveNGO(@PathVariable Long id, HttpSession session) {
        String role = (String) session.getAttribute("userRole");
        if (!"ADMIN".equals(role)) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Unauthorized. Admin access required."
            ));
        }
        try {
            NGO ngo = adminService.approveNGO(id);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "NGO approved successfully",
                "ngo", ngo
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }

    @PutMapping("/ngos/{id}/reject")
    public ResponseEntity<?> rejectNGO(@PathVariable Long id, HttpSession session) {
        String role = (String) session.getAttribute("userRole");
        if (!"ADMIN".equals(role)) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Unauthorized. Admin access required."
            ));
        }
        try {
            adminService.rejectNGO(id);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "NGO rejected successfully"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id, HttpSession session) {
        String role = (String) session.getAttribute("userRole");
        if (!"ADMIN".equals(role)) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Unauthorized. Admin access required."
            ));
        }
        try {
            adminService.deleteUser(id);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "User deleted successfully"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutAdmin(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok(Map.of(
            "success", true,
            "message", "Admin logout successful"
        ));
    }
}

