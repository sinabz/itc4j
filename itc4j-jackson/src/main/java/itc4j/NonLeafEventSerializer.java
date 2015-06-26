package itc4j;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * Serialize {@link itc4j.NonLeafEventSerializer}
 *
 * @author Ian Eure <ian.eure@gmail.com>
 */
public class NonLeafEventSerializer extends StdSerializer<NonLeafEvent> {
  public NonLeafEventSerializer() {
    super(NonLeafEvent.class);
  }

  @Override
  public void serialize(final NonLeafEvent id,
                        final JsonGenerator jgen,
                        final SerializerProvider provider)
  throws IOException {
    jgen.writeStartArray();
    jgen.writeNumber(id.getValue());
    jgen.writeObject(id.getLeft());
    jgen.writeObject(id.getRight());
    jgen.writeEndArray();
  }
}
