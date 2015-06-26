package itc4j;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * Serialize {@link itc4j.NonLeafIDSerializer}
 *
 * @author Ian Eure <ian.eure@gmail.com>
 */
public class NonLeafIDSerializer extends StdSerializer<NonLeafID> {
  public NonLeafIDSerializer() {
    super(NonLeafID.class);
  }

  @Override
  public void serialize(final NonLeafID id,
                        final JsonGenerator jgen,
                        final SerializerProvider provider)
  throws IOException {
    jgen.writeStartArray();
    jgen.writeObject(id.getLeft());
    jgen.writeObject(id.getRight());
    jgen.writeEndArray();
  }
}
