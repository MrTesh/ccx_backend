package com.example.controller;

import com.example.model.FileInfo;
import com.example.response.FileResponseMessage;
import com.example.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/api")
public class FilesController {


    @Autowired
    FileStorageService storageService;

    public String decodeUrl(String value) throws UnsupportedEncodingException {
        return URLDecoder.decode(value, StandardCharsets.UTF_8);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/img")
    ResponseEntity<List<String>> getImageNames(){
        final Path pathUpload = Paths.get("./uploads");
        final List<String> fileNames = new ArrayList<>();

        if (Files.exists(pathUpload) && Files.isDirectory(pathUpload )) {
            try ( DirectoryStream<Path> directoryStream = Files.newDirectoryStream(pathUpload )) {
                for (Path path : directoryStream) {
                    if (Files.isRegularFile(path)) fileNames.add(path.getFileName().toString());
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return new ResponseEntity<>(fileNames, HttpStatus.OK);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/image")
    public ResponseEntity<FileResponseMessage> uploadFile(@RequestParam("file")MultipartFile file){
        String message = "";
        try{
            storageService.save(file);
            message = "Uploaded the file successfully:" + file.getOriginalFilename();
            return ResponseEntity.status(HttpStatus.OK).body(new FileResponseMessage(message));

        }catch (Exception e){
            message = "Could not upload the file:" + file.getOriginalFilename() + ". Error:" + e.getMessage();
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new FileResponseMessage(message));
        }
    }


    @CrossOrigin(origins = "*")
    @GetMapping("/image")
    public ResponseEntity<List<FileInfo>> getListFiles(){
        List<FileInfo> fileInfos = storageService.loadAll().map(path -> {
            String filename = path.getFileName().toString();
            String url = MvcUriComponentsBuilder
                    .fromMethodName(FilesController.class, "getFile", path.getFileName().toString()).build().toString();
            return new FileInfo(filename, url);
        }).toList();

        return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
    }

//TODO узнать про PathVariable, .+ , разобраться в этих 2х get методах
    @CrossOrigin(origins = "*")
    @GetMapping("/image/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getFile(@PathVariable String filename){
        Resource file = storageService.load(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/get-image/{filename}")
    public ResponseEntity<byte[]> getImage(@PathVariable String filename){
        try {
            byte[] resource = Files.readAllBytes(Path.of("uploads", filename));
//                return new ResponseEntity<>(resource, HttpStatus.OK);
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(resource);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();

        }
    }
}
