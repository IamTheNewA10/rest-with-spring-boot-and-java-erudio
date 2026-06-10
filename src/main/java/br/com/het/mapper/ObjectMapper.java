package br.com.het.mapper;

import java.util.ArrayList;
import java.util.List;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;

public class ObjectMapper {

  private static Mapper mapper = DozerBeanMapperBuilder.buildDefault();

  public static <O, D> D parseObject(O origin, Class<D> destination) {
    return mapper.map(origin, destination);
  }

  public static <O, D> List<D> parseListObject(List<O> origin, Class<D> destination) {
    List<D> destinationObjects = new ArrayList<D>(); // <- Lista temporaria para armazenar os objetos convertidos
    for (Object o : origin) { // <- converte cada item "o" do tipo de origem para o tipo de destino
      destinationObjects.add(mapper.map(o, destination)); // Ex: Do tipo Person ao tipo PersonDTO ou vice e versa
    }
    return destinationObjects;
  }
}
