package com.okanciftci.cukatify.controllers;

import com.okanciftci.cukatify.entities.mongo.User;
import com.okanciftci.cukatify.models.LoginRequest;
import com.okanciftci.cukatify.models.LoginSuccessResponse;
import com.okanciftci.cukatify.security.jwt.JwtTokenProvider;
import com.okanciftci.cukatify.services.impl.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


import java.util.List;

import static com.okanciftci.cukatify.security.SecurityConstants.TOKEN_PREFIX;

@RestController
@RequestMapping("/users")
@CrossOrigin
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;




    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest){

        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )

            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = TOKEN_PREFIX + tokenProvider.generateToken(authentication);

            return ResponseEntity.ok(new LoginSuccessResponse(true,jwt));

        }catch (Exception e){
            log.error("Exception " + e.getMessage());
            return ResponseEntity.badRequest().body(false);
        }

    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user){
        try {
            User newUser = userService.saveUser(user);
            return new ResponseEntity<User>(newUser, HttpStatus.CREATED);
        }catch (Exception e){
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public ResponseEntity<?> getUsers(){
        List<User> users = userService.getUsers();
        return new ResponseEntity<List<User>>(users, HttpStatus.OK);
    }

    @RequestMapping(value = "/toggle/{username}",method = RequestMethod.PATCH)
    public ResponseEntity<?> toggleUser(@PathVariable String username){

       try{
           boolean state = userService.toggleUser(username);
           return new ResponseEntity<Boolean>(state,HttpStatus.OK);
       }catch (Exception e){
           log.error(e.getMessage());
           return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
       }
    }



}
