package com.dapurBunda.dbb_backend.controller;

import com.dapurBunda.dbb_backend.entity.Admin;
import com.dapurBunda.dbb_backend.entity.Pelanggan;
import com.dapurBunda.dbb_backend.repository.AdminRepository;
import com.dapurBunda.dbb_backend.repository.PelangganRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    @Autowired
    private PelangganRepository pelangganRepo;
    
    @Autowired
    private AdminRepository adminRepo;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Pelanggan req) {
        try {
            // Validasi email sudah ada atau belum
            if (pelangganRepo.findByEmail(req.getEmail()).isPresent()) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "Email sudah digunakan!");
                return ResponseEntity.badRequest().body(errorResponse);
            }
            
            // Validasi username sudah ada atau belum
            if (pelangganRepo.findByUsername(req.getUsername()).isPresent()) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "Username sudah digunakan!");
                return ResponseEntity.badRequest().body(errorResponse);
            }
            
            // Validasi input tidak kosong
            if (req.getUsername() == null || req.getUsername().trim().isEmpty() ||
                req.getEmail() == null || req.getEmail().trim().isEmpty() ||
                req.getPassword() == null || req.getPassword().trim().isEmpty()) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "Semua field harus diisi!");
                return ResponseEntity.badRequest().body(errorResponse);
            }
            
            // Validasi panjang password minimal 6 karakter
            if (req.getPassword().length() < 6) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "Password minimal 6 karakter!");
                return ResponseEntity.badRequest().body(errorResponse);
            }
            
            Pelanggan pelanggan = new Pelanggan();
            pelanggan.setUsername(req.getUsername());
            pelanggan.setEmail(req.getEmail());
            // Hash password sebelum disimpan
            pelanggan.setPassword(passwordEncoder.encode(req.getPassword()));
            
            Pelanggan savedPelanggan = pelangganRepo.save(pelanggan);
            
            // Response sukses
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Registrasi berhasil!");
            response.put("user", Map.of(
                "idPelanggan", savedPelanggan.getIdPelanggan(),
                "username", savedPelanggan.getUsername(),
                "email", savedPelanggan.getEmail()
            ));
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Terjadi kesalahan saat registrasi: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Pelanggan req) {
        try {
            Optional<Pelanggan> userOpt = pelangganRepo.findByEmail(req.getEmail());
            
            if (userOpt.isPresent()) {
                Pelanggan user = userOpt.get();
                
                // Verifikasi password
                if (passwordEncoder.matches(req.getPassword(), user.getPassword())) {
                    
                    // Debug - Print user data
                    System.out.println("=== LOGIN SUCCESS DEBUG ===");
                    System.out.println("User ID: " + user.getIdPelanggan());
                    System.out.println("Username: " + user.getUsername());
                    System.out.println("Email: " + user.getEmail());
                    
                    // Response sukses
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", true);
                    response.put("message", "Login berhasil!");
                    response.put("role", "pelanggan");
                    
                    // User object dengan field yang jelas
                    Map<String, Object> userData = new HashMap<>();
                    userData.put("idPelanggan", user.getIdPelanggan());
                    userData.put("id_pelanggan", user.getIdPelanggan());
                    userData.put("id", user.getIdPelanggan());
                    userData.put("username", user.getUsername());
                    userData.put("email", user.getEmail());
                    
                    response.put("user", userData);
                    
                    System.out.println("Response user data: " + userData);
                    
                    return ResponseEntity.ok(response);
                } else {
                    Map<String, Object> errorResponse = new HashMap<>();
                    errorResponse.put("success", false);
                    errorResponse.put("message", "Password salah!");
                    return ResponseEntity.badRequest().body(errorResponse);
                }
            }
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "User tidak ditemukan!");
            return ResponseEntity.badRequest().body(errorResponse);
            
        } catch (Exception e) {
            System.err.println("Login error: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Terjadi kesalahan saat login: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    // Login Admin (buat nanti)
    @PostMapping("/admin/login")
    public ResponseEntity<?> loginAdmin(@RequestBody Admin req) {
        try {
            Optional<Admin> adminOpt = adminRepo.findById(req.getUsername());
            
            if (adminOpt.isPresent()) {
                Admin admin = adminOpt.get();
                
                // Verifikasi password menggunakan BCrypt
                if (passwordEncoder.matches(req.getPassword(), admin.getPassword())) {
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", true);
                    response.put("message", "Login admin berhasil!");
                    response.put("role", "admin");
                    response.put("user", Map.of(
                        "username", admin.getUsername()
                    ));
                    
                    return ResponseEntity.ok(response);
                } else {
                    Map<String, Object> errorResponse = new HashMap<>();
                    errorResponse.put("success", false);
                    errorResponse.put("message", "Password salah!");
                    return ResponseEntity.badRequest().body(errorResponse);
                }
            }
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Admin tidak ditemukan!");
            return ResponseEntity.badRequest().body(errorResponse);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Terjadi kesalahan saat login: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}