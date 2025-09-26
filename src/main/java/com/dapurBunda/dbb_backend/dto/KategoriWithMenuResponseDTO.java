package com.dapurBunda.dbb_backend.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KategoriWithMenuResponseDTO {
    private String nama;
    private List<MenuItemResponseDTO> items;
}