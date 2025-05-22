package com.ekoregin.nms.dto;

import com.ekoregin.nms.database.entity.Role;
import com.ekoregin.nms.validation.UniqueUsername;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Value;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Value
public class UserCreateEditDto {

    @Email
    @UniqueUsername
    String username;

    @NotBlank
    String rawPassword;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    LocalDate birthDate;

    String firstname;

    String lastname;

    Role role = Role.USER;
}
