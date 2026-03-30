package com.example.BE.security.util;

import com.example.BE.entity.JobPortalUser;
import com.example.BE.repository.JobPortalUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class JobPortalUsernamePwdAuthenticationProvider implements AuthenticationProvider {

    private final JobPortalUserRepository jobPortalUserRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String pwd = authentication.getCredentials().toString();
        JobPortalUser jobPortalUser = jobPortalUserRepository.findJobPortalUserByEmail(username)
                .orElseThrow(
                        ()-> new UsernameNotFoundException("User not found: " + username)
                );
        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(jobPortalUser.getRole().getName()));

        if(passwordEncoder.matches(pwd, jobPortalUser.getPasswordHash())){
            return new UsernamePasswordAuthenticationToken(jobPortalUser,null,authorities);
        }else{
            throw new BadCredentialsException("Invalid password!");
        }
    }

    //Nó giúp Spring Security phân loại và điều hướng yêu cầu xác thực đến đúng nơi có khả năng xử lý nó( Xem thử cái authentication ở authController gửi về đây
    //phải UsernamePasswordAuthenticationToken không . Nếu phải thì gửi lên authenticate xử lý
    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
