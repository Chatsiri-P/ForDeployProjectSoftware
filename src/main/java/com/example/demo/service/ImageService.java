package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.*;
import com.example.demo.repository.ImageRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class ImageService {

	private final ImageRepository imageRepository;

	@Value("${app.upload.profile-dir}")
	private String profileUploadDir;

	@Value("${app.upload.event-dir}")
	private String eventUploadDir;

	@Autowired
	public ImageService(ImageRepository imageRepository) {
		this.imageRepository = imageRepository;
	}

	public Image saveProfileImage(MultipartFile file, User user) {
		String filename = generateFileName(file.getOriginalFilename());
		String filePath = profileUploadDir + filename;

		saveFileToSystem(file, filePath);

		Image image = new Image(filePath, Image.ImageType.PROFILE);
		image.setUser(user);

		return imageRepository.save(image);
	}

	public Image saveEventImage(MultipartFile file, Event event) {
		String filename = generateFileName(file.getOriginalFilename());
		String filePath = eventUploadDir + filename;

		saveFileToSystem(file, filePath);

		Image image = new Image(filePath, Image.ImageType.EVENT);
		image.setEvent(event);

		return imageRepository.save(image);
	}

	private String generateFileName(String originalFilename) {
		String extension = "";
		if (originalFilename != null && originalFilename.contains(".")) {
			extension = originalFilename.substring(originalFilename.lastIndexOf("."));
		}
		return UUID.randomUUID().toString() + extension;
	}

	private void saveFileToSystem(MultipartFile file, String filePath) {
		try {
			Path path = Paths.get(filePath);
			Files.createDirectories(path.getParent());
			Files.write(path, file.getBytes());
		} catch (IOException e) {
			throw new RuntimeException("Failed to save file: " + e.getMessage());
		}
	}

	public List<Image> getEventImages(Event event) {
		return imageRepository.findByEvent(event);
	}

	public List<Image> getUserImages(User user) {
		return imageRepository.findByUser(user);
	}
}
