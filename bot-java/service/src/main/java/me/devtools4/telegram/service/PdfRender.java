package me.devtools4.telegram.service;

import static me.devtools4.telegram.api.Multipliers.COLUMNS;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import java.util.List;
import me.devtools4.telegram.api.Multipliers;

public class PdfRender {

  public byte[] render(List<Multipliers> list) {
    try (var os = new ByteArrayOutputStream()) {
      var document = new Document(PageSize.A4.rotate());
      PdfWriter.getInstance(document, os);
      document.open();

      var font = FontFactory.getFont(FontFactory.COURIER, 10, BaseColor.BLACK);
      var table = new PdfPTable(COLUMNS.size());
      COLUMNS.forEach(x -> {
        var header = new PdfPCell();
        header.setBackgroundColor(BaseColor.LIGHT_GRAY);
        header.setBorderWidth(2);
        header.setPhrase(new Phrase(x, font));
        table.addCell(header);
      });

      list.forEach(x -> x
          .str()
          .stream()
          .map(s -> new Phrase(s, font))
          .forEach(table::addCell));

      document.add(table);
      document.close();

      return os.toByteArray();
    } catch (Exception ex) {
      throw new IllegalArgumentException(ex);
    }
  }
}