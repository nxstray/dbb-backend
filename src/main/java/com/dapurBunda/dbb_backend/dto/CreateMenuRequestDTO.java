package com.dapurBunda.dbb_backend.dto;

import lombok.Data;

@Data
public class CreateMenuRequestDTO {
    private String namaMenu;
    private String deskripsi;
    private String gambar;
    private Float harga;
    private Integer stok;
    private Integer idKategori;
}