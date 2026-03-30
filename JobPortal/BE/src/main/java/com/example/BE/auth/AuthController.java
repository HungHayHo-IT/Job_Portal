package com.example.BE.auth;

import com.example.BE.constants.ApplicationConstants;
import com.example.BE.dto.LoginRequestDto;
import com.example.BE.dto.LoginResponeDto;
import com.example.BE.dto.RegisterRequestDto;
import com.example.BE.dto.UserDto;
import com.example.BE.entity.JobPortalUser;
import com.example.BE.entity.Role;
import com.example.BE.repository.JobPortalUserRepository;
import com.example.BE.repository.RoleRepository;
import com.example.BE.security.util.JwtUtil;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final RoleRepository roleRepository;
    private final JobPortalUserRepository jobPortalUserRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login/public")
    public ResponseEntity<LoginResponeDto> apiLogin(@RequestBody LoginRequestDto loginRequestDto){
        try {
            // Bước 1: Trích xuất thông tin người dùng gửi lên từ DTO
            String username = loginRequestDto.username();
            String password = loginRequestDto.password();

            // Bước 2: Đóng gói thông tin vào một Token "chưa xác thực" (Unauthenticated Token)
            // Ở bước này, token chỉ đóng vai trò như một vật chứa dữ liệu thô.
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username,password);

            // Bước 3: Giao Token cho AuthenticationManager để tiến hành kiểm tra (Xác thực)
           // Quản lý sẽ đối chiếu với DB (hoặc InMemory) và trả về một Token "đã xác thực" (Authenticated Token) nếu thành công.
            var resultAuthentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
            // Generate JWT token
            String jwtToken = jwtUtil.generateJwtToken(resultAuthentication);

            var userDto = new UserDto();
            var loggedInUser = (JobPortalUser) resultAuthentication.getPrincipal();
            BeanUtils.copyProperties(loggedInUser,userDto);
            userDto.setRole(loggedInUser.getRole().getName());
            userDto.setUserId(loggedInUser.getId());

            return ResponseEntity.status(HttpStatus.OK)
                    .body(new LoginResponeDto(HttpStatus.OK.getReasonPhrase(),
                            userDto, jwtToken));
        } catch (BadCredentialsException ex) {
            return buildErrorResponse(HttpStatus.UNAUTHORIZED,
                    "Invalid username or password");
        } catch (Exception ex) {
            return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                    "An unexpected error occurred");
        }

    }

    @PostMapping("/register/public")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequestDto registerRequestDto) {

        Optional<JobPortalUser> existingUser = jobPortalUserRepository.readUserByEmailOrMobileNumber
                (registerRequestDto.email(), registerRequestDto.mobileNumber());
        if (existingUser.isPresent()) {
            Map<String, String> errors = new HashMap<>();
            JobPortalUser jobPortalUser = existingUser.get();
            if (jobPortalUser.getEmail().equalsIgnoreCase(registerRequestDto.email())) {
                errors.put("email", "Email is already registered");
            }
            if (jobPortalUser.getMobileNumber().equals(registerRequestDto.mobileNumber())) {
                errors.put("mobileNumber", "Mobile number is already registered");
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }
        JobPortalUser jobPortalUser = new JobPortalUser();
        BeanUtils.copyProperties(registerRequestDto, jobPortalUser);
        jobPortalUser.setPasswordHash(passwordEncoder.encode(registerRequestDto.password()));
        Role role = roleRepository.findRoleByName(ApplicationConstants.ROLE_JOB_SEEKER)
                .orElseThrow(() -> new IllegalArgumentException("Role not found: " +
                        ApplicationConstants.ROLE_JOB_SEEKER));
        jobPortalUser.setRole(role);
        jobPortalUserRepository.save(jobPortalUser);
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
    }





    public ResponseEntity<LoginResponeDto> buildErrorResponse(HttpStatus status , String message){
        return ResponseEntity.status(status)
                .body(new LoginResponeDto(message,null,null));
    }
}
