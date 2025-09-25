package com.dapurBunda.dbb_backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Table(name = "pelanggan")
@Data
public class Pelanggan {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pelanggan")
    private Long idPelanggan;
    
    @Column(name = "username", nullable = false, length = 100)
    private String username;
    
    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;
    
    // Relationship
    @OneToMany(mappedBy = "pelanggan", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Pesanan> pesananList;
}