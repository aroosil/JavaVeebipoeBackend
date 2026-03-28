package ee.andu.server.service;

import ee.andu.server.entity.Person;
import ee.andu.server.entity.PersonRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;

@Service
public class JwtService {

    private final String superSecretKey = "UXWbKk07JM8Da06BAEfpTLtY4ltpafHYLfAWEpVYEWU";
    private final SecretKey secretKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(superSecretKey));

    public String generateToken(Person person) {

        return Jwts
                .builder()
                .signWith(secretKey)
                .id(person.getId().toString())
                .subject(person.getEmail())
                .issuer(person.getRole().toString())
                .compact();
    }

    public Person validateToken(String token){
        //TODO: null check for token
        Claims claims = Jwts
                .parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        Person person = new Person();
        person.setId(Long.parseLong(claims.getId()));
        person.setEmail(claims.getSubject());
        person.setRole(PersonRole.valueOf(claims.getIssuer()));

        return person;
    }
}
