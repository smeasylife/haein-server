package ksm.haein.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final PasswordEncoder passwordEncoder;

    private static final List<WhiteList> WHITE_LISTS = List.of(
            new WhiteList(HttpMethod.POST, "/auth/kakao/login")
    );

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.httpBasic(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(requests -> {
                    WHITE_LISTS.forEach(whiteList ->
                            requests.requestMatchers(whiteList.method(), whiteList.url()).permitAll());
                    requests.anyRequest().authenticated();
                    }
                )
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                )
                .cors(cors -> cors
                        .configurationSource(CorsConfig.corsConfigurationSource())
                )
                .sessionManagement(session -> session
                        .sessionFixation(f -> f.migrateSession())
                        .maximumSessions(1).maxSessionsPreventsLogin(true)
                );

        return http.build();
    }
}
