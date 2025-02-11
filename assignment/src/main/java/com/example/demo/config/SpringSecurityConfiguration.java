package com.example.demo.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SpringSecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests(
                        authorizeRequests -> {
                            authorizeRequests.requestMatchers("/**").permitAll();
                            authorizeRequests.requestMatchers("/h2-console/**").permitAll();
                        })
                .formLogin(login -> login
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/", true)  // 로그인 성공 시 리다이렉트
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
//                .logout(logout -> logout
//                        .logoutUrl("/logout")  // 로그아웃 URL 설정
//                        .logoutSuccessUrl("/login?logout=true")  // 로그아웃 성공 시 리다이렉트할 URL
//                        .invalidateHttpSession(true)  // 세션 무효화
//                        .deleteCookies("JSESSIONID")  // JSESSIONID 쿠키 삭제
//                        .permitAll()
//                )
                .sessionManagement( session ->
                        session.maximumSessions(1) // 사용자당 최대 세션 수 1로 제한
                )
                // h2 콘솔 확인
                .headers(
                        headersConfigurer ->
                                headersConfigurer.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
                ).csrf(csrf -> csrf
                        .ignoringRequestMatchers("/h2-console/**")  // H2 콘솔에 대한 CSRF 보호를 비활성화
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
