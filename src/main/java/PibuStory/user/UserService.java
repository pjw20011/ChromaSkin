package PibuStory.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import PibuStory.mail.EmailService;

import java.util.ArrayList;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final EmailService emailService; // 이메일 전송 서비스

    @Autowired
    public UserService(UserRepository userRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    // 로그인 기능: Spring Security에 필요한 UserDetails 객체 반환
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        // 이메일 인증 여부 체크(241113 기준 로그인 오류 나서 미사용 - 추후 기능 보완 필요시 사용(코드 최적화 안돼서 주석처리함))
        // if (!user.isEmailVerified()) {
        //     throw new UsernameNotFoundException("Email not verified for username: " + username);
        // }

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>());
    }

    // 회원가입 시 username으로 사용자 찾기
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // 이메일, 이름, 생년월일로 사용자 찾기
    public User findByEmailAndNameAndBirthDate(String email, String name, String birthDate) {
        return userRepository.findByEmailAndNameAndBirthDate(email, name, birthDate);
    }

    // 닉네임으로 사용자 찾기
    public User findByNickname(String nickname) {
        return userRepository.findByNickname(nickname);
    }

    // 아이디와 이메일로 사용자 찾기 (비밀번호 찾기 기능용)
    public User findByUsernameAndEmail(String username, String email) {
        return userRepository.findByUsernameAndEmail(username, email);
    }

    // 비밀번호 찾기: 아이디와 이메일 확인 후 임시 비밀번호 발급 및 이메일 전송
    public boolean findPassword(String username, String email) {
        User user = userRepository.findByUsernameAndEmail(username, email);

        if (user != null) {
            String tempPassword = generateTempPassword();
            user.setPassword(tempPassword); // 임시 비밀번호 설정 (암호화 필요 시 추가)
            userRepository.save(user); // 업데이트

            // 이메일 전송
            emailService.sendTempPassword(email, tempPassword);
            return true;
        }

        return false;
    }

    // 임시 비밀번호 생성
    private String generateTempPassword() {
        return UUID.randomUUID().toString().substring(0, 8); // 8자리 임시 비밀번호 생성
    }

    // 중복된 username, nickname, email 체크 메서드
    public boolean isUsernameAvailable(String username) {
        return !userRepository.existsByUsername(username);
    }

    public boolean isNicknameAvailable(String nickname) {
        return !userRepository.existsByNickname(nickname);
    }

    public boolean isEmailAvailable(String email) {
        return !userRepository.existsByEmail(email);
    }

    // 회원가입 시 사용자 저장 (중복된 사용자명 방지 로직 추가)
    public User saveUser(User user) {
        // 기존 사용자일 경우에는 사용자명 중복을 체크하지 않고 업데이트
        if (user.getId() != null) {
            return userRepository.save(user);
        }

        // 새로운 사용자일 경우에만 사용자명 중복을 확인
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new IllegalArgumentException("Username already exists");
        }
        return userRepository.save(user);
    }
}
