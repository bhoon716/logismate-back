package hangman.logismate.fileupload;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;

import java.io.IOException;
import java.io.InputStream;

// 파일 업로드
@Component
public class AwsS3Uploader {
    private final S3Client amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    // 생성자
    public AwsS3Uploader(S3Client amazonS3) {
        this.amazonS3 = amazonS3;
    }

    // 파일 업로드 메서드
    public String uploadFile(MultipartFile file) throws IOException {
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();

        // 파일 형식 확인 (JPEG, PNG만 허용)
        if (!fileExtension.equals("jpeg") && !fileExtension.equals("png")) {
            return "이미지 파일(JPEG, PNG)만 업로드 가능합니다.";
        }

        // 파일 Content-Type 확인 (image/jpeg 또는 image/png)
        String contentType = file.getContentType();
        if (contentType == null || (!contentType.equals("image/jpeg") && !contentType.equals("image/png"))) {
            return "유효한 이미지 형식이 아닙니다.";
        }

        // 파일 업로드 처리 (예: S3에 업로드)
        try {
            // S3 업로드 호출
            String uploadedFileUrl = uploadToS3(file, fileName);
            return "파일 업로드 성공: " + uploadedFileUrl;
        } catch (IOException e) {
            return "파일 업로드 실패";
        }
    }

    // S3에 파일을 업로드하는 메서드
    private String uploadToS3(MultipartFile file, String fileName) throws IOException {
        InputStream inputStream = file.getInputStream();

        // S3에 파일 업로드
        amazonS3.putObject(new PutObjectRequest(bucketName, fileName, inputStream, null));

        // 업로드한 파일의 URL 반환
        return amazonS3.getUrl(bucketName, fileName).toString();
    }
}
