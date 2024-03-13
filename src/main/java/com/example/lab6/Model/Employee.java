package com.example.lab6.Model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class Employee {
    @NotEmpty(message = "ID should not be empty")
    @Size(min = 3,message = "id should be at least 3 characters")
    private String ID;
    @NotEmpty(message = "name should not be empty")
    @Size(min = 5, message = "name should be at least 5 characters")
    @Pattern(regexp = "^[a-zA-Z]+$")
    private String name;
//    @NotEmpty(message = "email should not be empty")
    @Email(message = "please enter a valid email")
    private String email;
    @NotEmpty(message = "phone number should not be empty")
    @Size(min = 10,max = 10,message = "phone number should be 10 digit")
    @Pattern(regexp = "^(05)[0-9]+$", message = "phone number should start with 05")
    private String phoneNumber;
    @NotNull(message = "age can not be empty")
    @Min(value = 26,message = "we only hire people at age 26 or more")
    private Integer age;
    @NotEmpty(message = "position can't be empty")
    @Pattern(regexp = "^(supervisor|coordinator)$",message = "position should be supervisor or coordinator.")
    private String position;
    @NotNull(message = "on leave should not be null")
    @AssertFalse(message = "on leave should be false")
    private Boolean onLeave;
    @NotNull(message = "hire date should not be empty")
    @PastOrPresent(message = "date can only be and the past or present")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date hireDate;
    @NotNull(message = "annual leave should not be empty")
    @PositiveOrZero(message = "annual leave should be more than 0")
    private Integer annualLeave;

}
