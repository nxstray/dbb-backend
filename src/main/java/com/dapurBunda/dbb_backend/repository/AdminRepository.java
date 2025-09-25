package com.dapurBunda.dbb_backend.repository;

import com.dapurBunda.dbb_backend.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, String> {
    
    // Cari admin berdasarkan username
    Admin findByUsername(String username);
    
    // Validasi login (username + password)
    Admin findByUsernameAndPassword(String username, String password);
}