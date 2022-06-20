package com.pig.basic.adapter;

import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.bind.util.ISO8601Utils;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期适配器
 */
public class DateAdapter extends TypeAdapter<Date> {

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public void write(JsonWriter jsonWriter, Date date) throws IOException {
        jsonWriter.value(sdf.format(date));
    }

    @Override
    public Date read(JsonReader jsonReader) throws IOException {
        if(jsonReader.peek() == JsonToken.NULL) {
            jsonReader.nextNull();
            return null;
        } else {
            Date date = this.deserializeToDate(jsonReader.nextString());
            return date;
        }
    }

    private Date deserializeToDate(String s) {
        synchronized (sdf) {
            try {
                return sdf.parse(s);
            } catch (ParseException ignored) {}
            try {
                return sdf.parse(s);
            } catch (ParseException ignored) {}
            try {
                return ISO8601Utils.parse(s, new ParsePosition(0));
            } catch (ParseException e) {
                throw new JsonSyntaxException(s, e);
            }
        }
    }
}
