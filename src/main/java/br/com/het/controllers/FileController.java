package br.com.het.controllers;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.het.controllers.docs.FileControllerDocs;
import br.com.het.data.dto.UploadFileResponseDTO;
import br.com.het.service.FileStorageService;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/file/v1")
public class FileController implements FileControllerDocs {

  private static final Logger logger = LoggerFactory.getLogger(FileController.class);

  @Autowired
  private FileStorageService service;

  @Override
  @PostMapping("/uploadFile")
  public UploadFileResponseDTO uploadFile(@RequestParam("file") MultipartFile file) {
    var fileName = service.storeFile(file);
    var fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
        .path("/api/file/v1/downloadfile/")
        .path(fileName)
        .toUriString();
    return new UploadFileResponseDTO(fileName, fileDownloadUri, file.getContentType(), file.getSize());
  }

  @Override
  @PostMapping("/uploadMultipleFiles")
  public List<UploadFileResponseDTO> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
    return Arrays.asList(files)
        .stream()
        .map(file -> uploadFile(file)).collect(Collectors.toList());
  }

  @Override
  @GetMapping("/downloadFile/{fileName:.+}")
  public ResponseEntity<Resource> downloadFile(@PathVariable("fileName") String fileName,
      HttpServletRequest request) {
    Resource resource = service.loadFileAssResource(fileName);
    String contentType = null;

    try {
      contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
    } catch (Exception e) {
      logger.error("Code Not Determine Error Type");
    }

    if (contentType == null) {
      contentType = "application/octet-stream";
    }
    return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
        .body(resource);
  }

}
