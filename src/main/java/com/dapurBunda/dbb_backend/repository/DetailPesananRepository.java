package com.dapurBunda.dbb_backend.repository;

import com.dapurBunda.dbb_backend.entity.DetailPesanan;
import com.dapurBunda.dbb_backend.entity.Pesanan;
import com.dapurBunda.dbb_backend.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetailPesananRepository extends JpaRepository<DetailPesanan, Long> {
    
    // Cari detail pesanan berdasarkan pesanan
    List<DetailPesanan> findByPesanan(Pesanan pesanan);

    // Cari detail pesanan berdasarkan menu
    List<DetailPesanan> findByMenu(Menu menu);

    // Cari semua detail pesanan berdasarkan id pesanan
    List<DetailPesanan> findByPesanan_IdPesanan(Long idPesanan);
}