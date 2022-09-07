package com.cloud.google.utility;

import java.net.URL;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nullable;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.BucketInfo;
import com.google.cloud.storage.Storage;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import reactor.core.publisher.Mono;

@Component
public class FileStorage {

	@Getter(AccessLevel.PROTECTED)
	@Setter(AccessLevel.PROTECTED)
	@Autowired
	private Storage storage;
	private Bucket bucket;
	private final String bucketName = "hello-buk-storage";
	private final String subdirectory = "images";
	
	private final String bucketAddress="https://storage.cloud.google.com/hello-buk-storage/images/";
	
	private void createBucket(String bucketName) {
		bucket = storage.get(bucketName.toLowerCase());
		if (bucket == null) {
			bucket = storage.create(BucketInfo.of(bucketName));
		}

	}

	private BlobId constructBlobId(String bucketName, @Nullable String subdirectory, String fileName) {
		return Optional.ofNullable(subdirectory).map(s -> BlobId.of(bucketName, subdirectory + "/" + fileName))
				.orElse(BlobId.of(bucketName, fileName));
	}

	private URL createSignedPathStyleUrl(BlobInfo blobInfo, int duration, TimeUnit timeUnit) {
		return getStorage().signUrl(blobInfo, duration, timeUnit, Storage.SignUrlOption.withPathStyle());
	}

	public String deleteFile(String fileName) {
		BlobId blobId = constructBlobId(bucketName, subdirectory, fileName.replace(bucketAddress, ""));
		if (storage.delete(blobId)) {
			return "File is Successfully deleted";
		}

		return "File not found";
	}

	public Mono<URL> uploadFiles(MultipartFile filePart, String imageType) {
		createBucket(bucketName);

		byte[] byteArray = convertToByteArray(filePart);
		String fileName = filePart.getOriginalFilename();
		BlobId blobId = constructBlobId(bucketName, subdirectory, fileName);
		return Mono.just(blobId).map(bId -> BlobInfo.newBuilder(blobId).setContentType(imageType).build())
				.doOnNext(blobInfo -> getStorage().create(blobInfo, byteArray))
				.map(blobInfo -> createSignedPathStyleUrl(blobInfo, 10, TimeUnit.DAYS));
	}

	@SneakyThrows
	private byte[] convertToByteArray(MultipartFile filePart) {

		return filePart.getBytes();

	}

	public String checkFileType(String fileName) {
		String extension = FilenameUtils.getExtension(fileName);
		String fType = "";
		switch (extension) {
		case "gif":
			fType = "image/gif";
			break;
		case "png":
			fType = "image/png";
			break;
		case "jpeg":
			fType = "image/jpeg";
			break;
		case "jpg":
			fType = "image/jpg";
			break;
		case "zip":
			fType = "application/zip";
			break;
		case "rar":
			fType = "application/rar";
			break;
		default:
			fType = "";
		}
		return fType;
	}

	public String getBucketAddress() {
		return bucketAddress;
	}
}
