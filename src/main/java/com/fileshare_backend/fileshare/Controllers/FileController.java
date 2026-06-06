package com.fileshare_backend.fileshare.Controllers;

import com.fileshare_backend.fileshare.FileMetaData;
import com.fileshare_backend.fileshare.FileMetadataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/files/")
@CrossOrigin(origins = "http://localhost:5500")
public class FileController {

    @Autowired
    private FileMetadataRepository fileRepository;

    // 🔥 FIXED: Points directly to your storage bucket endpoint with a trailing slash
    @Value("${supabase.url}")
    private String SUPABASE_URL;

    @Value("${supabase.key}")
    private String SUPABASE_KEY;
    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file,
                                        @RequestParam("code") String code) {
        try {
            // 🔥 ADVANCED FIX: Add a unique prefix to prevent files with the same name from overwriting each other
            // Ensure your SUPABASE_KEY variable does NOT contain any accidentally copied spaces or line breaks!
            String uniqueFileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

            java.net.http.HttpClient client = java.net.http.HttpClient.newHttpClient();

// Create the body publisher first to keep the builder clean
            java.net.http.HttpRequest.BodyPublisher bodyPublisher =
                    java.net.http.HttpRequest.BodyPublishers.ofByteArray(file.getBytes());

            java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder()
                    .uri(java.net.URI.create(SUPABASE_URL + uniqueFileName))
                    .header("apikey", SUPABASE_KEY)
                    .header("Authorization", "Bearer " + SUPABASE_KEY.trim()) // Added .trim() to strip hidden spaces
                    .header("Content-Type", file.getContentType())
                    .PUT(bodyPublisher) // 🔥 PUT method applied directly to the body publisher
                    .build();

            java.net.http.HttpResponse<String> response = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());
            // Supabase returns 200 OK for successful uploads/overwrites
            if (response.statusCode() != 200) {
                return ResponseEntity.status(500).body("Failed to push file to Supabase: " + response.body());
            }

            // Save metadata info to Neon PostgreSQL
            FileMetaData metadata = new FileMetaData();
            metadata.setFile_name(uniqueFileName);
            metadata.setCode(code);
            metadata.setUploadedAt(LocalDateTime.now());
            metadata.setFile_size(file.getSize());
            fileRepository.save(metadata);

            return ResponseEntity.ok("File securely deployed to Supabase cloud!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Upload failed: " + e.getMessage());
        }
    }

    @GetMapping("/download/{code}")
    public ResponseEntity<?> downloadFile(@PathVariable("code") String code) {
        Optional<FileMetaData> metaDataOtp = fileRepository.findByCode(code);

        if (metaDataOtp.isEmpty()) {
            return ResponseEntity.status(404).body("Invalid access code.");
        }

        FileMetaData metadata = metaDataOtp.get();

        // 🔥 TRIPLE CHECK: This MUST say /public/ and NOT /authenticated/
        String publicDownloadUrl = "https://zvfhpffhnconmjoglnaz.supabase.co/storage/v1/object/public/fileshare-uploads/" + metadata.getFile_name();

        return ResponseEntity.status(302)
                .header(HttpHeaders.LOCATION, publicDownloadUrl)
                .build();
    }
}