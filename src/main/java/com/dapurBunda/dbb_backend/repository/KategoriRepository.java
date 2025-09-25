package com.dapurBunda.dbb_backend.repository;

import com.dapurBunda.dbb_backend.entity.Kategori;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KategoriRepository extends JpaRepository<Kategori, Integer> {

    // Cari kategori berdasarkan nama persis
    Optional<Kategori> findByNamaKategori(String namaKategori);

    // Cari kategori berdasarkan nama yang mirip (case-insensitive)
    boolean existsByNamaKategoriIgnoreCase(String namaKategori);
}