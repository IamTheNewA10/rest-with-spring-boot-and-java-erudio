package br.com.het.file.importer.contract;

import java.io.InputStream;
import java.util.List;

import br.com.het.data.dto.PersonDTO;

public interface FileImporter {

  List<PersonDTO> importFile(InputStream inputStream) throws Exception;
}
