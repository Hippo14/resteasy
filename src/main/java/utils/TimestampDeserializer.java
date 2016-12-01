package utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by MSI on 2016-10-30.
 */
public class TimestampDeserializer extends JsonDeserializer<Timestamp> {

    final static Logger LOG = Logger.getLogger(TimestampDeserializer.class);

    @Override
    public Timestamp deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        String date = jsonParser.getText();
        try {
            Date parsedDate = format.parse(date);
            return new Timestamp(parsedDate.getTime());
        } catch (ParseException e) {
            LOG.error(e);
            throw new RuntimeException(e);
        }
    }

}
