package com.example.BE.service;

import com.example.BE.constants.ApplicationConstants;
import com.example.BE.contact.mapper.ContactMapper;
import com.example.BE.contact.service.impl.ContactServiceImpl;
import com.example.BE.dto.ContactRequestDto;
import com.example.BE.dto.ContactResponseDto;
import com.example.BE.entity.Contact;
import com.example.BE.repository.ContactRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContactServiceImplTest {

    @Mock
    private ContactMapper contactMapper;

    @Mock
    private ContactRepository contactRepository;

    @InjectMocks
    private ContactServiceImpl contactService;

    @Test
    void testSaveContact() {
        // Arrange
        ContactRequestDto requestDto = mock(ContactRequestDto.class);

        Contact contact = new Contact();
        contact.setId(1L);

        when(contactMapper.transformToEntity(requestDto)).thenReturn(contact);
        when(contactRepository.save(contact)).thenReturn(contact);

        // Act
        boolean result = contactService.saveContact(requestDto);

        // Assert
        assertTrue(result);
        verify(contactRepository).save(contact);
    }

    @Test
    void testFetchNewContactMsgs() {
        // Arrange
        Contact contact = new Contact();
        ContactResponseDto responseDto = mock(ContactResponseDto.class);

        when(contactRepository.findContactsByStatus(ApplicationConstants.NEW_MESSAGE))
                .thenReturn(List.of(contact));

        when(contactMapper.transformToDto(contact)).thenReturn(responseDto);

        // Act
        List<ContactResponseDto> result = contactService.fetchNewContactMsgs();

        // Assert
        assertEquals(1, result.size());
        assertEquals(responseDto, result.get(0));
    }

    @Test
    void testFetchNewContactMsgsWithSort() {
        // Arrange
        Contact contact = new Contact();
        ContactResponseDto responseDto = mock(ContactResponseDto.class);

        when(contactRepository.findContactsByStatus(
                eq(ApplicationConstants.NEW_MESSAGE),
                any(Sort.class)
        )).thenReturn(List.of(contact));

        when(contactMapper.transformToDto(contact)).thenReturn(responseDto);

        // Act
        List<ContactResponseDto> result =
                contactService.fetchNewContactMsgsWithSort("createdAt", "desc");

        // Assert
        assertEquals(1, result.size());
        assertEquals(responseDto, result.get(0));
    }

    @Test
    void testFetchNewContactMsgsWithPaginationAndSort() {
        // Arrange
        Contact contact = new Contact();
        ContactResponseDto responseDto = mock(ContactResponseDto.class);

        Page<Contact> contactPage = new PageImpl<>(List.of(contact));

        when(contactRepository.findContactsByStatus(
                eq(ApplicationConstants.NEW_MESSAGE),
                any(Pageable.class)
        )).thenReturn(contactPage);

        when(contactMapper.transformToDto(contact)).thenReturn(responseDto);

        // Act
        Page<ContactResponseDto> result =
                contactService.fetchNewContactMsgsWithPaginationAndSort(
                        0,
                        10,
                        "createdAt",
                        "desc"
                );

        // Assert
        assertEquals(1, result.getContent().size());
        assertEquals(responseDto, result.getContent().get(0));
    }

    @Test
    void testCloseContactMsg() {
        // Arrange
        when(contactRepository.updateStatusById(
                eq("CLOSED"),
                eq(1L),
                anyString(),
                any(Instant.class)
        )).thenReturn(1);

        // Act
        boolean result = contactService.closeContactMsg(1L, "CLOSED");

        // Assert
        assertTrue(result);
    }
}