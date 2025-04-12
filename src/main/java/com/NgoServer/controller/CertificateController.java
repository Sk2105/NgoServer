package com.NgoServer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.NgoServer.dto.CertificateBodyDTO;
import com.NgoServer.services.CertificateService;

@RestController
@RequestMapping("/api/certificates")
public class CertificateController {

    @Autowired
    private CertificateService certificateService;

    @PostMapping
    public ResponseEntity<?> createCertificate(@RequestBody CertificateBodyDTO certificateBodyDTO) {
        return ResponseEntity.ok().body(certificateService.createCertificate(certificateBodyDTO));
    }

    @PostMapping("{id}/assign")
    public ResponseEntity<?> assignCertificate(@PathVariable String id, @RequestParam Long volunteerId) {
        return ResponseEntity.ok().body(certificateService.assignCertificate(id, volunteerId));
    }

    @GetMapping("/{id}.png")
    public ResponseEntity<?> downloadCertificate(@PathVariable String id) {
        byte[] imageBytes = certificateService.downloadCertificate(id);

        return ResponseEntity.ok()
        .contentType(MediaType.IMAGE_PNG) // ✅ correct media type
        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"output.png\"")
                .body(imageBytes);
    }

}
