package org.example.authservice.service;



import com.example.dto.LoginResponseDto;
import com.example.dto.RegisterRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.example.authservice.entity.User;
import org.example.authservice.exception.UserAlreadyExistsException;
import org.example.authservice.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class AuthService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, AuthenticationManager authenticationManager,
                       BCryptPasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional
    public void register(RegisterRequestDto request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("Email already registered");
        }
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
    }

    public LoginResponseDto login(String email, String password) {
        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        if (auth.isAuthenticated()) {
            User user = userRepository.findByEmail(email);
            LoginResponseDto loginResponseDto=new LoginResponseDto();
             loginResponseDto.setTokenType("Bearer");
             loginResponseDto.setToken(jwtService.generateToken(user));
             loginResponseDto.setExpiresIn(jwtService.getExpirationMs());
             return loginResponseDto;
        }
        throw new AuthenticationException("Invalid email or password") {
        };

    }
}
