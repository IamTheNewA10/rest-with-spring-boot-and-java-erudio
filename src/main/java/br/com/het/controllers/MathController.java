package br.com.het.controllers;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.het.exception.UnsupportedMathOperationException;
import br.com.het.math.SimpleMath;
import br.com.het.request.converter.NumberConverter;

@RestController
@RequestMapping("/math")
public class MathController {

  private SimpleMath math = new SimpleMath();

  @RequestMapping("/sum/{n1}/{n2}")
  public Double sum(
      @PathVariable("n1") String n1,
      @PathVariable("n2") String n2) throws Exception {
    if (!NumberConverter.isNumeric(n1) || !NumberConverter.isNumeric(n2))
      throw new UnsupportedMathOperationException("Please set a numeric value!!");
    return math.sum(NumberConverter.convertToDouble(n1), NumberConverter.convertToDouble(n2));
  }

  @RequestMapping("/sub/{n1}/{n2}")
  public Double subtraction(
      @PathVariable("n1") String n1,
      @PathVariable("n2") String n2) throws Exception {
    if (!NumberConverter.isNumeric(n1) || !NumberConverter.isNumeric(n2))
      throw new UnsupportedMathOperationException("Please set a numeric value!!");
    return math.subtraction(NumberConverter.convertToDouble(n1), NumberConverter.convertToDouble(n2));
  }

  @RequestMapping("/mult/{n1}/{n2}")
  public Double multiplication(
      @PathVariable("n1") String n1,
      @PathVariable("n2") String n2) throws Exception {
    if (!NumberConverter.isNumeric(n1) || !NumberConverter.isNumeric(n2))
      throw new UnsupportedMathOperationException("Please set a numeric value!!");
    return math.multiplication(NumberConverter.convertToDouble(n1), NumberConverter.convertToDouble(n2));
  }

  @RequestMapping("/div/{n1}/{n2}")
  public Double division(
      @PathVariable("n1") String n1,
      @PathVariable("n2") String n2) throws Exception {
    if (!NumberConverter.isNumeric(n1) || !NumberConverter.isNumeric(n2))
      throw new UnsupportedMathOperationException("Please set a numeric value!!");
    return math.division(NumberConverter.convertToDouble(n1), NumberConverter.convertToDouble(n2));
  }

  @RequestMapping("/mean/{n1}/{n2}")
  public Double mean(
      @PathVariable("n1") String n1,
      @PathVariable("n2") String n2) throws Exception {
    if (!NumberConverter.isNumeric(n1) || !NumberConverter.isNumeric(n2))
      throw new UnsupportedMathOperationException("Please set a numeric value!!");
    return math.mean(NumberConverter.convertToDouble(n1), NumberConverter.convertToDouble(n2));
  }

  @RequestMapping("/sqr/{n1}")
  public Double mean(
      @PathVariable("n1") String n1) throws Exception {
    if (!NumberConverter.isNumeric(n1))
      throw new UnsupportedMathOperationException("Please set a numeric value!!");
    return math.mean(NumberConverter.convertToDouble(n1));
  }
}
