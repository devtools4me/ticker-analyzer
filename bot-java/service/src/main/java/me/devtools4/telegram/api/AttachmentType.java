package me.devtools4.telegram.api;

import org.springframework.http.MediaType;

public enum AttachmentType {
  PNG, PDF;

  public MediaType mediaType() {
    switch (this) {
      case PDF: return MediaType.APPLICATION_PDF;
      case PNG: return MediaType.IMAGE_PNG;
      default: throw new IllegalArgumentException("Unsupported value=" + this);
    }
  }

  public String fileExtension() {
    switch (this) {
      case PDF: return ".pdf\"";
      case PNG: return ".png\"";
      default: throw new IllegalArgumentException("Unsupported value=" + this);
    }
  }
}