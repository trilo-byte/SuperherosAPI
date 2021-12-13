package com.trilobyte.superheros.security;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.stereotype.Service;

import com.trilobyte.superheros.persistence.entities.AuthUserEntity;
import com.trilobyte.superheros.persistence.repository.AuthUserRepository;

@Service
public class SuperheroUserDetailsService implements UserDetailsService {

  @Autowired private AuthUserRepository userDao;

  @Override
  public UserDetails loadUserByUsername(final String email) {
    final AuthUserEntity userFind =
        userDao
            .findOneByEmail(email)
            .orElseThrow(
                () -> {
                  throw new UsernameNotFoundException("User not found with email : " + email);
                });

    final String username = userFind.getEmail();
    // XXX ignore password in Challenge
    final String pwd = PasswordEncoderFactories.createDelegatingPasswordEncoder().encode("");

    final Set<GrantedAuthority> authorities =
        userFind
            .getRoles()
            .stream()
            .map(r -> new SimpleGrantedAuthority("ROLE_" + r.getRole()))
            .collect(Collectors.toSet());
    return new SuperheroUserPrincipal(username, pwd, authorities);
  }
}
