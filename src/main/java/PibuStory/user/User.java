package PibuStory.user;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
public class User {

    @Id
    private String id;
    private String username;
    private String password;
    private String email;
    private String skinType;
    private String skinLastTestDate;
    private String personalColor;
    private String personalLastTestDate;

    // 이메일 인증 여부 추가 (241111 추가)
    private boolean emailVerified = false;
    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }
    // 여기까지 추가 (241111 추가)


    // 이름, 성별, 전화번호, 생년월일 추가
    private String name;
    private String gender; // "male" or "female"
    private String birthDate; // "YYYY-MM-DD" 형식
    private String nickname;  // 여기서 오타를 수정함

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getBirthDate() { return birthDate; }
    public void setBirthDate(String birthDate) { this.birthDate = birthDate; }

    public String getNickname() { return nickname; }  // 필드와 일치하도록 수정
    public void setNickname(String nickname) { this.nickname = nickname; }  // 필드와 일치하도록 수정

    public String getSkinType() {
        return skinType;
    }

    public void setSkinType(String skinType) {
        this.skinType = skinType;
    }

    public String getSkinLastTestDate() {
        return skinLastTestDate;
    }

    public void setSkinLastTestDate(String skinLastTestDate) {
        this.skinLastTestDate = skinLastTestDate;
    }

    public String getPersonalColor() {
        return personalColor;
    }

    public void setPersonalColor(String personalColor) {
        this.personalColor = personalColor;
    }

    public String getPersonalLastTestDate() {
        return personalLastTestDate;
    }

    public void setPersonalLastTestDate(String personalLastTestDate) {
        this.personalLastTestDate = personalLastTestDate;
    }
}
