package com.example.BE.contact.controller;

import com.example.BE.constants.ApplicationConstants;
import com.example.BE.contact.service.IContactService;
import com.example.BE.dto.ContactRequestDto;
import com.example.BE.dto.ContactResponseDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;
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

    @GetMapping("/sort/admin")
    public ResponseEntity<List<ContactResponseDto>> fetchNewContactMsgsWithSort(
            @RequestParam(defaultValue = "createAt") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ){
        List<ContactResponseDto> contactResponseDtos = iContactService.fetchNewContactMsgsWithSort(sortBy,sortDir);
        return ResponseEntity.status(HttpStatus.OK).body(contactResponseDtos);
    }

    @GetMapping("/page/admin")
    public ResponseEntity<Page<ContactResponseDto>> fetchNewContactMsgsWithPaginationAndSort(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "createAt") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ){
        Page<ContactResponseDto> contactResponseDtos = iContactService.fetchNewContactMsgsWithPaginationAndSort(pageNumber,pageSize,sortBy,sortDir);
        return ResponseEntity.status(HttpStatus.OK).body(contactResponseDtos);

    }

    @PatchMapping("/{id}/status/admin")
    public ResponseEntity<String> closeContactMsg(@PathVariable String id) {
        boolean isUpdated = iContactService.closeContactMsg(Long.valueOf(id),
                ApplicationConstants.CLOSED_MESSAGE);
        if (isUpdated) {
            return ResponseEntity.status(HttpStatus.OK).body("Contact message updated successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to update contact message.");
        }
    }



}
