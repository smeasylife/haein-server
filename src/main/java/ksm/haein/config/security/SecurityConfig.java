package ksm.haein.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final PasswordEncoder passwordEncoder;
    private final LogoutSuccessHandler logoutSuccessHandler;
    private final SimpleUrlAuthenticationSuccessHandler authenticationSuccessHandler;

    private static final List<WhiteList> WHITE_LISTS = List.of(
            new WhiteList(HttpMethod.POST, "/auth/kakao/login"),
            new WhiteList(HttpMethod.POST, "/signup/send-code"),
            new WhiteList(HttpMethod.POST, "/signup/verify-code"),
            new WhiteList(HttpMethod.GET, "/swagger-ui/**"),
            new WhiteList(HttpMethod.OPTIONS, "/swagger-ui/**"),
            new WhiteList(HttpMethod.GET, "/v3/api-docs/**"),
            new WhiteList(HttpMethod.GET, "/v3/api-docs")
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
                .formLogin(form -> form
                        .loginPage("http://localhost:3000/login")
                        .loginProcessingUrl("/login")
                        .permitAll()
                        .usernameParameter("email")
                        .successHandler(authenticationSuccessHandler)
                )
                .logout(logout -> logout
                        .invalidateHttpSession(true)
                        .logoutSuccessHandler(logoutSuccessHandler)
                        .deleteCookies("JSESSIONID")
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

