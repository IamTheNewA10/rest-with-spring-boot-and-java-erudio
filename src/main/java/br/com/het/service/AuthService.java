package br.com.het.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.het.data.dto.security.AccountCredentialsDTO;
import br.com.het.data.dto.security.TokenDTO;
import br.com.het.repository.UserRepository;
import br.com.het.security.jwt.JwtTokenProvider;

@Service
public class AuthService {

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

}
