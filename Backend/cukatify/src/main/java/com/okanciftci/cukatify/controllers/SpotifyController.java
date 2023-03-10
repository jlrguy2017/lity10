package com.okanciftci.cukatify.controllers;

import com.okanciftci.cukatify.common.enums.RoleNames;
import com.okanciftci.cukatify.entities.mongo.Role;
import com.okanciftci.cukatify.security.jwt.JwtTokenProvider;
import com.okanciftci.cukatify.services.abstr.RoleService;
import com.okanciftci.cukatify.services.impl.UserService;
import com.okanciftci.cukatify.spotify.abstr.SpotifyAPIService;
import com.okanciftci.cukatify.spotify.SpotifyLoginHelper;
import com.wrapper.spotify.SpotifyHttpManager;
import com.wrapper.spotify.model_objects.specification.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.net.URI;

import static com.okanciftci.cukatify.security.SecurityConstants.TOKEN_PREFIX;

@RestController
@RequestMapping("/spotify")
@CrossOrigin
@Slf4j
public class SpotifyController {

    @Autowired
    private SpotifyLoginHelper spotifyLoginHelper;

    @Autowired
    private SpotifyAPIService spotifyAPIService;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;


    @GetMapping("/loginURI")
    public ResponseEntity<?> loginWithSpotifyUser(){

        URI uri = spotifyLoginHelper.getSpotifyURI();

        return new ResponseEntity<>(uri, HttpStatus.OK);

    }

    @GetMapping("/callback")
    public ResponseEntity<?> accessSpotifyPageWithCallback(@RequestParam("code") String code) {

        String token = spotifyLoginHelper.getSpotifyAccessToken(code);

        User spotifyUser = spotifyAPIService.getUserCredentials(token);

        com.okanciftci.cukatify.entities.mongo.User systemUser = userService.getUser(spotifyUser.getEmail());

        if(systemUser == null){

                com.okanciftci.cukatify.entities.mongo.User newSystemUser = new com.okanciftci.cukatify.entities.mongo.User();

                newSystemUser.setUsername(spotifyUser.getEmail());

                newSystemUser.setSpotifyUser(true);

                newSystemUser.setImageUrl(spotifyUser.getImages()[0].getUrl());

                newSystemUser.setAccessToken(token);

                newSystemUser.setFullName(spotifyUser.getDisplayName());

                newSystemUser.setPassword(SpotifyLoginHelper.staticPass);

                newSystemUser.setSpotifyId(spotifyUser.getId());

                Role role = roleService.getRoleByName(RoleNames.SPOTIFY_USER.name());

                newSystemUser.addRole(role);

                role = roleService.getRoleByName(RoleNames.USER.name());

                newSystemUser.addRole(role);

                userService.saveSpotifyUser(newSystemUser);

                try {
                    Authentication authentication = authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    newSystemUser.getUsername(),
                                    SpotifyLoginHelper.staticPass
                            )
                    );

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    String jwt = TOKEN_PREFIX + tokenProvider.generateToken(authentication,token,spotifyUser.getDisplayName());

                    HttpHeaders headers = new HttpHeaders();
                    headers.add("Location", "http://localhost:3000/spotify#"+jwt);
                    return new ResponseEntity(headers, HttpStatus.FOUND);
                }catch (Exception e){
                    return new ResponseEntity(e.getMessage(), HttpStatus.FOUND);
                }

        }else {
            boolean isValid = userService.checkActive(systemUser.getUsername());
            if (isValid) {
                systemUser.setAccessToken(token);
                try {
                    Authentication authentication = authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    systemUser.getUsername(),
                                    SpotifyLoginHelper.staticPass
                            )
                    );

                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    String jwt = TOKEN_PREFIX + tokenProvider.generateToken(authentication, token, systemUser.getFullName());

                    HttpHeaders headers = new HttpHeaders();
                    headers.add("Location", "http://localhost:3000/spotify#" + jwt);
                    return new ResponseEntity(headers, HttpStatus.FOUND);
                } catch (Exception e) {
                    return new ResponseEntity(e.getMessage(), HttpStatus.FOUND);
                }
            }else{
                return new ResponseEntity(false, HttpStatus.BAD_REQUEST);
            }
        }
    }

}
