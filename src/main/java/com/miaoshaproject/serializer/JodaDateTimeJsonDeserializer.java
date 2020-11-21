package com.miaoshaproject.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.IOException;

/**
 * @Author: vvshuai
 * @Description:
 * @Date: Created in 9:22 2020/9/14
 * @Modified By:
 */
public class JodaDateTimeJsonDeserializer extends JsonDeserializer<DateTime> {
    @Override
    public DateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        String datestring = jsonParser.readValueAs(String.class);
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

        return DateTime.parse(datestring, formatter);
    }
}
