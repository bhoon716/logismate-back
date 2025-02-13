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

    private UserRole userRole;

    private String email;

    private String password;

    private String companyName;

    private String RegisterBusinessNumber;

    private String companyContact;

    private String companyAddress;

    private String companyImage;
}
