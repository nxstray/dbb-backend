package com.dapurBunda.dbb_backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Table(name = "menu")
@Data
public class Menu {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_menu")
    private Long idMenu;
    
    @Column(name = "nama_menu", nullable = false, length = 150)
    private String namaMenu;
    
    @Column(name = "nama_kategori", length = 100)
    private String namaKategori;
    
    @Column(name = "harga", nullable = false)
    private Float harga;
    
    @Column(name = "stok", nullable = false)
    private Integer stok;
    
    // Foreign Key
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_kategori", nullable = false)
    private Kategori kategori;
    
    // Relationship
    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DetailPesanan> detailPesananList;
}