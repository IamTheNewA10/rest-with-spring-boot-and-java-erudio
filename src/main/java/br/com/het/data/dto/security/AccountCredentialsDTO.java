package br.com.het.data.dto.security;

import java.io.Serializable;

public class AccountCredentialsDTO implements Serializable {

  private static final Long serialVersionUID = 1L;

  private String userName;
  private String fullName;
  private String password;

  public AccountCredentialsDTO() {
  }

  public AccountCredentialsDTO(String userName, String fullName, String password) {
    this.userName = userName;
    this.fullName = fullName;
    this.password = password;
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

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getFullName() {
    return fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((userName == null) ? 0 : userName.hashCode());
    result = prime * result + ((fullName == null) ? 0 : fullName.hashCode());
    result = prime * result + ((password == null) ? 0 : password.hashCode());
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
    AccountCredentialsDTO other = (AccountCredentialsDTO) obj;
    if (userName == null) {
      if (other.userName != null)
        return false;
    } else if (!userName.equals(other.userName))
      return false;
    if (fullName == null) {
      if (other.fullName != null)
        return false;
    } else if (!fullName.equals(other.fullName))
      return false;
    if (password == null) {
      if (other.password != null)
        return false;
    } else if (!password.equals(other.password))
      return false;
    return true;
  }

}
