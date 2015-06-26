package itc4j;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * Serialize {@link itc4j.Stamp}
 *
 * @author Ian Eure <ian.eure@gmail.com>
 */
public class StampSerializer extends StdSerializer<Stamp> {
  public StampSerializer() {
    super(Stamp.class);
  }

  @Override
  public void serialize(final Stamp s,
                        final JsonGenerator jgen,
                        final SerializerProvider provider)
  throws IOException {
    jgen.writeStartArray();
    jgen.writeObject(s.getId());
    jgen.writeObject(s.getEvent());
    jgen.writeEndArray();
  }
}
