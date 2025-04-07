package PibuStory.mail;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "emails") // MongoDB의 컬렉션 이름 지정
public class Email {
    @Id // MongoDB에서의 ID 필드로 사용
    private String id;

    // 이메일 주소
    private String email;

    // 이메일 인증 여부
    private boolean emailStatus;

    @Builder
    public Email(String email) {
        this.email = email;
        this.emailStatus = false;
    }
}