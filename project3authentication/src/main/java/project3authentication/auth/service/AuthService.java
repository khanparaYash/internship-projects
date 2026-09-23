package project3authentication.auth.service;


import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project3authentication.auth.dto.RegisterRequestDto;
import project3authentication.auth.entity.User;
import project3authentication.exception.UserAlreadyExistsException;
import project3authentication.auth.repository.UserRepository;


@Service
public class AuthService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, AuthenticationManager authenticationManager, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void register(RegisterRequestDto request) {

        if (userRepository.existsByEmail(request.email())) {
            throw new UserAlreadyExistsException("Email already registered");
        }
        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        userRepository.save(user);
    }

    public String login(String email, String password) {

//        if (!userRepository.existsByEmail(email)) {
//            throw new UserAlreadyExistsException("Email not found");
//        }
//        Optional<User> user = userRepository.findByEmail(email);
//        if (!passwordEncoder.matches(password, user.get().getPassword())) {
//            throw new UserAlreadyExistsException("Invalid password");
//        }

        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        if (auth.isAuthenticated()) {
            return "Login successful";
        }
        throw new AuthenticationException("Invalid email or password") {
        };

    }
}
