package com.dapurBunda.dbb_backend.repository;

import com.dapurBunda.dbb_backend.entity.Pembayaran;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PembayaranRepository extends JpaRepository<Pembayaran, Long> {
    
    // Cari pembayaran berdasarkan id pesanan
    List<Pembayaran> findByPesanan_IdPesanan(Long idPesanan);

    // Cari pembayaran berdasarkan metode bayar
    List<Pembayaran> findByMetodeByr(String metodeByr);

    // Cari pembayaran berdasarkan tanggal bayar
    List<Pembayaran> findByTanggalBayar(java.util.Date tanggalBayar);
}