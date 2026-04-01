package com.example.BE.contact.service;

import com.example.BE.dto.ContactRequestDto;
import com.example.BE.dto.ContactResponseDto;

import java.util.List;

public interface IContactService {

    public boolean saveContact(ContactRequestDto contactRequestDto);

    List<ContactResponseDto> fetchNewContactMsgs();
}
