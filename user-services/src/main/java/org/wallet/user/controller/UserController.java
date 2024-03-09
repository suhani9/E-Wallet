package org.wallet.user.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wallet.user.dto.UserDto;
import org.wallet.user.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @PostMapping("/register")
    public ResponseEntity<Long> createUserRegistration(@RequestBody @Valid UserDto userDTO){
        long createdId = userService.createUserRegistratiion(userDTO);
       return new ResponseEntity<> (createdId, HttpStatus.CREATED);
    }

}
