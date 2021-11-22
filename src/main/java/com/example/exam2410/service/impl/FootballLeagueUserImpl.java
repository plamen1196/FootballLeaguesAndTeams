package com.example.exam2410.service.impl;

import java.util.Collection;

import com.example.exam2410.model.entity.UserRole;
import com.example.exam2410.model.entity.enums.UserRoleEnum;
import com.example.exam2410.service.FootballLeagueUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class FootballLeagueUserImpl extends User implements FootballLeagueUser {

    //two constructors for different purposes

    public FootballLeagueUserImpl(String username, String password,
                                  Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public FootballLeagueUserImpl(String username, String password, boolean enabled, boolean accountNonExpired,
                                  boolean credentialsNonExpired, boolean accountNonLocked,
                                  Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked,
                authorities);
    }

    public String getUserIdentifier() {
        return getUsername();
    }

}
