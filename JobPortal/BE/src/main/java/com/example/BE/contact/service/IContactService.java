package com.example.BE.contact.service;

import com.example.BE.dto.ContactRequestDto;

public interface IContactService {

    public boolean saveContact(ContactRequestDto contactRequestDto);
}
