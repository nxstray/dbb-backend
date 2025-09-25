package com.dapurBunda.dbb_backend.controller;

import com.dapurBunda.dbb_backend.entity.Pembayaran;
import com.dapurBunda.dbb_backend.entity.Pesanan;
import com.dapurBunda.dbb_backend.repository.PembayaranRepository;
import com.dapurBunda.dbb_backend.repository.PesananRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/pembayaran")
@CrossOrigin(origins = "http://localhost:5173")
public class PembayaranController {

    @Autowired
    private PembayaranRepository pembayaranRepo;

    @Autowired
    private PesananRepository pesananRepo;

    @PostMapping("/pay/{idPesanan}")
    public ResponseEntity<Map<String, Object>> bayar(@PathVariable Long idPesanan, @RequestBody Pembayaran req) {
        Pesanan pesanan = pesananRepo.findById(idPesanan).orElse(null);
        if (pesanan == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Pesanan tidak ditemukan"));
        }

        pesanan.setStatus("SUCCESS");
        pesananRepo.save(pesanan);

        Pembayaran pembayaran = new Pembayaran();
        pembayaran.setPesanan(pesanan);
        pembayaran.setMetodeByr(req.getMetodeByr());
        pembayaran.setJumlahByr(req.getJumlahByr());
        pembayaran.setTanggalBayar(new Date());
        pembayaranRepo.save(pembayaran);

        return ResponseEntity.ok(Map.of(
            "message", "Pembayaran sukses!",
            "idPembayaran", pembayaran.getIdPembayaran(),
            "idPesanan", idPesanan
        ));
    }

    @PostMapping("/generate-qris/{idPesanan}")
    public ResponseEntity<Map<String, Object>> generateQris(@PathVariable Long idPesanan) {
        Optional<Pesanan> pesananOpt = pesananRepo.findById(idPesanan);
        if (pesananOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Pesanan tidak ditemukan"));
        }

        Pesanan pesanan = pesananOpt.get();

        // Simulasi QR String
        String qrString = "QRIS|DBB|" + pesanan.getIdPesanan() + "|" + pesanan.getTotal();

        Map<String, Object> response = new HashMap<>();
        response.put("idPesanan", pesanan.getIdPesanan());
        response.put("total", pesanan.getTotal());
        response.put("qrString", qrString);

        return ResponseEntity.ok(response);
    }
}