package com.dapurBunda.dbb_backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "pesanan")
@Data
public class Pesanan {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pesanan")
    private Long idPesanan;
    
    // Foreign Key
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pelanggan", nullable = false)
    private Pelanggan pelanggan;
    
    @Column(name = "tanggal_pemesanan", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date tanggalPemesanan;
    
    @Column(name = "total", nullable = false)
    private Float total;
    
    @Column(name = "status", nullable = false, length = 50)
    private String status;
    
    // Relationships
    @OneToMany(mappedBy = "pesanan", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DetailPesanan> detailPesananList;
    
    @OneToMany(mappedBy = "pesanan", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Pembayaran> pembayaranList;
}
