package PibuStory.mail;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final RedisUtil redisUtil;

    // 회원가입 시 이메일 인증 코드 전송
    public void sendEmail(String toEmail) {
        String verificationCode = generateVerificationCode();
        redisUtil.setDataExpire(toEmail, verificationCode, 300); // 5분 동안 유효

        try {
            var message = mailSender.createMimeMessage();
            var helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(toEmail);
            helper.setSubject("ChromaSkin 이메일 인증");

            String msgg = "";
            msgg += "<div style='margin:100px;'>";
            msgg += "<h1> ChromaSkin </h1>";
            msgg += "<h1 style='color:blue;'>인증번호</h1> <h1> 안내 메일입니다.</h1>";
            msgg += "<br>";
            msgg += "<p>ChromaSkin에 오신 것을 환영합니다!<p>";
            msgg += "<br>";
            msgg += "<p>해당 이메일은 회원가입을 위한 인증번호 안내 메일입니다.<p>";
            msgg += "<br>";
            msgg += "<p>하단 인증번호를 '이메일 인증번호' 칸에 입력하여 가입을 완료해주세요.<p>";
            msgg += "<br>";
            msgg += "<div align='center' style='border:1px solid black; font-family:verdana';>";
            msgg += "<h3 style='color:blue;'>회원가입 인증 코드입니다.</h3>";
            msgg += "<div style='font-size:130%'>";
            msgg += "CODE : <strong>";
            msgg += verificationCode + "</strong><div><br/> ";
            msgg += "</div>";

            helper.setText(msgg, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
            // 로깅 또는 알림 등의 추가 예외 처리 코드 작성 가능
        }
    }

    // 비밀번호 찾기 시 임시 비밀번호 전송
    public void sendTempPassword(String toEmail, String tempPassword) {
        try {
            var message = mailSender.createMimeMessage();
            var helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(toEmail);
            helper.setSubject("ChromaSkin 임시 비밀번호 발급 안내");

            String msgg = "";
            msgg += "<div style='margin:100px;'>";
            msgg += "<h1> ChromaSkin </h1>";
            msgg += "<h2 style='color:blue;'>임시 비밀번호 안내 메일입니다.</h2>";
            msgg += "<br>";
            msgg += "<p>ChromaSkin에 오신 것을 환영합니다!<p>";
            msgg += "<br>";
            msgg += "<p>요청하신 임시 비밀번호는 아래와 같습니다.<p>";
            msgg += "<br>";
            msgg += "<div align='center' style='border:1px solid black; font-family:verdana';>";
            msgg += "<h3 style='color:blue;'>임시 비밀번호</h3>";
            msgg += "<div style='font-size:130%'>";
            msgg += "PASSWORD : <strong>";
            msgg += tempPassword + "</strong><div><br/> ";
            msgg += "</div>";
            msgg += "<p>로그인 후 비밀번호를 꼭 변경해주세요.<p>";

            helper.setText(msgg, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
            // 로깅 또는 알림 등의 추가 예외 처리 코드 작성 가능
        }
    }

    // 이메일 인증 코드 검증
    public boolean verifyEmailCode(String email, String code) {
        String storedCode = redisUtil.getData(email);
        return code.equals(storedCode);
    }

    // 인증 코드 생성 메서드
    private String generateVerificationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }
}
