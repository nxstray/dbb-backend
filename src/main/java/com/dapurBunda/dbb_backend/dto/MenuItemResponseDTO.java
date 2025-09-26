package com.dapurBunda.dbb_backend.dto;

import com.dapurBunda.dbb_backend.entity.Menu;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MenuItemResponseDTO {
    private Long id;
    private String nama;
    private String deskripsi;
    private Float harga;
    private String gambar;
    private Integer stok;
    
    public MenuItemResponseDTO(Menu menu) {
        this.id = menu.getIdMenu();
        this.nama = menu.getNamaMenu();
        this.deskripsi = menu.getDeskripsi();
        this.harga = menu.getHarga();
        this.gambar = menu.getGambar();
        this.stok = menu.getStok();
    }
}