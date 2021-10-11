package me.devtools4.telegram.api;

import com.dslplatform.json.CompiledJson;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@CompiledJson
public class StartInfo {
  private String text;
  private String callbackData;

  public static StartInfo of(String text, String callbackData) {
    StartInfo info = new StartInfo();
    info.setText(text);
    info.setCallbackData(callbackData);
    return info;
  }
}