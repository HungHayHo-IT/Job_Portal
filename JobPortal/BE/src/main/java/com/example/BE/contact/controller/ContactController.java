package com.example.BE.contact.controller;

import com.example.BE.contact.service.IContactService;
import com.example.BE.dto.ContactRequestDto;
import com.example.BE.dto.ContactResponseDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/contacts")
@RequiredArgsConstructor
public class ContactController {

    private final IContactService iContactService;


    @PostMapping(path = "/public")
    public ResponseEntity<String> saveContactMsg(@RequestBody @Valid ContactRequestDto contactRequestDto){
        boolean isSaved = iContactService.saveContact(contactRequestDto);
        if(isSaved){
            return ResponseEntity.status(HttpStatus.CREATED).body("Request processed successfully");
        }else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Request processing failed");
        }
    }

    @GetMapping
    public ResponseEntity<String> fetchOpenContacts(@RequestParam
                                                    @Validated @NotBlank(message = "Status can not be blank")
                                                    @Size(min = 4,message = "Status lenght should be of minimum 4 chars") String status) {
        return ResponseEntity.ok("These are the contacts with the given status: " + status);
    }

    @GetMapping("/admin")
    public ResponseEntity<List<ContactResponseDto>> fetchNewContactMsgs() {
        List<ContactResponseDto> contactResponseDtos = iContactService.fetchNewContactMsgs();
        return ResponseEntity.status(HttpStatus.OK).body(contactResponseDtos);
    }



}
