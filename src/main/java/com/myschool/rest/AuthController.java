package com.myschool.rest;

import com.myschool.domain.User;
import com.myschool.dto.UserDto;
import com.myschool.repository.UserRepository;
import com.myschool.rest.vm.LoginRelayVM;
import com.myschool.rest.vm.LoginVM;
import com.myschool.security.SecurityUtils;
import com.myschool.security.jwt.TokenProvider;
import com.myschool.service.UserService;
import com.myschool.utils.CustomErrorType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Optional;


@RestController
@CrossOrigin
public class AuthController {

  private final TokenProvider tokenProvider;

  private final AuthenticationManager authenticationManager;

    public AuthController( TokenProvider tokenProvider, AuthenticationManager authenticationManager) {
        this.tokenProvider = tokenProvider;
        this.authenticationManager = authenticationManager;
    }


    @GetMapping("/authenticate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void authenticate() {
        // we don't have to do anything here
        // this is just a secure endpoint and the JWTFilter
        // validates the token
        // this service is called at startup of the app to check
        // if the jwt token is still valid
    }

    /*
    @PostMapping("/login")
    public String authorize(@Valid @RequestBody LoginVM loginUser,
        HttpServletResponse response) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
            loginUser.getUsername(), loginUser.getPassword());

        try {
          this.authenticationManager.authenticate(authenticationToken);
          return this.tokenProvider.createToken(loginUser.getUsername());
        }
        catch (AuthenticationException e) {
          response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
          return null;
        }
    }
    */

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<?> authorize(@Valid @RequestBody LoginVM loginUser,
                                       HttpServletResponse response) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginUser.getUsername(), loginUser.getPassword());

        try {
            this.authenticationManager.authenticate(authenticationToken);
            return new ResponseEntity(this.tokenProvider.createToken(loginUser.getUsername()), HttpStatus.OK);
        }
        catch (BadCredentialsException bce) {
            return new ResponseEntity("Bad credentials", HttpStatus.BAD_REQUEST);
        }
        catch (DisabledException e) {
            System.out.println(e);
            return new ResponseEntity("User is disabled", HttpStatus.BAD_REQUEST);
        }
        catch (AccountExpiredException e) {
            System.out.println(e);
            return new ResponseEntity("User account has expired", HttpStatus.BAD_REQUEST);
        }
        catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(e, HttpStatus.EXPECTATION_FAILED);
        }
        /*catch (AuthenticationException e) {
          System.out.println(e);
          response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
          return null;
        }*/
    }

    @GetMapping("/api/refresh-token")
    public String refreshToken() {
        return this.tokenProvider.createToken(SecurityUtils.getCurrentUserLogin());
    }
}