package br.com.het.file.exporter.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import br.com.het.exception.BadRequestException;
import br.com.het.file.exporter.MediaTypes;
import br.com.het.file.exporter.contract.FileExporter;
import br.com.het.file.exporter.impl.CsvExporter;
import br.com.het.file.exporter.impl.XlsxExporter;

@Component
public class FileExporterFactory {

  private Logger logger = LoggerFactory.getLogger(FileExporterFactory.class);

  @Autowired
  private ApplicationContext context;

  public FileExporter geExporter(String acceptHeader) throws Exception {
    // mediaTypes é uma interface que criei para armazenar o valor do Accept para
    // csv e xlsx
    if (acceptHeader.equalsIgnoreCase(MediaTypes.APPLICATION_XLSX_VALUE)) {
      return context.getBean(XlsxExporter.class);
    } else if (acceptHeader.equalsIgnoreCase(MediaTypes.APPLICATION_CSV_VALUE)) {
      return context.getBean(CsvExporter.class);
    } else {
      throw new BadRequestException("Invalid File Format");
    }
  }
}
