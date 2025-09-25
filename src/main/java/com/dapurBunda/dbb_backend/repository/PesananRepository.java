package com.dapurBunda.dbb_backend.repository;

import com.dapurBunda.dbb_backend.entity.Pesanan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PesananRepository extends JpaRepository<Pesanan, Long> {
    
    // cari pesanan berdasarkan pelanggan
    List<Pesanan> findByPelanggan_IdPelanggan(Long idPelanggan);

    // cari pesanan berdasarkan status
    List<Pesanan> findByStatus(String status);
}