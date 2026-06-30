package br.com.het.security.jwt;

import java.util.Base64;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import br.com.het.data.dto.security.TokenDTO;
import br.com.het.exception.InvalidJwtAuthenticationException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class JwtTokenProvider {

  @Value("${security.jwt.token.secret-key:secret}")
  private String secretKey = "secret";
  @Value("${security.jwt.token.expire-length:3600000}")
  private long validityInMilliseconds = 3600000; // 1h

  @Autowired
  private UserDetailsService userDetailsService;

  Algorithm algorithm = null;

  @PostConstruct
  protected void init() {
    secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    algorithm = algorithm.HMAC256(secretKey.getBytes());
  }

  // metodo que cria o token e retorna um objeto TokenDTO
  public TokenDTO createAccessToken(String userName, List<String> roles) {
    Date now = new Date();
    Date validity = new Date(now.getTime() + validityInMilliseconds);

    // @
    String accesToken = getAccessToken(userName, roles, now, validity);

    // #
    String refreshToken = getRefreshToken(userName, roles, now);

    return new TokenDTO(userName, true, now, validity, accesToken, refreshToken);
  }

  // # criacao do token de refresh
  private String getRefreshToken(String userName, List<String> roles, Date now) {
    Date refreshTokenValidity = new Date(now.getTime() + (validityInMilliseconds * 3));
    return JWT.create()
        .withClaim("roles", roles)
        .withIssuedAt(now)
        .withExpiresAt(refreshTokenValidity)
        .withSubject(userName)
        .sign(algorithm);
  }

  // @ criacao do token de acesso
  private String getAccessToken(String userName, List<String> roles, Date now, Date validity) {
    String issuerUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
    return JWT.create()
        .withClaim("roles", roles)
        .withIssuedAt(now)
        .withExpiresAt(validity)
        .withSubject(userName)
        .withIssuer(issuerUrl)
        .sign(algorithm);
  }

  // metodo ultilizado por JwtTokenFilter
  public Authentication getAuthentication(String token) {
    DecodedJWT decodedJwt = decodedToken(token);
    UserDetails userDetails = this.userDetailsService
        .loadUserByUsername(decodedJwt.getSubject());
    return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
  }

  // $
  private DecodedJWT decodedToken(String token) {
    Algorithm alg = Algorithm.HMAC256(secretKey.getBytes());
    JWTVerifier verifier = JWT.require(alg).build();
    DecodedJWT decodedJWT = verifier.verify(token);
    return decodedJWT;
  }

  // extrai o token do header "Authorization: Bearer <token> --- obs: utilizado
  // por JwtTokenFilter"
  public String resolveToken(HttpServletRequest request) {
    String bearerToken = request.getHeader("Authorization");

    // verifica se o conteudo do header não é nulo
    if (StringUtils.isNotBlank(bearerToken) &&

    // verifica se o conteudo do header começa com o prefixo "Bearer "
        bearerToken.startsWith("Bearer ")) {

      // retira o preixo "Bearer " e retorna somente o token
      return bearerToken.substring("Bearer ".length());
    }
    return null;
  }

  // verifica se o token nao expirou --- obs: utilizado por JwtTokenFilter
  public Boolean validateToken(String token) {
    // $ metodo que decodifica o token
    DecodedJWT decodedJWT = decodedToken(token);
    try {
      // verifica se a data de expiracao do token é anterior a data atual
      if (decodedJWT.getExpiresAt().before(new Date())) {
        return false;
      } else {
        return true;
      }
    } catch (Exception e) {
      throw new InvalidJwtAuthenticationException("Expired or Invalid JWT Token");
    }
  }
}
