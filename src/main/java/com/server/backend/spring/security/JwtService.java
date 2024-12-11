package com.server.backend.spring.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.expiration-time}")
    private long jwtExpiration;

    // Ottiene la chiave di firma dalla decodifica della secret key
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Genera un token JWT per un utente, con tempo di emissione e scadenza
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername()) // Imposta il nome utente
                .setIssuedAt(new Date()) // Imposta il timestamp
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration)) //Scadenza del token
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // Firma il token con la chiave di firma e algoritmo HS256
                .compact();    }

    // Verifica la validità del token confrontando il nome utente e controllando la scadenza
    public boolean isTokenValid(String token, UserDetails userDetails) {
        String username = extractUsername(token); // Estrae il nome utente dal token
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token); // Controlla che il nome utente corrisponda e il token non sia scaduto
    }

    // Estrae il nome utente dal token JWT
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    // Estrae i claims (dati del payload) dal token JWT
    private Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey()) // Utilizza la chiave di firma per la decodifica
                .build()
                .parseClaimsJws(token) // Decodifica il token e ottiene i claims
                .getBody();
    }

    // Controlla se il token è scaduto confrontando la data di scadenza con l'orario corrente
    private boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }
}

