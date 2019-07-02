package com.solucionesdigitales.partner.service.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageService {
   public void init();
   public String saveFile(MultipartFile file, String auxDir);
   public String saveFile(MultipartFile file, String auxDir, boolean guardarConUUID);
   public Path load(String fileName);
   public Resource loadAsResource(String fileName);
   public Resource loadAsResourceSubDir(String fileName, String subDir);
   public void delete(String nombreArchivo);
   public boolean deleteSubDir(String fileName, String subDir);
   public Stream<Path> loadAll();
}
