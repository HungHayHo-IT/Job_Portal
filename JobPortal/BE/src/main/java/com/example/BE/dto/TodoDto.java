package com.example.BE.dto;


public record TodoDto(Long userId, Long id, String title, boolean completed) {
}
