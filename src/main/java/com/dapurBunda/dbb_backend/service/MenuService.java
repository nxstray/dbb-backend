package com.dapurBunda.dbb_backend.service;

import com.dapurBunda.dbb_backend.dto.CreateMenuRequestDTO;
import com.dapurBunda.dbb_backend.dto.UpdateMenuRequestDTO;
import com.dapurBunda.dbb_backend.dto.KategoriWithMenuResponseDTO;
import com.dapurBunda.dbb_backend.dto.MenuItemResponseDTO;
import com.dapurBunda.dbb_backend.dto.MenuResponseDTO;
import com.dapurBunda.dbb_backend.entity.Kategori;
import com.dapurBunda.dbb_backend.entity.Menu;
import com.dapurBunda.dbb_backend.repository.KategoriRepository;
import com.dapurBunda.dbb_backend.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;
    private final KategoriRepository kategoriRepository;

    public List<MenuResponseDTO> getAllMenu() {
        List<Menu> menus = menuRepository.findAll();
        return menus.stream()
                .map(this::convertToMenuResponse)
                .collect(Collectors.toList());
    }

    public List<KategoriWithMenuResponseDTO> getMenuByCategories() {
        List<Kategori> kategoris = kategoriRepository.findAll();
        return kategoris.stream()
                .map(kategori -> {
                    List<Menu> menusInCategory = menuRepository.findByKategori_IdKategori(kategori.getIdKategori().longValue());
                    List<MenuItemResponseDTO> menuItems = menusInCategory.stream()
                            .map(MenuItemResponseDTO::new)
                            .collect(Collectors.toList());
                    
                    return new KategoriWithMenuResponseDTO(kategori.getNamaKategori(), menuItems);
                })
                .collect(Collectors.toList());
    }

    public Optional<MenuResponseDTO> getMenuById(Long id) {
        return menuRepository.findById(id)
                .map(this::convertToMenuResponse);
    }

    public MenuResponseDTO createMenu(CreateMenuRequestDTO request) {
        Optional<Kategori> kategoriOpt = kategoriRepository.findById(request.getIdKategori());
        
        if (!kategoriOpt.isPresent()) {
            throw new RuntimeException("Kategori tidak ditemukan dengan ID: " + request.getIdKategori());
        }

        Kategori kategori = kategoriOpt.get();

        Menu menu = new Menu();
        menu.setNamaMenu(request.getNamaMenu());
        menu.setDeskripsi(request.getDeskripsi());
        menu.setGambar(request.getGambar());
        menu.setHarga(request.getHarga());
        menu.setStok(request.getStok());
        menu.setKategori(kategori);
        menu.setNamaKategori(kategori.getNamaKategori());

        Menu savedMenu = menuRepository.save(menu);
        return convertToMenuResponse(savedMenu);
    }

    public MenuResponseDTO updateMenu(Long id, UpdateMenuRequestDTO request) {
        Optional<Menu> menuOpt = menuRepository.findById(id);
        
        if (!menuOpt.isPresent()) {
            throw new RuntimeException("Menu tidak ditemukan dengan ID: " + id);
        }

        Menu menu = menuOpt.get();

        if (request.getNamaMenu() != null && !request.getNamaMenu().trim().isEmpty()) {
            menu.setNamaMenu(request.getNamaMenu());
        }
        
        if (request.getDeskripsi() != null) {
            menu.setDeskripsi(request.getDeskripsi());
        }
        
        if (request.getGambar() != null) {
            menu.setGambar(request.getGambar());
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
                Kategori kategori = kategoriOpt.get();
                menu.setKategori(kategori);
                menu.setNamaKategori(kategori.getNamaKategori());
            }
        }

        Menu savedMenu = menuRepository.save(menu);
        return convertToMenuResponse(savedMenu);
    }

    public void deleteMenu(Long id) {
        if (!menuRepository.existsById(id)) {
            throw new RuntimeException("Menu tidak ditemukan dengan ID: " + id);
        }
        menuRepository.deleteById(id);
    }

    public void seedSampleData() {
        // Create categories first if not exist
        Kategori makananUtama = findOrCreateKategori("Makanan Utama");
        Kategori appetizer = findOrCreateKategori("Appetizer");
        Kategori minuman = findOrCreateKategori("Minuman");

        // Create sample menus
        createSampleMenuIfNotExists("Nasi Goreng Spesial", 
            "Nasi goreng dengan ayam dan telur", 
            "/src/images/nasgor.jpg", 
            25000f, 50, makananUtama);
            
        createSampleMenuIfNotExists("Ayam Bakar", 
            "Ayam bakar bumbu kecap khas Dapur Bunda", 
            "/src/images/ayam.jpeg", 
            30000f, 30, makananUtama);
            
        createSampleMenuIfNotExists("Spring Roll", 
            "Lumpia goreng renyah isi sayuran", 
            "/src/images/spring.jpg", 
            15000f, 40, appetizer);
            
        createSampleMenuIfNotExists("Salad Segar", 
            "Salad buah dan sayuran segar", 
            "/src/images/salad.avif", 
            20000f, 25, appetizer);
            
        createSampleMenuIfNotExists("Es Teh Manis", 
            "Teh hitam dengan es batu", 
            "/src/images/esteh.jpg", 
            8000f, 100, minuman);
            
        createSampleMenuIfNotExists("Jus Jeruk", 
            "Jus jeruk segar alami", 
            "/src/images/jusjeruk.jpg", 
            12000f, 80, minuman);
    }

    private Kategori findOrCreateKategori(String namaKategori) {
        return kategoriRepository.findByNamaKategori(namaKategori)
                .orElseGet(() -> {
                    Kategori kategori = new Kategori();
                    kategori.setNamaKategori(namaKategori);
                    return kategoriRepository.save(kategori);
                });
    }

    private void createSampleMenuIfNotExists(String namaMenu, String deskripsi, String gambar, Float harga, Integer stok, Kategori kategori) {
        if (menuRepository.findByNamaMenuContainingIgnoreCase(namaMenu).isEmpty()) {
            Menu menu = new Menu();
            menu.setNamaMenu(namaMenu);
            menu.setDeskripsi(deskripsi);
            menu.setGambar(gambar);
            menu.setHarga(harga);
            menu.setStok(stok);
            menu.setKategori(kategori);
            menu.setNamaKategori(kategori.getNamaKategori());
            menuRepository.save(menu);
        }
    }

    private MenuResponseDTO convertToMenuResponse(Menu menu) {
        MenuResponseDTO response = new MenuResponseDTO();
        response.setId(menu.getIdMenu());
        response.setNama(menu.getNamaMenu());
        response.setDeskripsi(menu.getDeskripsi());
        response.setHarga(menu.getHarga());
        response.setGambar(menu.getGambar());
        response.setStok(menu.getStok());
        response.setNamaKategori(menu.getNamaKategori());
        return response;
    }
}