/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.samaddico.fileuploader.service;

import com.samaddico.fileuploader.exception.FileStorageException;
import com.samaddico.fileuploader.model.File;
import com.samaddico.fileuploader.repository.FileRepository;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author addico
 */
@Service
public class FileService {

    @Autowired
    private FileRepository fileRepository;

    private final Path rootLocation;

    @Autowired
    public FileService(@Value("${file.storage.location}") String storageLocation) {
        this.rootLocation = Paths.get(storageLocation);
    }

    public void store(MultipartFile file) throws IOException, FileStorageException {

        if (file.isEmpty()) {
            throw new FileStorageException("Failed to store empty file " + file.getOriginalFilename());//check if file is empty
        }
        
        Files.copy(
                file.getInputStream(),
                this.rootLocation.resolve(file.getOriginalFilename()),
                StandardCopyOption.REPLACE_EXISTING
        );

    }

    public File save(File file) {
        return fileRepository.save(file);
    }

    public List<File> getAll() {
        return fileRepository.findAll();
    }

    public void deleteAll() {
        fileRepository.deleteAll();
    }
}
