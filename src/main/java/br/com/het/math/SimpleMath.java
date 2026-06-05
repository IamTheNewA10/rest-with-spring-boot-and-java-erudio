package br.com.het.math;

public class SimpleMath {

  public Double sum(Double n1, Double n2) {
    return n1 + n2;
  }

  public Double subtraction(Double n1, Double n2) {
    return n1 - n2;
  }

  public Double multiplication(Double n1, Double n2) {
    return n1 * n2;
  }

  public Double division(Double n1, Double n2) {
    return n1 / n2;
  }

  public Double mean(Double n1, Double n2) {
    return (n1 + n2) / 2;
  }

  public Double mean(Double n1) {
    return Math.sqrt(n1);
  }
}
