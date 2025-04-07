package PibuStory.mail;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailDto {
    private String mail;
    private String verifyCode;
}