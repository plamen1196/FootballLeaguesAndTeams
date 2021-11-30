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

    // The purpose of this method is to map our user representation (UserEntity)
    // to the user representation in the spring security world (UserDetails).
    // The only thing that spring will provide to us is the username.
    // The username will come from the HTML login form.

    User user =
        userRepository.findByUsername(username).
            orElseThrow(() -> new UsernameNotFoundException("User with name " + username + " not found!"));

    return mapToUserDetails(user);
  }

  private static UserDetails mapToUserDetails(User user) {

    // GrantedAuthority is the representation of a user role in the
    // spring world. SimpleGrantedAuthority is an implementation of GrantedAuthority
    // which spring provides for our convenience.
    // Our representation of role is UserRoleEntity
    List<GrantedAuthority> authorities =
        user.
            getRoles().
            stream().
            map(r -> new SimpleGrantedAuthority("ROLE_" + r.getRole().name())).
            collect(Collectors.toList());

    // User is the spring implementation of UserDetails interface.
    return new FootballLeagueUserImpl(
        user.getUsername(),
        user.getPassword(),
        authorities
    );
  }
}
