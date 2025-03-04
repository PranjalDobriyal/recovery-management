package com.recovery.managementsystem.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;

@Service
public class FileUploadService {

    private final Path uploadPath;
    private final Path documentsPath;
    private final Path profilePath;

    public FileUploadService(
            @Value("${file.upload-dir}") String uploadDir,
            @Value("${file.documents-dir}") String documentsDir,
            @Value("${file.profile-dir}")String profileDir) {
        this.profilePath=Paths.get(profileDir).toAbsolutePath().normalize();
        this.uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
        this.documentsPath = Paths.get(documentsDir).toAbsolutePath().normalize();
        
        createDirectories();
    }

    private void createDirectories() {
        try {
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
                System.out.println("Created directory: " + uploadPath);
            }

            if (!Files.exists(documentsPath)) {
                Files.createDirectories(documentsPath);
                System.out.println("Created directory: " + documentsPath);
            }
            if (!Files.exists(profilePath)) {
                Files.createDirectories(profilePath);
                System.out.println("Created directory: " + profilePath);
            }
        } catch (IOException e) {
            throw new RuntimeException("❌ Could not create upload directories! Error: " + e.getMessage(), e);
        }
    }

    public String saveFile(MultipartFile file) throws IOException {
        return saveFileToDirectory(file, uploadPath);  
    }
    
    public String saveDocument(MultipartFile file,String oldFileName) throws IOException {
    	 if (file.isEmpty()) {
             throw new IllegalStateException("Cannot upload an empty file.");
         }
    	 if(oldFileName != null && !oldFileName.isEmpty())
    	 {
    	 deleteOldFile(oldFileName);
    	 }
        return saveFileToDirectory(file, documentsPath);
    }
    public String saveProfile(MultipartFile file, String oldFileName) throws IOException {
    	if (file.isEmpty()) {
            throw new IllegalStateException("Cannot upload an empty file.");
        }
   	 if(oldFileName != null && !oldFileName.isEmpty())
   	 {
   	 deleteOldProfile(oldFileName);
   	 }
       return saveFileToDirectory(file, profilePath);
	}

    private void deleteOldProfile(String fileName) {
    	if (fileName != null && !fileName.isEmpty()) {
            Path fileToDelete = profilePath.resolve(fileName).normalize();
            try {
                Files.deleteIfExists(fileToDelete);
                System.out.println(" Deleted old file: " + fileToDelete);
            } catch (IOException e) {
                System.err.println(" Could not delete old file: " + fileToDelete);
            }
        }
		
	}

	public void deleteOldFile(String fileName) {
        if (fileName != null && !fileName.isEmpty()) {
            Path fileToDelete = documentsPath.resolve(fileName).normalize();
            try {
                Files.deleteIfExists(fileToDelete);
                System.out.println("✅ Deleted old file: " + fileToDelete);
            } catch (IOException e) {
                System.err.println("❌ Could not delete old file: " + fileToDelete);
            }
        }
    }
	private String saveFileToDirectory(MultipartFile file, Path directory) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalStateException("Cannot upload an empty file.");
        }

        // Ensure the directory exists before saving the file
        if (!Files.exists(directory)) {
            Files.createDirectories(directory);
        }

        String uniqueFileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path targetLocation = directory.resolve(uniqueFileName);

        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        return uniqueFileName;
    }

	
}
