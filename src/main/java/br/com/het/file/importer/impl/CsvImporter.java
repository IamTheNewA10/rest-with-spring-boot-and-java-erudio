package br.com.het.file.importer.impl;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;

import br.com.het.data.dto.PersonDTO;
import br.com.het.file.importer.contract.FileImporter;

@Component
public class CsvImporter implements FileImporter {

  @Override
  public List<PersonDTO> importFile(InputStream inputStream) throws Exception {
    CSVFormat format = CSVFormat.Builder.create()
        .setHeader()
        .setSkipHeaderRecord(true)
        .setIgnoreEmptyLines(true)
        .setTrim(true)
        .build();
    Iterable<CSVRecord> records = format.parse(new InputStreamReader(inputStream));
    return parseRecordsToPersonDTO(records);
  }

  private List<PersonDTO> parseRecordsToPersonDTO(Iterable<CSVRecord> records) {
    List<PersonDTO> people = new ArrayList<>();

    for (CSVRecord record : records) {
      PersonDTO person = new PersonDTO();
      person.setFirstName(record.get("first_name"));
      person.setLastName(record.get("last_name"));
      person.setAdress(record.get("address"));
      person.setGender(record.get("gender"));
      person.setEnabled(true);
      people.add(person);
    }
    return people;
  }

}
