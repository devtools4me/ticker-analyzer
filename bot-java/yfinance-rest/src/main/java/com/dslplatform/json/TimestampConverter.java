package com.dslplatform.json;

import java.io.IOException;
import java.util.Calendar;

@JsonConverter(target = Calendar.class)
public abstract class TimestampConverter {

  public static final JsonReader.ReadObject<Calendar> JSON_READER = new JsonReader.ReadObject<Calendar>() {
    public Calendar read(JsonReader reader) throws IOException {
      long time = NumberConverter.deserializeLong(reader);
      return calendar(time);
    }
  };

  public static final JsonWriter.WriteObject<Calendar> JSON_WRITER = new JsonWriter.WriteObject<Calendar>() {
    public void write(JsonWriter writer, Calendar value) {
      if (value == null) {
        writer.writeNull();
      } else {
        NumberConverter.serialize(timestamp(value), writer);
      }
    }
  };

  public static Calendar calendar(long timestamp) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(timestamp * 1000);
    return calendar;
  }

  public static long timestamp(Calendar calendar) {
    return calendar.getTimeInMillis() / 1000;
  }
}