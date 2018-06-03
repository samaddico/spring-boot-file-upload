/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.samaddico.fileuploader.controller;

import com.samaddico.fileuploader.exception.FileStorageException;
import com.samaddico.fileuploader.model.File;
import com.samaddico.fileuploader.model.JSONResponse;
import com.samaddico.fileuploader.service.FileService;
import java.io.IOException;
import java.util.List;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author addico
 */
@ControllerAdvice
@RestController
public class FileUploadController {

    @Autowired
    private FileService fileService;

    @PostMapping(value = "/upload")
    public JSONResponse upload(@RequestParam("file") MultipartFile file) throws FileStorageException, IOException {

        String fileName = file.getOriginalFilename();//get file name
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());//get file extension
        JSONResponse response = new JSONResponse();

        

        fileService.store(file);//save file on file system

        //persist file to db here
        File newFile = new File(fileName, extension);
        fileService.save(newFile); // save file in DB

        //prepare reesponse here
        response.setStatus(true);
        response.setMessage("File " +fileName+ " successfully uploaded");
        response.setResult(newFile);

        return response;
    }

    @GetMapping(value = "/all")
    public List<File> listAll() {
        return fileService.getAll();
    }

    @ExceptionHandler(IOException.class)
    @ResponseBody
    public JSONResponse fileUploadError(IOException e) {
        JSONResponse jSONResponse = new JSONResponse();
        jSONResponse.setStatus(false);
        jSONResponse.setMessage("Oops, an error occured during file upload");
        jSONResponse.setResult(e.getMessage());
        return jSONResponse;
    }
    
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public JSONResponse error(Exception e) {
        JSONResponse jSONResponse = new JSONResponse();
        jSONResponse.setStatus(false);
        jSONResponse.setMessage(e.getMessage());
        jSONResponse.setResult(e.getMessage());
        return jSONResponse;
    }

    @ExceptionHandler(FileStorageException.class)
    @ResponseBody
    public JSONResponse fileStorageError(FileStorageException e) {
        JSONResponse jSONResponse = new JSONResponse();
        jSONResponse.setStatus(false);
        jSONResponse.setMessage(e.getMessage());
        jSONResponse.setResult(e.getMessage());
        return jSONResponse;
    }
}
