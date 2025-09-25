package com.dapurBunda.dbb_backend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "detail_pesanan")
@Data
public class DetailPesanan {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detail_pesanan")
    private Long idDetailPesanan;
    
    // Foreign Key Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pesanan", nullable = false)
    private Pesanan pesanan;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_menu", nullable = false)
    private Menu menu;
    
    @Column(name = "jumlah", nullable = false)
    private Integer jumlah;
    
    @Column(name = "subtotal", nullable = false)
    private Float subtotal;
}