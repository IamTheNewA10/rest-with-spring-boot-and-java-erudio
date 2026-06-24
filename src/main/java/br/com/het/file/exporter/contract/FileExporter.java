package br.com.het.file.exporter.contract;

import java.io.InputStream;
import java.util.List;

import org.springframework.core.io.Resource;

import br.com.het.data.dto.PersonDTO;

public interface FileExporter {

  Resource exportFile(List<PersonDTO> people) throws Exception;
}
