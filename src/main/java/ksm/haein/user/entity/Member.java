package ksm.haein.user.entity;

import jakarta.persistence.*;
import ksm.haein.user.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nickname;

    @Column(unique = true)
    private String email;

    private String phoneNumber;

    private Role role;

    private LocalDateTime createdAt;
}
