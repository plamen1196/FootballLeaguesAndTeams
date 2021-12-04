package com.example.FootballLeagues.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.example.FootballLeagues.model.entity.User;
import com.example.FootballLeagues.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class FootballLeagueServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public FootballLeagueServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // Username is coming from the Login Form and
        // Then is mapped to UserDetails
        User user =
                userRepository.findByUsername(username).
                        orElseThrow(() -> new UsernameNotFoundException("User with name " + username + " not found!"));

        return mapToUserDetails(user);
    }

    private static UserDetails mapToUserDetails(User user) {

        // GrantedAuthority(SimpleGrantedAuthority) = UserRole (Spring)
        List<GrantedAuthority> authorities =
                user
                        .getRoles()
                        .stream()
                        .map(r -> new SimpleGrantedAuthority("ROLE_" + r.getRole().name()))
                        .collect(Collectors.toList());

        // FootballLeagueUser is extending User(Spring Security)
        return new FootballLeagueUserImpl(
                user.getUsername(),
                user.getPassword(),
                authorities
        );
    }
}
