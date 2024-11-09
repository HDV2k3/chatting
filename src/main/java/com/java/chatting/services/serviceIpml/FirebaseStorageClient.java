// FirebaseStorageClient.java
package com.java.chatting.services.serviceIpml;
import com.google.cloud.storage.*;
import com.google.firebase.cloud.StorageClient;
import com.java.chatting.constants.BucketConstants;
import com.java.chatting.exception.AppException;
import com.java.chatting.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;


@Slf4j
@Service
public class FirebaseStorageClient {

    private final StorageClient storageClient;

    public FirebaseStorageClient(StorageClient storageClient) {
        this.storageClient = storageClient;
    }

    public String uploadFileToBucket(String bucketName, String folderName, MultipartFile file) {
        try {
            String normalizedFolderName = folderName.endsWith("/") ? folderName.substring(0, folderName.length() - 1) : folderName;
            String generatedFileName = generateUniqueFileName(file.getOriginalFilename());
            String fullObjectName = normalizedFolderName + "/" + generatedFileName;
            Storage storage = storageClient.bucket().getStorage();
            BlobInfo blobInfo = createBlobInfo(bucketName, fullObjectName, file.getContentType());
            storage.create(blobInfo, file.getBytes());
            makeFilePublic(bucketName, fullObjectName);
            return createPublicUrl(bucketName, fullObjectName);
        } catch (IOException e) {
            log.error("Failed to upload file: {}", file.getOriginalFilename(), e);
            throw new AppException(ErrorCode.FILE_UPLOAD_FAILED);
        }
    }

    private String createPublicUrl(String bucketName, String objectName) {
        String encodedObjectName = URLEncoder.encode(objectName, StandardCharsets.UTF_8)
                .replace("+", "%20");
        return String.format(BucketConstants.URL_FIREBASE_API.getValue(), bucketName, encodedObjectName);
    }

    private void makeFilePublic(String bucketName, String objectName) {
        Storage storage = storageClient.bucket().getStorage();
        Blob blob = storage.get(BlobId.of(bucketName, objectName));
        if (blob != null) {
            blob.createAcl(Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER));
        } else {
            log.error("File not found: {}", objectName);
            throw new AppException(ErrorCode.FILE_UPLOAD_FAILED);
        }
    }

    private String generateUniqueFileName(String originalFileName) {
        return UUID.randomUUID() + "_" + originalFileName;
    }

    private BlobInfo createBlobInfo(String bucketName, String objectName, String contentType) {
        return BlobInfo.newBuilder(BlobId.of(bucketName, objectName))
                .setContentType(contentType)
                .build();
    }

}