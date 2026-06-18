package br.com.het.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import br.com.het.config.FileStorageConfig;
import br.com.het.controllers.FileController;
import br.com.het.exception.FileNotFoundException;
import br.com.het.exception.FileStorageException;

@Service
public class FileStorageService {

  private static final Logger logger = LoggerFactory.getLogger(FileStorageService.class);

  private final Path fileStorageLocation;

  public FileStorageService(FileStorageConfig fileStorageConfig) {
    Path path = Paths.get(fileStorageConfig.getUploadDir()).toAbsolutePath().toAbsolutePath().normalize();

    this.fileStorageLocation = path;
    try {
      logger.info("Creating Directories");
      Files.createDirectories(this.fileStorageLocation);
    } catch (Exception e) {
      logger.error("Could not create the directory where the files will be stored");
      throw new FileStorageException("Could not create the directory where the files will be stored", e);
    }
  }

  public String storeFile(MultipartFile file) {
    String fileName = StringUtils.cleanPath(file.getOriginalFilename());

    try {
      if (fileName.contains("..")) {
        logger.error("Sorry File Name Contains an Invalid path Sequence " + fileName);
        throw new FileStorageException("Sorry File Name Contains an Invalid path Sequence " + fileName);
      }

      logger.info("Saving File In Disk");

      Path targetLocation = this.fileStorageLocation.resolve(fileName);
      Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
      return fileName;

    } catch (Exception e) {
      logger.error("Could Not Store File" + fileName + ". Please try again");
      throw new FileStorageException("Could Not Store File" + fileName + ". Please try again");
    }
  }

  public Resource loadFileAssResource(String fileName) {
    try {
      // caminho ate o arquivo
      Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
      Resource resource = new UrlResource(filePath.toUri());
      if (resource.exists()) {
        return resource;
      } else {
        throw new FileNotFoundException("File Not Found" + fileName);
      }
    } catch (Exception e) {
      logger.error("File Not Found" + fileName);
      throw new FileNotFoundException("File Not Found" + fileName, e);
    }
  }
}
