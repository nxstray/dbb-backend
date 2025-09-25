// PesananController.java - Improved with debugging
package com.dapurBunda.dbb_backend.controller;

import com.dapurBunda.dbb_backend.entity.*;
import com.dapurBunda.dbb_backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/pesanan")
@CrossOrigin(origins = "http://localhost:5173")
public class PesananController {

    @Autowired
    private PesananRepository pesananRepository;
    
    @Autowired
    private MenuRepository menuRepository;
    
    @Autowired
    private PelangganRepository pelangganRepository;
    
    @Autowired
    private DetailPesananRepository detailPesananRepository;

    // DTO class untuk menerima request
    public static class CheckoutRequest {
        private Long pelangganId;
        private List<CartItem> items;
        
        // Getters and setters
        public Long getPelangganId() { return pelangganId; }
        public void setPelangganId(Long pelangganId) { this.pelangganId = pelangganId; }
        
        public List<CartItem> getItems() { return items; }
        public void setItems(List<CartItem> items) { this.items = items; }
    }
    
    public static class CartItem {
        private Long menuId;
        private Integer quantity;
        
        // Getters and setters
        public Long getMenuId() { return menuId; }
        public void setMenuId(Long menuId) { this.menuId = menuId; }
        
        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
    }

    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(@RequestBody CheckoutRequest request) {
        try {
            System.out.println("=== DEBUG CHECKOUT ===");
            System.out.println("Pelanggan ID: " + request.getPelangganId());
            System.out.println("Items count: " + (request.getItems() != null ? request.getItems().size() : 0));
            
            Map<String, Object> response = new HashMap<>();
            
            // Validasi input
            if (request.getPelangganId() == null) {
                response.put("success", false);
                response.put("message", "ID Pelanggan tidak boleh kosong");
                return ResponseEntity.badRequest().body(response);
            }
            
            if (request.getItems() == null || request.getItems().isEmpty()) {
                response.put("success", false);
                response.put("message", "Keranjang kosong");
                return ResponseEntity.badRequest().body(response);
            }
            
            // Cari pelanggan
            Optional<Pelanggan> pelangganOpt = pelangganRepository.findById(request.getPelangganId());
            if (!pelangganOpt.isPresent()) {
                response.put("success", false);
                response.put("message", "Pelanggan tidak ditemukan");
                return ResponseEntity.badRequest().body(response);
            }
            
            Pelanggan pelanggan = pelangganOpt.get();
            System.out.println("Found pelanggan: " + pelanggan.getUsername());
            
            // Hitung total dan validasi menu
            Float total = 0f;
            
            for (CartItem item : request.getItems()) {
                System.out.println("Processing menu ID: " + item.getMenuId() + " qty: " + item.getQuantity());
                
                if (item.getMenuId() == null) {
                    response.put("success", false);
                    response.put("message", "ID Menu tidak valid");
                    return ResponseEntity.badRequest().body(response);
                }
                
                Optional<Menu> menuOpt = menuRepository.findById(item.getMenuId());
                if (!menuOpt.isPresent()) {
                    System.out.println("Menu not found with ID: " + item.getMenuId());
                    response.put("success", false);
                    response.put("message", "Menu dengan ID " + item.getMenuId() + " tidak ditemukan");
                    return ResponseEntity.badRequest().body(response);
                }
                
                Menu menu = menuOpt.get();
                System.out.println("Found menu: " + menu.getNamaMenu() + " price: " + menu.getHarga() + " stock: " + menu.getStok());
                
                // Cek stok
                if (menu.getStok() < item.getQuantity()) {
                    response.put("success", false);
                    response.put("message", "Stok " + menu.getNamaMenu() + " tidak mencukupi");
                    return ResponseEntity.badRequest().body(response);
                }
                
                Float subtotal = menu.getHarga() * item.getQuantity();
                total += subtotal;
                System.out.println("Subtotal for " + menu.getNamaMenu() + ": " + subtotal);
            }
            
            System.out.println("Total pesanan: " + total);
            
            // Buat pesanan
            Pesanan pesanan = new Pesanan();
            pesanan.setPelanggan(pelanggan);
            pesanan.setTanggalPemesanan(java.sql.Date.valueOf(LocalDate.now()));
            pesanan.setTotal(total);
            pesanan.setStatus("PENDING");
            
            Pesanan savedPesanan = pesananRepository.save(pesanan);
            System.out.println("Pesanan saved with ID: " + savedPesanan.getIdPesanan());
            
            // Buat detail pesanan dan update stok
            for (CartItem item : request.getItems()) {
                Menu menu = menuRepository.findById(item.getMenuId()).get();
                
                // Buat detail pesanan
                DetailPesanan detail = new DetailPesanan();
                detail.setPesanan(savedPesanan);
                detail.setMenu(menu);
                detail.setJumlah(item.getQuantity());
                detail.setSubtotal(menu.getHarga() * item.getQuantity());
                
                detailPesananRepository.save(detail);
                System.out.println("Detail pesanan saved for menu: " + menu.getNamaMenu());
                
                // Update stok
                menu.setStok(menu.getStok() - item.getQuantity());
                menuRepository.save(menu);
                System.out.println("Stock updated for " + menu.getNamaMenu() + " new stock: " + menu.getStok());
            }
            
            response.put("success", true);
            response.put("message", "Pesanan berhasil dibuat");
            response.put("pesananId", savedPesanan.getIdPesanan());
            response.put("total", total);
            
            System.out.println("=== CHECKOUT SUCCESS ===");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.err.println("=== ERROR CHECKOUT ===");
            e.printStackTrace();
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Terjadi kesalahan: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    @GetMapping
    public ResponseEntity<List<Pesanan>> getAllPesanan() {
        List<Pesanan> pesanans = pesananRepository.findAll();
        return ResponseEntity.ok(pesanans);
    }
    
    @GetMapping("/pelanggan/{pelangganId}")
    public ResponseEntity<List<Pesanan>> getPesananByPelanggan(@PathVariable Long pelangganId) {
        List<Pesanan> pesanans = pesananRepository.findByPelanggan_IdPelanggan(pelangganId);
        return ResponseEntity.ok(pesanans);
    }
}