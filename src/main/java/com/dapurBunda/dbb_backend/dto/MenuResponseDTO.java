package com.dapurBunda.dbb_backend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MenuResponseDTO {
    private Long id;
    private String nama;
    private String deskripsi;
    private Float harga;
    private String gambar;
    private Integer stok;
    private String namaKategori;
}