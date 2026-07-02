package br.com.het.service;

import static br.com.het.mapper.ObjectMapper.parseObject;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm;
import org.springframework.stereotype.Service;

import br.com.het.data.dto.PersonDTO;
import br.com.het.data.dto.security.AccountCredentialsDTO;
import br.com.het.data.dto.security.TokenDTO;
import br.com.het.exception.RequiredObjectIsNullException;
import br.com.het.model.Person;
import br.com.het.model.User;
import br.com.het.repository.UserRepository;
import br.com.het.security.jwt.JwtTokenProvider;

@Service
public class AuthService {

  Logger logger = LoggerFactory.getLogger(AuthService.class);

  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  JwtTokenProvider tokenProvider;

  @Autowired
  UserRepository userRepository;

  public ResponseEntity<TokenDTO> signIn(AccountCredentialsDTO credentials) {

    // o metodo ".autenticate"" verfica no banco se as credenciais username e senha
    // batem com as do banco, se sim -> busca o usuario completo no banco, se não ->
    // lanca excecão "Bad Credentials"
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            credentials.getUserName(),
            credentials.getPassword()));

    var user = userRepository.findByUsername(credentials.getUserName());
    if (user == null)
      throw new UsernameNotFoundException("Username" + credentials.getUserName() + "Not Found!");

    // chama o tokenProvider parq gerar o token
    var token = tokenProvider.createAccessToken(
        credentials.getUserName(),
        user.getRoles());

    return ResponseEntity.ok(token);
  }

  public ResponseEntity<TokenDTO> refresh(String username, String refreshToken) {
    var user = userRepository.findByUsername(username);
    TokenDTO token;
    if (user != null) {
      token = tokenProvider.refreshToken(refreshToken);
    } else {
      throw new UsernameNotFoundException("Username" + username + "Not Found!");
    }

    return ResponseEntity.ok(token);
  }

  public AccountCredentialsDTO create(AccountCredentialsDTO user) {

    if (user == null)
      throw new RequiredObjectIsNullException();

    logger.info("Creating new person!");
    var entity = new User();
    entity.setFullName(user.getFullName());
    entity.setUsername(user.getUserName());
    entity.setPassword(generateHashedPassword(user.getPassword()));
    entity.setAccoutNonExpired(true);
    entity.setAccoutNonLocked(true);
    entity.setCredentialsNonExpired(true);
    entity.setEnabled(true);

    var dto = userRepository.save(entity);
    return new AccountCredentialsDTO(dto.getUsername(), dto.getFullName(), dto.getPassword());
  }

  // pega a senha cadstrada encrypta e retorna
  private String generateHashedPassword(String password) {
    PasswordEncoder pbkdf2Encoder = new Pbkdf2PasswordEncoder(
        "", 8, 185000, SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256);

    Map<String, PasswordEncoder> encoders = new HashMap<>();
    encoders.put("pbkdf2", pbkdf2Encoder);
    DelegatingPasswordEncoder passwordEncoder = new DelegatingPasswordEncoder("pbkdf2", encoders);

    passwordEncoder.setDefaultPasswordEncoderForMatches(pbkdf2Encoder);
    return passwordEncoder.encode(password);
  }
}
