package com.dapurBunda.dbb_backend.controller;

import com.dapurBunda.dbb_backend.service.PasswordMigrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/migration")
@CrossOrigin(origins = "http://localhost:5173")
public class PasswordMigrationController {
    
    @Autowired
    private PasswordMigrationService migrationService;
    
    // khusus ditahap development
    @PostMapping("/migrate-passwords")
    public ResponseEntity<?> migratePasswords() {
        try {
            migrationService.migratePelangganPasswords();
            migrationService.migrateAdminPasswords();
            return ResponseEntity.ok("Password migration completed successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Migration failed: " + e.getMessage());
        }
    }
}