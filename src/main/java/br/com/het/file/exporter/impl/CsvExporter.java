package br.com.het.file.exporter.impl;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import br.com.het.data.dto.PersonDTO;
import br.com.het.file.exporter.contract.FileExporter;

@Component
public class CsvExporter implements FileExporter {

  @Override
  public Resource exportFile(List<PersonDTO> people) throws Exception {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    OutputStreamWriter writter = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);

    CSVFormat csvFormat = CSVFormat.Builder.create()
        .setHeader("ID", "First Name", "Last Name", "Address", "Gender", "Enabled")
        .setSkipHeaderRecord(false)
        .build();

    try (CSVPrinter printer = new CSVPrinter(writter, csvFormat)) {
      for (PersonDTO person : people) {
        printer.printRecord(
            person.getId(),
            person.getFirstName(),
            person.getLastName(),
            person.getAdress(),
            person.getGender(),
            person.isEnabled());
      }
    }

    return new ByteArrayResource(outputStream.toByteArray());
  }

}
