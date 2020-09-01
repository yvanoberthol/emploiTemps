package com.myschool.security.jwt;

import com.myschool.domain.Annee;
import com.myschool.domain.Role;
import com.myschool.domain.User;
import com.myschool.domain.enumerations.TypeEtablissement;
import com.myschool.repository.AnneeRepository;
import com.myschool.repository.UserRepository;
import com.myschool.security.SecurityConstants;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang.ObjectUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class TokenProvider {

    private final String secretKey;

    private final long tokenValidityInMilliseconds;

    private final UserDetailsService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AnneeRepository anneeRepository;


    public TokenProvider(UserDetailsService userService) {
        this.secretKey = Base64.getEncoder().encodeToString(SecurityConstants.SECRET.getBytes());
        this.tokenValidityInMilliseconds = SecurityConstants.EXPIRATION_TIME;
        //this.tokenValidityInMilliseconds = 60000;  //one minute
        this.userService = userService;
    }

    public String createToken(String username) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + this.tokenValidityInMilliseconds);

        User user = userRepository.findByLoginOrEmail(username);
        Annee anneeActive = anneeRepository.findActive();
        Long anneeId = null;
        String annee = null;
        String typeEtablisement = TypeEtablissement.Secondaire.toValue();

        if(anneeActive != null){
            anneeId = anneeActive.getId();
            annee = anneeActive.getDebut() + " - " + anneeActive.getFin();
            typeEtablisement = anneeActive.getTypeEtablissement().toValue();
        }

        return Jwts.builder().setId(UUID.randomUUID().toString()).setSubject(username)
                .setIssuedAt(now).signWith(SignatureAlgorithm.HS512, this.secretKey)
                .setExpiration(validity).claim("id", user.getId())
                .claim("name", user.getName())
                .claim("anneeId", anneeId)
                .claim("annee", annee)
                .claim("typeSchool", typeEtablisement)
                .claim("role", user.getRole().getName()).compact();
    }

    public Authentication getAuthentication(String token) {
        String username = Jwts.parser().setSigningKey(this.secretKey).parseClaimsJws(token)
                .getBody().getSubject();
        UserDetails userDetails = this.userService.loadUserByUsername(username);

        return new UsernamePasswordAuthenticationToken(userDetails, "",
                userDetails.getAuthorities());
    }

}