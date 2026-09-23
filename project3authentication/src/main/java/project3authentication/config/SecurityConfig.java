package project3authentication.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    public UserDetailsService UserDetailsService;

    public SecurityConfig(UserDetailsService UserDetailsService) {
        this.UserDetailsService = UserDetailsService;
    }

    @Bean // it is used to get the SecurityFilterChain which is used to configure the security of the application
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

       return http
               .csrf(AbstractHttpConfigurer::disable)
               .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()
                       .requestMatchers("/api/**").permitAll()
                       .requestMatchers("/swagger-ui/**").permitAll()
                        .anyRequest().permitAll())
               .httpBasic(httpBasic -> {})
               .build();

    }

    @Bean // it is used to get the AuthenticationManager from the AuthenticationConfiguration we use this at login time
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean // it is used to get the AuthenticationProvider
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(UserDetailsService);
        provider.setPasswordEncoder(bCryptPasswordEncoder());
        return provider;
    }
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
