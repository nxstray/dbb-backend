package com.dapurBunda.dbb_backend.dto;

import lombok.Data;
import java.util.List;

@Data
public class CheckoutRequestDTO {
    private int idPelanggan;
    private float total;
    private List<Item> items;

    @Data
    public static class Item {
        private int idMenu;
        private int jumlah;
        private float subtotal;
    }
}