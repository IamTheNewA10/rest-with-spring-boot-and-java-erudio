package br.com.het.request.converter;

import br.com.het.exception.UnsupportedMathOperationException;

public class NumberConverter {

  public static Double convertToDouble(String strN) throws UnsupportedMathOperationException {
    if (strN == null || strN.isEmpty())
      throw new UnsupportedMathOperationException("Please set a numeric value!!"); //
    // troca a virgula por ponto para poder converter para Double
    String number = strN.replace(",", ".");
    return Double.parseDouble(number);
  }

  public static boolean isNumeric(String strN) {
    if (strN == null || strN.isEmpty())
      return false;
    // troca a virgula por ponto para poder realizar operacoes
    String number = strN.replace(",", ".");
    // verifica se é um numero e retorna true ou false
    return number.matches("[-+]?[0-9]*\\.?[0-9]+");
  }
}
