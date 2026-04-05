package com.example.BE.user.service;

import com.example.BE.dto.UserDto;

import java.util.Optional;

public interface IUserService {

    Optional<UserDto> searchUserByEmail(String email);

    UserDto elevateToEmployer(Long userId);// nang cap nguoi dung len ROLE_EMPLOYER

    UserDto assignCompanyToEmployer(Long userId, Long companyId); // Gan mot cong ty cho nha tuyen dung
}
