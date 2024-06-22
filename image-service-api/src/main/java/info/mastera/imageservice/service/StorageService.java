package info.mastera.imageservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.UUID;

@Service
public class StorageService {

    private static final Logger logger = LoggerFactory.getLogger(StorageService.class);

    private final S3Client s3Client;
    private final String bucketName;

    public StorageService(S3Client s3Client,
                          @Value("${amazon.s3.bucket}") String bucketName) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
    }

    public String upload(long placeId, MultipartFile file) {
        try {
            if (file == null
                    || file.getOriginalFilename() == null
                    || file.getOriginalFilename().isEmpty()) {
                throw new IllegalArgumentException("File has no name");
            }

            String etag = getETag(file);
            String md5 = getMD5(file);

            String key = getObjectKey(placeId, file.getOriginalFilename());

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            PutObjectResponse response = s3Client.putObject(
                    putObjectRequest,
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            logger.debug("source etag: {}; source md5: {}; target etag: {}; target md5: {}; exp: {}; meta: {}",
                    etag, md5, response.eTag(), response.sseCustomerKeyMD5(), response.expiration(), response.responseMetadata());
            return key;
        } catch (IOException e) {
            logger.error("IO exception: {}", e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            logger.error("Algorithm exception: {}", e.getMessage());
        } catch (AwsServiceException ase) {
            logger.error("AWS service exception: {}", ase.getMessage(), ase);
        } catch (SdkClientException ace) {
            logger.error("Client error: {}", ace.getMessage(), ace);
        }
        return null;
    }

    public String getObjectPublicLink(String objectKey) {
        return s3Client.utilities()
                .getUrl(
                        GetUrlRequest.builder()
                                .bucket(bucketName)
                                .key(objectKey)
                                .build()
                ).toExternalForm();
    }

    /**
     * Method to calculate MD5 in HEX format.
     * Note: getBytes() is very expensive operation as it copies InputStream to byte array
     */
    private String getETag(MultipartFile file) throws NoSuchAlgorithmException, IOException {
        MessageDigest mdigest = MessageDigest.getInstance("MD5");
        byte[] digest = mdigest.digest(file.getBytes());
        return new BigInteger(1, digest).toString(16);
    }

    /**
     * Method to calculate MD5 in text format.
     * Note: getBytes() is very expensive operation as it copies InputStream to byte array
     */
    private String getMD5(MultipartFile file) throws IOException {
        byte[] resultByte = DigestUtils.md5Digest(file.getBytes());
        return new String(Base64.getEncoder().encode(resultByte));
    }

    private String getObjectKey(long placeId, String filename) {
        return placeId + "/"
                + UUID.randomUUID() + "/"
                + filename.substring(0, Math.min(filename.length(), 36));
    }
}
