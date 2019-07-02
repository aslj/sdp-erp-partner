package com.solucionesdigitales.partner.service.storage.impl;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import com.solucionesdigitales.partner.service.storage.StorageService;
import com.solucionesdigitales.partner.service.storage.exceptions.StorageException;
import com.solucionesdigitales.partner.service.storage.exceptions.StorageFileNotFoundException;
import com.solucionesdigitales.partner.service.storage.properties.StorageProperties;

@Service
public class StorageServiceImpl implements StorageService {
	private static final Logger LOGGER = LoggerFactory.getLogger(StorageServiceImpl.class);
	private final Path rootLocation;

	@Autowired
	public StorageServiceImpl(final StorageProperties properties) {
		this.rootLocation = Paths.get(properties.getLocation());
	}

	@Override
	public String saveFile(final MultipartFile file, final String auxDir) {
		final String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		String uuid = "";
		try {
			if (file.isEmpty()) {
				throw new StorageException("Failed to save the empty file: " + fileName);
			}
			if (fileName.contains("..")) {
				// This is a security check
				throw new StorageException("Cannot store file with relative path outside current directory " + fileName);
			}
			final String subDir = (auxDir != null && !auxDir.isEmpty() ? auxDir : "");  
			final Path newRoot = Paths.get(rootLocation.toString(), subDir);
			Files.createDirectories(newRoot.getParent());
			if(!subDir.isEmpty()) {
				if(!Files.exists(newRoot)) {
					Files.createDirectory(newRoot);
				}
			}
			LOGGER.info("~~~ FILE "+file.getOriginalFilename()+" ~~~");
			final int fileNameLength = file.getOriginalFilename().length(); 
			int lastIndex = file.getOriginalFilename().lastIndexOf(".");
			String fileExt = "";
			if(lastIndex > -1) {
				fileExt = file.getOriginalFilename().substring(lastIndex, fileNameLength);
			} else {
				switch (file.getContentType()) {
				case "image/png":
					fileExt = ".png";
					break;
				default:
					fileExt = ".file";
					break;
				}				 
			}
			LOGGER.info("~~~ FILE EXT"+fileExt+" ~~~");
			if(fileExt != null && !fileExt.isEmpty()) {				
				uuid = UUID.randomUUID().toString()+fileExt;				
			} else {
				uuid = UUID.randomUUID().toString()+".file";
			}
			Files.copy(file.getInputStream(), newRoot.resolve(uuid),
					StandardCopyOption.REPLACE_EXISTING);
		}
		catch (final IOException e) {
			throw new StorageException("Failed to save the file " + fileName, e);
		}
		return  uuid;
	}

	@Override
	public Stream<Path> loadAll() {
		try {
			return Files.walk(this.rootLocation, 1)
					.filter(path -> !path.equals(this.rootLocation))
					.map(path -> this.rootLocation.relativize(path));
		}
		catch (final IOException e) {
			throw new StorageException("Failed to read stored files", e);
		}

	}

	@Override
	public Path load(final String fileName) {
		return rootLocation.resolve(fileName);
	}

	@Override
	public Resource loadAsResource(final String fileName) {
		try {
			final Path file = load(fileName);
			final Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			}
			else {
				throw new StorageFileNotFoundException("Failed to load the resource: " + fileName);
			}
		}
		catch (final MalformedURLException e) {
			throw new StorageFileNotFoundException("Failed to load the resource: " + fileName, e);
		}
	}

	@Override
	public Resource loadAsResourceSubDir(final String fileName, final String subDir) {
		try {
			final Path file = Paths.get(rootLocation.toString(), subDir, fileName);
			final Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				throw new StorageFileNotFoundException("Failed to load the resource: " + fileName);
			}
		}
		catch (final MalformedURLException e) {
			throw new StorageFileNotFoundException("Failed to load the resource: " + fileName, e);
		}
	}

	@Override
	public void delete(final String fileName) {
		final Path file = load(fileName);
		FileSystemUtils.deleteRecursively(file.toFile());
	}

	@Override
	public boolean deleteSubDir(final String fileName, final String subDir) {
		boolean res = false;
		final Path path = Paths.get(rootLocation.toString(), subDir);
		final File file = path.resolve(fileName).toFile();
		if(file.exists()) {
			res = file.delete();
		}
		return res;
	}

	@Override
	public String saveFile(final MultipartFile file,final String auxDir, final boolean saveUUID) {
		final String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		String uuid = "";
		try {
			if (file.isEmpty()) {
				throw new StorageException("Failed to save the empty file..." + fileName);
			}
			if (fileName.contains("..")) {
				// This is a security check
				throw new StorageException("Cannot store file with relative path outside current directory " + fileName);
			}
			final String subDir = (auxDir != null && !auxDir.isEmpty() ? auxDir : "");  
			final Path newRoot = Paths.get(rootLocation.toString(), subDir);
			Files.createDirectories(newRoot.getParent());
			if(!subDir.isEmpty()) {
				if(!Files.exists(newRoot)) {
					Files.createDirectory(newRoot);
				}
			}	            
			uuid = UUID.randomUUID().toString()+"."+file.getOriginalFilename().split("\\.")[1];
			if(saveUUID) {
				uuid = UUID.randomUUID().toString()+"."+file.getOriginalFilename().split("\\.")[1];
			}else {
				uuid = fileName;
			}
			Files.copy(file.getInputStream(), newRoot.resolve(uuid), StandardCopyOption.REPLACE_EXISTING);
		} catch(IOException e) {
			throw new StorageException("Failed to save the file " + fileName, e);
		}
		return uuid;
	}

	@Override
	public void init() {
		try {
			if(!Files.exists(rootLocation)) {
				Files.createDirectories(rootLocation);
			}
		} catch(IOException e) {
			throw new StorageException("Failed to start the folder", e);
		}
	}
}
