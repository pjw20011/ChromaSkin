package PibuStory.config;

import PibuStory.user.User;
import PibuStory.user.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
public class SecurityConfig {

    private final UserService userService;

    public SecurityConfig(UserService userService) {
        this.userService = userService;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers(
                                "/api/v1/email/send",
                                "/api/v1/email/verify",
                                "/api/v1/user/email/verify", // 메일로 인증 온거 이걸로 인증하는 거임
                                "/board/**",
                                "/comments/**",
                                "/reply/**",
                                "/cosmetics/**",
                                "product/**"
                        )
                )
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers(
                                        "/",
                                        "/products", "/register", "/api/users/register", "/login", "/error",
                                        "/find-id", "/found-id", "/find-password", "/reset-password", "/api/v1/email/send",
                                        "/api/v1/email/verify", "/api/v1/user/email/verify", "/email-send",
                                        "/emailSend", "/api/users/check-username", "/api/users/check-nickname",
                                        "/api/users/check-email", "/css/**", "/js/**", "/image/**",
                                        "/toggle-cosmetics-great"
                                )
                                .permitAll()
                                .anyRequest().authenticated()
                )
                .formLogin(formLogin ->
                        formLogin
                                .loginPage("/login")
                                .loginProcessingUrl("/authenticate")
                                .successHandler(authenticationSuccessHandler())
                                .failureHandler(authenticationFailureHandler())
                                .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .permitAll()
                )
                .userDetailsService(userService);

        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (request, response, authentication) -> {
            // 로그인 성공 시 사용자 정보에서 닉네임 가져오기
            String username = authentication.getName();
            User user = userService.findByUsername(username);

            // 세션에 닉네임 저장
            request.getSession().setAttribute("nickname", user.getNickname());

            // 로그인 성공 후 index로 리디렉트
            response.sendRedirect("/?success=true");
        };
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return (request, response, exception) -> {
            response.sendRedirect("/login?error=true");
        };
    }
}
