package br.com.het.security.jwt;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

// a classe é um bean que intercepta as requisiçoes de endpoints protegidos e faz a autenticaçao do metodo
public class JwtTokenFilter extends GenericFilterBean {

  @Autowired
  private JwtTokenProvider tokenProvider;

  public JwtTokenFilter(JwtTokenProvider tokenProvider) {
    this.tokenProvider = tokenProvider;
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain filter)
      throws IOException, ServletException {

    // armazena o token fornecido por JwtTokenProvider pelo metodo ".resolveToken"
    // que captura o token do Header da requisição
    var token = tokenProvider.resolveToken((HttpServletRequest) request);

    // verifica se o token não esta em branco
    if (StringUtils.isNotBlank(token) &&
    // verifica se o token nao expirou por meio do metodo ".validateToken" de
    // JwtTokenProvider
        tokenProvider.validateToken(token)) {

      // metodo que retorna um atributo Authentication contendo as informaçoes da
      // autenticaçao
      Authentication authentication = tokenProvider.getAuthentication(token);
      if (authentication != null) {
        // Armazena informações do usuário autenticado na request atual
        SecurityContextHolder.getContext().setAuthentication(authentication);
      }

    }

    // permite a requisicao passar adiante
    filter.doFilter(request, response);
  }

}
