package me.devtools4.telegram.df;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.With;

@Data
@Builder
public class PngProps {

  @NonNull
  private String rowKeyColumnName;
  @NonNull
  private String columnName;
  @NonNull
  private Integer width;
  @With
  @NonNull
  private Integer height;
}