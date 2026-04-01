package com.example.BE.dto;

public record LoginResponseDto(String message, UserDto user, String jwtToken) {
}
