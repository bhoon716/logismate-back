package hangman.logismate.fileupload;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/upload")
public class FileController {

    @Autowired
    private AwsS3Uploader awsS3Uploader;

    @PostMapping
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            // S3에 파일 업로드
            String fileUrl = awsS3Uploader.uploadFile(file);
            return "{\"message\": \"파일 업로드 성공\", \"url\": \"" + fileUrl + "\"}";
        } catch (IOException e) {
            return "{\"message\": \"파일 업로드 실패\", \"error\": \"" + e.getMessage() + "\"}";
        }
    }
}
