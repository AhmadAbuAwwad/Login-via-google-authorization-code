package com.spring.oauth2.social.demo.services;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.spring.oauth2.social.demo.models.User;
import com.spring.oauth2.social.demo.repository.UserRepository;
import com.spring.oauth2.social.security.SecurityUtil;
import com.spring.oauth2.social.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoginService {

    @Value("${google-client-id}")
    private String googleClientId;

    @Value("${google-client-secret}")
    private String googleClientSecret;

    @Autowired
    UserRepository userRepository;

    @Autowired
    SecurityUtil securityUtil;

    @Autowired
    JwtUtils jwtUtils;


    public String loginViaGoogle(String authorizationCode) throws IOException {

        //  get Access token
        GoogleTokenResponse googleAuthCode = null;
        try {
            googleAuthCode = new GoogleAuthorizationCodeTokenRequest(new NetHttpTransport(),
                    JacksonFactory.getDefaultInstance(), "https://oauth2.googleapis.com/token", googleClientId, googleClientSecret,
                    authorizationCode, "http://localhost:9191/login/oauth2/code/google").execute();
        } catch (HttpClientErrorException.BadRequest e) {
        }

        //  Parse token
        GoogleIdToken idToken = googleAuthCode.parseIdToken();
        GoogleIdToken.Payload payload = idToken.getPayload();

        User user = userRepository.findUserByGoogleId(payload.getSubject());

        //  Create One
        if (user == null) {
            user = new User();
        }
        user.setEmail(payload.getEmail());
        user.setGoogleId(payload.getSubject());
        userRepository.saveAndFlush(user);

        String jwt = jwtUtils.generateJwtToken(payload.getEmail());

        return jwt;
    }
}
