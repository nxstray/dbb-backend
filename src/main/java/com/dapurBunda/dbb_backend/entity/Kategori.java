package com.dapurBunda.dbb_backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Table(name = "kategori")
@Data
public class Kategori {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_kategori")
    private Integer idKategori;
    
    @Column(name = "nama_kategori", nullable = false, length = 100)
    private String namaKategori;
    
    // Relationship
    @OneToMany(mappedBy = "kategori", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Menu> menuList;
}