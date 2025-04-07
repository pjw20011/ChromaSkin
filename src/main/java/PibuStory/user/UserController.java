package PibuStory.user;

import PibuStory.board.Board;
import PibuStory.board.BoardService;
import PibuStory.board.CommentService;
import PibuStory.board.ReplyService;
import PibuStory.board.Comment;
import PibuStory.board.Reply;
import PibuStory.mail.EmailService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.http.ResponseEntity;

import java.security.Principal;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class UserController {
    // 사용자 정보를 다루기 위한 서비스
    @Autowired
    private UserService userService;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    // 비밀번호 암호화를 위한 빈
    @Autowired
    private EmailService emailService;
    // 게시글 정보를 다루기 위한 서비스
    @Autowired
    private BoardService boardService;
    // 댓글 정보를 다루기 위한 서비스
    @Autowired
    private CommentService commentService;
    // 대댓글 정보를 다루기 위한 서비스
    @Autowired
    private ReplyService replyService;


//    // 마이페이지 렌더링
    @GetMapping("/my-page")
    public String showMyPage(Model model, HttpSession session) {
        // 세션에서 nickname 값을 가져옴
        String nickname = (String) session.getAttribute("nickname");

        if (nickname == null) {
            // 비로그인 상태일 경우 메인 페이지로 리다이렉트
            return "redirect:/";
        }

        // 로그인한 사용자의 정보를 가져옴
        User user = userService.findByNickname(nickname);
        if (user == null) {
            model.addAttribute("errorMessage", "사용자 정보를 찾을 수 없습니다.");
            return "redirect:/";
        }

        // 사용자 정보를 뷰에 전달
        model.addAttribute("user", user);
        return "my-page"; // my-page 페이지로 이동
    }



    // 마이페이지에서 비밀번호 변경
    @PostMapping("/my-page")
    public String resetPasswordInMyPage(@RequestParam("newPassword") String newPassword,
                                        @RequestParam("oldPassword") String oldPassword,
                                        Principal principal, RedirectAttributes redirectAttributes, HttpSession session) {
        if (principal == null) {
            return "redirect:/";
        }

        User user = userService.findByUsername(principal.getName());
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            redirectAttributes.addFlashAttribute("errorMessage", "현재 비밀번호가 일치하지 않습니다.");
            return "redirect:/my-page";
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userService.saveUser(user);

        // 세션 만료 처리
        session.invalidate();

        // 성공 메시지를 추가하여 index.html로 리다이렉트
        redirectAttributes.addFlashAttribute("successMessage", "비밀번호가 성공적으로 변경되었습니다. 다시 로그인해주세요.");
        return "redirect:/";
    }

    // 마이페이지에서 내가 작성한 게시글 목록 조회
//    @GetMapping("/my-page/my-boards")
//    @ResponseBody
//    public List<Board> getMyBoards(Principal principal) {
//        if (principal == null) {
//            return new ArrayList<>(); // 비로그인 상태일 경우 빈 리스트 반환
//        }
//
//        String nickname = principal.getName();
//        return boardService.getBoardsByWriter(nickname); // 닉네임으로 게시글 조회
//    }

    // 마이페이지에서 내가 작성한 게시글 목록 조회
    @GetMapping("/my-page/my-boards")
    @ResponseBody
    public List<Board> getMyBoards(HttpSession session) {
        // 세션에서 nickname 값을 가져옴
        String nickname = (String) session.getAttribute("nickname");
        if (nickname == null) {
            return new ArrayList<>(); // 비로그인 상태일 경우 빈 리스트 반환
        }

        // 닉네임으로 게시글 조회
        return boardService.getBoardsByWriter(nickname);
    }

    // 마이페이지에서 내가 댓글 단 글 목록 조회 기능
    @GetMapping("/my-page/my-replies")
    @ResponseBody
    public List<Map<String, Object>> getMyReplies(HttpSession session) {
        // 세션에서 nickname 값을 가져옴
        String author = (String) session.getAttribute("nickname");
        if (author == null) {
            return new ArrayList<>(); // 비로그인 상태일 경우 빈 리스트 반환
        }

        // 댓글 및 대댓글 조회
        List<Comment> comments = commentService.getCommentsByAuthor(author);
        List<Reply> replies = replyService.getRepliesByAuthor(author);

        // 댓글과 대댓글 통합
        List<Map<String, Object>> myReplies = new ArrayList<>();
        comments.forEach(comment -> {
            Map<String, Object> item = new HashMap<>();
            item.put("type", "comment");
            item.put("boardId", comment.getBoardId());
            item.put("content", comment.getContent());
            item.put("date", comment.getCreatedDate());

            // 게시글 제목 조회
            Board board = boardService.getBoardById(comment.getBoardId());
            item.put("subject", board != null ? board.getSubject() : "제목 없음");

            myReplies.add(item);
        });

        replies.forEach(reply -> {
            Map<String, Object> item = new HashMap<>();
            item.put("type", "reply");
            item.put("boardId", reply.getBoardId());
            item.put("content", reply.getContent());
            item.put("date", reply.getCreatedDate());

            // 게시글 제목 조회
            Board board = boardService.getBoardById(reply.getBoardId());
            item.put("subject", board != null ? board.getSubject() : "제목 없음");

            myReplies.add(item);
        });

        return myReplies;
    }



    // 로그인 페이지 렌더링
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    // 회원가입 페이지 렌더링
    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    // 홈 페이지 렌더링
    @GetMapping("/")
    public String index(Model model, Principal principal) {
        if (principal != null) {
            String username = principal.getName();
            User user = userService.findByUsername(username);
            model.addAttribute("nickname", user.getNickname());
        }
        return "index";
    }

    // 아이디 중복 체크 API
    @GetMapping("/api/users/check-username")
    @ResponseBody
    public ResponseEntity<Boolean> checkUsername(@RequestParam("username") String username) {
        return ResponseEntity.ok(userService.isUsernameAvailable(username));
    }

    // 닉네임 중복 체크 API
    @GetMapping("/api/users/check-nickname")
    @ResponseBody
    public ResponseEntity<Boolean> checkNickname(@RequestParam("nickname") String nickname) {
        return ResponseEntity.ok(userService.isNicknameAvailable(nickname));
    }

    // 이메일 중복 체크 API
    @GetMapping("/api/users/check-email")
    @ResponseBody
    public ResponseEntity<Boolean> checkEmail(@RequestParam("email") String email) {
        return ResponseEntity.ok(userService.isEmailAvailable(email));
    }

    // 회원가입 처리
    @PostMapping("/api/users/register")
    public String registerUser(@ModelAttribute("user") User user, @RequestParam("confirmPassword") String confirmPassword, RedirectAttributes redirectAttributes) {
        if (!userService.isUsernameAvailable(user.getUsername())) {
            redirectAttributes.addFlashAttribute("errorMessage", "이미 존재하는 아이디입니다.");
            return "redirect:/register";
        }
        if (!userService.isNicknameAvailable(user.getNickname())) {
            redirectAttributes.addFlashAttribute("errorMessage", "이미 존재하는 닉네임입니다.");
            return "redirect:/register";
        }
        if (!userService.isEmailAvailable(user.getEmail())) {
            redirectAttributes.addFlashAttribute("errorMessage", "이미 존재하는 이메일입니다.");
            return "redirect:/register";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.saveUser(user);

        redirectAttributes.addFlashAttribute("registeredEmail", user.getEmail());
        return "redirect:/email-send";
    }
    // 이메일 인증 처리
    @GetMapping("/email-send")
    public String showEmailSendPage(@ModelAttribute("registeredEmail") String registeredEmail, Model model) {
        model.addAttribute("registeredEmail", registeredEmail);
        return "emailSend";
    }

    // 비밀번호 찾기 페이지 렌더링
    @GetMapping("/find-password")
    public String showFindPasswordPage() {
        return "find-password";
    }

    // 비밀번호 찾기 처리
    @PostMapping("/find-password")
    public String resetPassword(@RequestParam("username") String username,
                                @RequestParam("email") String email,
                                RedirectAttributes redirectAttributes) {
        User user = userService.findByUsernameAndEmail(username, email);

        // 사용자 정보가 없을 경우
        if (user == null) {
            redirectAttributes.addFlashAttribute("alertMessage", "입력하신 정보가 일치하지 않습니다. 다시 입력해주세요.");
            return "redirect:/find-password"; // 실패 시 find-password로 리다이렉트
        }

        // 임시 비밀번호 생성 및 저장
        String tempPassword = generateTemporaryPassword();
        user.setPassword(passwordEncoder.encode(tempPassword));
        userService.saveUser(user);

        try {
            // 이메일 전송
            emailService.sendTempPassword(email, tempPassword);
            redirectAttributes.addFlashAttribute("alertMessage", "임시 비밀번호가 발송되었습니다. 확인 후 로그인해주세요.");
            return "redirect:/login"; // 성공 시 login으로 리다이렉트
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("alertMessage", "임시 비밀번호 전송에 실패했습니다. 다시 시도해주세요.");
            return "redirect:/find-password"; // 실패 시 find-password로 리다이렉트
        }
    }

    // 아이디 찾기 페이지 렌더링
    @GetMapping("/find-id")
    public String showFindIdPage() {
        return "find-id";
    }
    // 아이디 찾기 결과 페이지 렌더링
    @GetMapping("/found-id")
    public String showFoundIdPage(@ModelAttribute("foundUsername") String foundUsername, Model model) {
        model.addAttribute("foundUsername", foundUsername);
        return "found-id";
    }

    // 아이디 찾기 요청 처리
    @PostMapping("/find-id")
    public String findIdByEmail(@RequestParam("email") String email,
                                @RequestParam("name") String name,
                                @RequestParam("birthDate") String birthDate,
                                RedirectAttributes redirectAttributes) {
        User user = userService.findByEmailAndNameAndBirthDate(email, name, birthDate);
        if (user == null) {
            redirectAttributes.addFlashAttribute("alertMessage", "입력하신 정보가 올바르지 않습니다. 다시 입력해주세요.");
            return "redirect:/find-id"; // 입력값이 잘못된 경우, 다시 아이디 찾기 페이지로 리다이렉트
        }
        redirectAttributes.addFlashAttribute("foundUsername", user.getUsername());
        return "redirect:/found-id"; // 아이디 찾기 완료 후 결과 페이지로 리다이렉트
    }

    // 임시 비밀번호 생성 메서드
    private String generateTemporaryPassword() {
        String upperCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCaseLetters = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";
        String specialCharacters = "!@#$%^&*()-_+=<>?";

        String combinedChars = upperCaseLetters + lowerCaseLetters + numbers + specialCharacters;
        SecureRandom random = new SecureRandom();
        int passwordLength = 10;

        StringBuilder password = new StringBuilder(passwordLength);
        password.append(upperCaseLetters.charAt(random.nextInt(upperCaseLetters.length())));
        password.append(lowerCaseLetters.charAt(random.nextInt(lowerCaseLetters.length())));
        password.append(numbers.charAt(random.nextInt(numbers.length())));
        password.append(specialCharacters.charAt(random.nextInt(specialCharacters.length())));

        for (int i = 4; i < passwordLength; i++) {
            password.append(combinedChars.charAt(random.nextInt(combinedChars.length())));
        }

        return password.toString();
    }

    // 사용자에게 알림 메시지를 띄우고 리다이렉트하는 메서드
    private String showMessageAndRedirect(final MessageDto params, Model model) {
        model.addAttribute("params", params);
        return "common/messageRedirect";
    }
}