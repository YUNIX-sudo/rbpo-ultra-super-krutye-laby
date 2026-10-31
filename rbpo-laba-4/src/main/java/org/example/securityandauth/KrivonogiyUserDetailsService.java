package org.example.securityandauth;

import org.example.model.User;
import org.example.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KrivonogiyUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public KrivonogiyUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("неправильное имя пользователя или пароль"));

        boolean isEnabled = user.getIsActive() != null 
                && user.getIsActive() == 1 
                && !"DEACTIVATED".equalsIgnoreCase(user.getRole());

        String role = user.getRole().startsWith("ROLE_") 
                ? user.getRole() 
                : "ROLE_" + user.getRole();

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .disabled(!isEnabled)
                .authorities(List.of(new SimpleGrantedAuthority(role)))
                .build();
    }
}
