package com.dapurBunda.dbb_backend.controller;

import com.dapurBunda.dbb_backend.entity.Kategori;
import com.dapurBunda.dbb_backend.entity.Menu;
import com.dapurBunda.dbb_backend.repository.KategoriRepository;
import com.dapurBunda.dbb_backend.repository.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:5173")
public class AdminMenuController {

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private KategoriRepository kategoriRepository;

    // DTO untuk create menu
    public static class CreateMenuRequest {
        private String namaMenu;
        private String namaKategori;
        private Float harga;
        private Integer stok;
        private Integer idKategori;

        // Getters and setters
        public String getNamaMenu() { return namaMenu; }
        public void setNamaMenu(String namaMenu) { this.namaMenu = namaMenu; }

        public String getNamaKategori() { return namaKategori; }
        public void setNamaKategori(String namaKategori) { this.namaKategori = namaKategori; }

        public Float getHarga() { return harga; }
        public void setHarga(Float harga) { this.harga = harga; }

        public Integer getStok() { return stok; }
        public void setStok(Integer stok) { this.stok = stok; }

        public Integer getIdKategori() { return idKategori; }
        public void setIdKategori(Integer idKategori) { this.idKategori = idKategori; }
    }

    // Get all menu (untuk admin)
    @GetMapping("/menu")
    public ResponseEntity<List<Menu>> getAllMenu() {
        List<Menu> menus = menuRepository.findAll();
        return ResponseEntity.ok(menus);
    }

    // Get all kategori
    @GetMapping("/kategori")
    public ResponseEntity<List<Kategori>> getAllKategori() {
        List<Kategori> kategoris = kategoriRepository.findAll();
        return ResponseEntity.ok(kategoris);
    }

    // Create kategori baru
    @PostMapping("/kategori")
    public ResponseEntity<?> createKategori(@RequestBody Map<String, String> request) {
        try {
            String namaKategori = request.get("namaKategori");
            
            if (namaKategori == null || namaKategori.trim().isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Nama kategori tidak boleh kosong");
                return ResponseEntity.badRequest().body(response);
            }

            Kategori kategori = new Kategori();
            kategori.setNamaKategori(namaKategori);
            
            Kategori savedKategori = kategoriRepository.save(kategori);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Kategori berhasil ditambahkan");
            response.put("kategori", savedKategori);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Gagal menambahkan kategori: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // Create menu baru
    @PostMapping("/menu")
    public ResponseEntity<?> createMenu(@RequestBody CreateMenuRequest request) {
        try {
            System.out.println("=== CREATE MENU DEBUG ===");
            System.out.println("Request: " + request);
            System.out.println("Nama Menu: " + request.getNamaMenu());
            System.out.println("ID Kategori: " + request.getIdKategori());
            
            // Validasi input
            if (request.getNamaMenu() == null || request.getNamaMenu().trim().isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Nama menu tidak boleh kosong");
                return ResponseEntity.badRequest().body(response);
            }

            if (request.getIdKategori() == null) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "ID Kategori tidak boleh kosong");
                return ResponseEntity.badRequest().body(response);
            }

            if (request.getHarga() == null || request.getHarga() <= 0) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Harga harus lebih dari 0");
                return ResponseEntity.badRequest().body(response);
            }

            if (request.getStok() == null || request.getStok() < 0) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Stok tidak boleh negatif");
                return ResponseEntity.badRequest().body(response);
            }

            // Cari kategori
            Optional<Kategori> kategoriOpt = kategoriRepository.findById(request.getIdKategori());
            if (!kategoriOpt.isPresent()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Kategori tidak ditemukan");
                return ResponseEntity.badRequest().body(response);
            }

            Kategori kategori = kategoriOpt.get();

            // Buat menu baru
            Menu menu = new Menu();
            menu.setNamaMenu(request.getNamaMenu());
            menu.setNamaKategori(request.getNamaKategori() != null ? request.getNamaKategori() : kategori.getNamaKategori());
            menu.setHarga(request.getHarga());
            menu.setStok(request.getStok());
            menu.setKategori(kategori);

            Menu savedMenu = menuRepository.save(menu);
            
            System.out.println("Menu saved with ID: " + savedMenu.getIdMenu());

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Menu berhasil ditambahkan");
            response.put("menu", savedMenu);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.err.println("Error creating menu: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Gagal menambahkan menu: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // Update menu
    @PutMapping("/menu/{id}")
    public ResponseEntity<?> updateMenu(@PathVariable Long id, @RequestBody CreateMenuRequest request) {
        try {
            Optional<Menu> menuOpt = menuRepository.findById(id);
            if (!menuOpt.isPresent()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Menu tidak ditemukan");
                return ResponseEntity.badRequest().body(response);
            }

            Menu menu = menuOpt.get();
            
            // Update fields if provided
            if (request.getNamaMenu() != null && !request.getNamaMenu().trim().isEmpty()) {
                menu.setNamaMenu(request.getNamaMenu());
            }
            if (request.getHarga() != null && request.getHarga() > 0) {
                menu.setHarga(request.getHarga());
            }
            if (request.getStok() != null && request.getStok() >= 0) {
                menu.setStok(request.getStok());
            }
            if (request.getIdKategori() != null) {
                Optional<Kategori> kategoriOpt = kategoriRepository.findById(request.getIdKategori());
                if (kategoriOpt.isPresent()) {
                    menu.setKategori(kategoriOpt.get());
                    menu.setNamaKategori(kategoriOpt.get().getNamaKategori());
                }
            }

            Menu savedMenu = menuRepository.save(menu);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Menu berhasil diupdate");
            response.put("menu", savedMenu);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Gagal mengupdate menu: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // Delete menu
    @DeleteMapping("/menu/{id}")
    public ResponseEntity<?> deleteMenu(@PathVariable Long id) {
        try {
            Optional<Menu> menuOpt = menuRepository.findById(id);
            if (!menuOpt.isPresent()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Menu tidak ditemukan");
                return ResponseEntity.badRequest().body(response);
            }

            menuRepository.deleteById(id);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Menu berhasil dihapus");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Gagal menghapus menu: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // Insert sample data (untuk development)
    @PostMapping("/seed-data")
    public ResponseEntity<?> seedData() {
        try {
            System.out.println("=== SEEDING DATA ===");
            
            // Create categories first
            Kategori makananUtama = new Kategori();
            makananUtama.setNamaKategori("Makanan Utama");
            makananUtama = kategoriRepository.save(makananUtama);
            
            Kategori appetizer = new Kategori();
            appetizer.setNamaKategori("Appetizer");
            appetizer = kategoriRepository.save(appetizer);
            
            Kategori minuman = new Kategori();
            minuman.setNamaKategori("Minuman");
            minuman = kategoriRepository.save(minuman);
            
            // Create menus
            Menu[] menus = {
                createMenuObject("Nasi Goreng Spesial", 25000f, 50, makananUtama),
                createMenuObject("Ayam Bakar", 30000f, 30, makananUtama),
                createMenuObject("Spring Roll", 15000f, 40, appetizer),
                createMenuObject("Salad Segar", 20000f, 25, appetizer),
                createMenuObject("Es Teh Manis", 8000f, 100, minuman),
                createMenuObject("Jus Jeruk", 12000f, 80, minuman)
            };
            
            for (Menu menu : menus) {
                menuRepository.save(menu);
                System.out.println("Saved: " + menu.getNamaMenu());
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Sample data berhasil ditambahkan");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.err.println("Error seeding data: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Gagal menambahkan sample data: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    private Menu createMenuObject(String nama, Float harga, Integer stok, Kategori kategori) {
        Menu menu = new Menu();
        menu.setNamaMenu(nama);
        menu.setNamaKategori(kategori.getNamaKategori());
        menu.setHarga(harga);
        menu.setStok(stok);
        menu.setKategori(kategori);
        return menu;
    }
}