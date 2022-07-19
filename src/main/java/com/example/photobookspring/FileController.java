package com.example.photobookspring;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;

@RestController
public class FileController {

    private final FileService fileService;
    private final ServletContext servletContext;

    @Autowired
    public FileController (FileService fileService, ServletContext servletContext)
    {
        this.fileService = fileService;
        this.servletContext = servletContext;
    }

//    @PostMapping("/file")
    @RequestMapping(value = "/file",
            method = RequestMethod.POST)
    @CrossOrigin
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) throws Exception{
        String upFile = fileService.storeFile(file);
        System.out.println("File received " + upFile);
        return new ResponseEntity<>(upFile, HttpStatus.OK);
    }


    @RequestMapping(value = "/file",
                    method = RequestMethod.GET)
    @CrossOrigin
    public ResponseEntity<UrlResource> getFile(String fileId) throws Exception {
        UrlResource resource = fileService.loadFile(fileId);
        String contentType = servletContext.getMimeType(resource.getFile().getAbsolutePath());

        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
