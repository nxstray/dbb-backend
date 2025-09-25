package com.dapurBunda.dbb_backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Entity
@Table(name = "pembayaran")
@Data
public class Pembayaran {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pembayaran")
    private Long idPembayaran;
    
    // Foreign Key Relationship
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pesanan", nullable = false)
    private Pesanan pesanan;
    
    @Column(name = "metode_byr", nullable = false, length = 50)
    private String metodeByr;
    
    @Column(name = "jumlah_byr", nullable = false)
    private Float jumlahByr;
    
    @Column(name = "tanggal_bayar", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date tanggalBayar;
}