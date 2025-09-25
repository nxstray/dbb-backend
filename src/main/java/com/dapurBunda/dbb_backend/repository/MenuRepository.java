package com.dapurBunda.dbb_backend.repository;

import com.dapurBunda.dbb_backend.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {
    
    // Cari menu berdasarkan nama (like %nama%)
    List<Menu> findByNamaMenuContainingIgnoreCase(String namaMenu);

    // Cari menu berdasarkan kategori id
    List<Menu> findByKategori_IdKategori(Long idKategori);

    // Cari menu dengan stok lebih dari 0
    List<Menu> findByStokGreaterThan(Integer stok);
}