package com.dapurBunda.dbb_backend.service;

import com.dapurBunda.dbb_backend.entity.Admin;
import com.dapurBunda.dbb_backend.entity.Pelanggan;
import com.dapurBunda.dbb_backend.repository.AdminRepository;
import com.dapurBunda.dbb_backend.repository.PelangganRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PasswordMigrationService {
    
    @Autowired
    private PelangganRepository pelangganRepo;
    
    @Autowired
    private AdminRepository adminRepo;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    // Method untuk migrate password pelanggan yang sudah ada
    public void migratePelangganPasswords() {
        List<Pelanggan> pelanggans = pelangganRepo.findAll();
        
        for (Pelanggan pelanggan : pelanggans) {
            String currentPassword = pelanggan.getPassword();
            
            // Cek apakah password sudah di-hash (BCrypt hash dimulai dengan $2a$, $2b$, atau $2y$)
            if (!currentPassword.startsWith("$2a$") && 
                !currentPassword.startsWith("$2b$") && 
                !currentPassword.startsWith("$2y$")) {
                
                // Hash password lama
                String hashedPassword = passwordEncoder.encode(currentPassword);
                pelanggan.setPassword(hashedPassword);
                pelangganRepo.save(pelanggan);
                
                System.out.println("Password migrated for pelanggan: " + pelanggan.getUsername());
            }
        }
    }
    
    // Method untuk migrate password admin yang sudah ada
    public void migrateAdminPasswords() {
        List<Admin> admins = adminRepo.findAll();
        
        for (Admin admin : admins) {
            String currentPassword = admin.getPassword();
            
            // Cek apakah password sudah di-hash
            if (!currentPassword.startsWith("$2a$") && 
                !currentPassword.startsWith("$2b$") && 
                !currentPassword.startsWith("$2y$")) {
                
                // Hash password lama
                String hashedPassword = passwordEncoder.encode(currentPassword);
                admin.setPassword(hashedPassword);
                adminRepo.save(admin);
                
                System.out.println("Password migrated for admin: " + admin.getUsername());
            }
        }
    }
}