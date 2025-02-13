package hangman.logismate.entity;

import hangman.logismate.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    private String email;

    private String password;

    private String companyName;

    private String businessRegistrationNumber;

    private String companyContact;

    private String companyAddress;

    // 포워더 회사 소개 이미지 파일 경로
    private String companyImagePath;
}
