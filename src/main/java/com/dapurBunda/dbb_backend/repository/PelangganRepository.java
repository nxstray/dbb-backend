package com.dapurBunda.dbb_backend.repository;

import com.dapurBunda.dbb_backend.entity.Pelanggan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PelangganRepository extends JpaRepository<Pelanggan, Long> {
    
    // Cari pelanggan berdasarkan username
    Optional<Pelanggan> findByUsername(String username);

    // Cari pelanggan berdasarkan email
    Optional<Pelanggan> findByEmail(String email);

    // Cek apakah email sudah terdaftar
    boolean existsByEmail(String email);

    // Cek apakah username sudah terdaftar
    boolean existsByUsername(String username);
}