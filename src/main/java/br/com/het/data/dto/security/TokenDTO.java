package br.com.het.data.dto.security;

import java.io.Serializable;
import java.util.Date;

public class TokenDTO implements Serializable {

  private static final Long serialVersionUID = 1L;

  private String userName;
  private Boolean authenticated;
  private Date created;
  private Date expiration;
  private String accesToken;
  private String refreshToken;

  public TokenDTO() {
  }

  public TokenDTO(String userName, Boolean authenticated, Date created, Date expiration, String accesToken,
      String refreshToken) {
    this.userName = userName;
    this.authenticated = authenticated;
    this.created = created;
    this.expiration = expiration;
    this.accesToken = accesToken;
    this.refreshToken = refreshToken;
  }

  public static Long getSerialversionuid() {
    return serialVersionUID;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public Boolean getAuthenticated() {
    return authenticated;
  }

  public void setAuthenticated(Boolean authenticated) {
    this.authenticated = authenticated;
  }

  public Date getCreated() {
    return created;
  }

  public void setCreated(Date created) {
    this.created = created;
  }

  public Date getExpiration() {
    return expiration;
  }

  public void setExpiration(Date expiration) {
    this.expiration = expiration;
  }

  public String getAccesToken() {
    return accesToken;
  }

  public void setAccesToken(String accesToken) {
    this.accesToken = accesToken;
  }

  public String getRefreshToken() {
    return refreshToken;
  }

  public void setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((userName == null) ? 0 : userName.hashCode());
    result = prime * result + ((authenticated == null) ? 0 : authenticated.hashCode());
    result = prime * result + ((created == null) ? 0 : created.hashCode());
    result = prime * result + ((expiration == null) ? 0 : expiration.hashCode());
    result = prime * result + ((accesToken == null) ? 0 : accesToken.hashCode());
    result = prime * result + ((refreshToken == null) ? 0 : refreshToken.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    TokenDTO other = (TokenDTO) obj;
    if (userName == null) {
      if (other.userName != null)
        return false;
    } else if (!userName.equals(other.userName))
      return false;
    if (authenticated == null) {
      if (other.authenticated != null)
        return false;
    } else if (!authenticated.equals(other.authenticated))
      return false;
    if (created == null) {
      if (other.created != null)
        return false;
    } else if (!created.equals(other.created))
      return false;
    if (expiration == null) {
      if (other.expiration != null)
        return false;
    } else if (!expiration.equals(other.expiration))
      return false;
    if (accesToken == null) {
      if (other.accesToken != null)
        return false;
    } else if (!accesToken.equals(other.accesToken))
      return false;
    if (refreshToken == null) {
      if (other.refreshToken != null)
        return false;
    } else if (!refreshToken.equals(other.refreshToken))
      return false;
    return true;
  }

}
