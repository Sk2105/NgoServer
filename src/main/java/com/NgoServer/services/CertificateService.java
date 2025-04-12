package com.NgoServer.services;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import javax.imageio.ImageIO;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;

import jakarta.servlet.http.HttpServletRequest;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.NgoServer.dto.CertificateBodyDTO;
import com.NgoServer.dto.ResponseDTO;
import com.NgoServer.models.Certificate;
import com.NgoServer.models.Image;
import com.NgoServer.models.Volunteer;
import com.NgoServer.models.VolunteerCertificate;
import com.NgoServer.repo.CertificateRepository;
import com.NgoServer.repo.ImageRepository;
import com.NgoServer.repo.VolunteerCertificateRepository;
import com.NgoServer.repo.VolunteerRepository;

@Service
public class CertificateService {

    @Autowired
    private MailService mailService;

    @Autowired
    private CertificateRepository certificateRepository;

    @Autowired
    private VolunteerRepository volunteerRepository;

    @Autowired
    private VolunteerCertificateRepository volunteerCertificateRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private HttpServletRequest request;

    private String cartificateBody = """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <title>Certificate</title>
                <style>
                    body {
                        text-align: center;
                        font-family: 'Times New Roman', serif;
                        border: 15px double gold;
                        padding: 40px;
                        background-color: #f9f9f9;
                        margin: 0 auto;
                    }
                    h1 {
                        color: #4b0082;
                        font-size: 36px;
                        margin-bottom: 0;
                    }
                    h2 {
                        color: #4b0082;
                        font-size: 28px;
                        margin-top: 5px;
                    }
                    .recipient {
                        font-size: 26px;
                        font-style: italic;
                        color: #2e8b57;
                        margin: 20px 0;
                    }
                    p {
                        font-size: 20px;
                    }
                    .footer {
                        margin-top: 40px;
                        font-size: 16px;
                        color: #555;
                    }
                </style>
            </head>
            <body>
                <h1>[Certificate Name]</h1>
                <h2>Certificate of Achievement</h2>
                <p>This certificate is proudly presented to</p>
                <p class="recipient">[Recipient Name]</p>
                <p>For outstanding performance and dedication.</p>
                <div class="footer">
                    <p>Presented on [Date]</p>
                    <p>Signature: __________________</p>
                </div>
            </body>
            </html>
                        """;

    public String getAppUrl(HttpServletRequest request) {
        return ServletUriComponentsBuilder
                .fromRequestUri(request)
                .replacePath(null)
                .build()
                .toUriString();
    }

    public ResponseDTO createCertificate(CertificateBodyDTO certificateBodyDTO) {
        try {
            Certificate certificate = toCertificate(certificateBodyDTO);
            byte[] imageBytes = generateCertificateImage(certificate);
            String imageName = certificate.getTitle() + ".png";
            Image image = new Image();
            image.setName(imageName);
            image.setData(imageBytes);
            imageRepository.save(image);

            String imageUrl = getAppUrl(request) + "/api/certificates/" + certificate.getId() + ".png";
            certificate.setImageUrl(imageUrl);
            certificateRepository.save(certificate);
            return new ResponseDTO("Certificate created successfully", HttpStatus.OK.value());
        } catch (RuntimeException e) {
            throw new RuntimeException("Error creating certificate: " + e.getMessage());
        }
    }

    private Certificate toCertificate(CertificateBodyDTO certificateBodyDTO) {
        try {
            if (certificateBodyDTO == null) {
                throw new RuntimeException("Certificate body is null");
            }
            Certificate certificate = new Certificate();
            if (certificateBodyDTO.title() == null || certificateBodyDTO.title().isEmpty()) {
                throw new RuntimeException("All fields are required");
            }
            certificate.setTitle(certificateBodyDTO.title());
            certificate.setDescription(certificateBodyDTO.description());
            return certificate;
        } catch (RuntimeException e) {
            throw new RuntimeException("Error creating certificate: " + e.getMessage());
        }

    }

    public ResponseDTO assignCertificate(String certificateId, Long volunteerId) {
        try {
            Certificate certificate = certificateRepository.findById(certificateId)
                    .orElseThrow(() -> new RuntimeException("Certificate not found"));
            if (certificate == null) {
                throw new RuntimeException("Certificate not found");
            }
            Volunteer volunteer = volunteerRepository.findAllVolunteers().stream()
                    .filter(v -> v.getId().equals(volunteerId))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Volunteer not found"));
            if (volunteer == null) {
                throw new RuntimeException("Volunteer not found");
            }

            VolunteerCertificate volunteerCertificate = new VolunteerCertificate();
            volunteerCertificate.setCertificate(certificate);
            volunteerCertificate.setVolunteer(volunteer);
            byte[] imageBytes = generateCertificateImage(certificate, volunteer);
            String imageName = certificate.getTitle() + "_" + volunteer.getUser().getUsername() + "_"
                    + System.currentTimeMillis() + ".png";
            Image image = new Image();
            image.setName(imageName);
            image.setData(imageBytes);
            imageRepository.save(image);
            String imageUrl = getAppUrl(request) + "/api/certificates/" + image.getId() + ".png";
            System.out.println(imageUrl);
            volunteerCertificate.setCertificateImageUrl(imageUrl);
            // Save the image URL in the VolunteerCertificate entity
            volunteerCertificateRepository.save(volunteerCertificate);
            // Send email to the volunteer with the certificate details
            mailService.sendCertificateEmail(volunteer.getUser().getEmail(), imageUrl, certificate, volunteer);
            // Send email to the admin with the certificate details
            return new ResponseDTO("Certificate assigned successfully", HttpStatus.OK.value());
        } catch (RuntimeException e) {
            throw new RuntimeException("Error assigning certificate: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Error assigning certificate: " + e.getMessage());
        }
    }

    public byte[] downloadCertificate(String certificateId) {
        Image image = imageRepository.findById(certificateId)
                .orElseThrow(() -> new RuntimeException("Certificate not found"));
        return image.getData();
    }

    public byte[] generateCertificateImage(Certificate certificate) {
        String html = cartificateBody.replace("[Certificate Name]", certificate.getTitle())
                .replace("[Date]", LocalDateTime.now().toString());
        try (ByteArrayOutputStream pdfOut = new ByteArrayOutputStream()) {

            // Step 1: HTML -> PDF (in-memory)
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            builder.useDefaultPageSize(210, 297, PdfRendererBuilder.PageSizeUnits.MM);
            builder.withHtmlContent(html, null);
            builder.toStream(pdfOut);
            builder.run();

            // Step 2: PDF -> Image
            try (PDDocument doc = PDDocument.load(new ByteArrayInputStream(pdfOut.toByteArray()))) {
                PDFRenderer renderer = new PDFRenderer(doc);
                BufferedImage image = renderer.renderImageWithDPI(0, 300); // page 0, 300 DPI
                ByteArrayOutputStream imageOut = new ByteArrayOutputStream();
                ImageIO.write(image, "png", imageOut);
                return imageOut.toByteArray();
            }

        } catch (Exception e) {
            throw new RuntimeException("HTML to image conversion failed", e);
        }
    }

    public byte[] generateCertificateImage(Certificate certificate, Volunteer volunteer) {
        String html = cartificateBody.replace("[Certificate Name]", certificate.getTitle())
                .replace("[Recipient Name]", volunteer.getUser().getUsername())
                .replace("[Date]", LocalDateTime.now().toString());
        try (ByteArrayOutputStream pdfOut = new ByteArrayOutputStream()) {

            // Step 1: HTML -> PDF (in-memory)
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            builder.useDefaultPageSize(210, 297, PdfRendererBuilder.PageSizeUnits.MM);
            builder.withHtmlContent(html, null);
            builder.toStream(pdfOut);
            builder.run();

            // Step 2: PDF -> Image
            try (PDDocument doc = PDDocument.load(new ByteArrayInputStream(pdfOut.toByteArray()))) {
                PDFRenderer renderer = new PDFRenderer(doc);
                BufferedImage image = renderer.renderImageWithDPI(0, 300); // page 0, 300 DPI
                ByteArrayOutputStream imageOut = new ByteArrayOutputStream();
                ImageIO.write(image, "png", imageOut);
                return imageOut.toByteArray();
            }

        } catch (Exception e) {
            throw new RuntimeException("HTML to image conversion failed", e);
        }
    }

}
