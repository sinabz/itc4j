package itc4j;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * Serialize {@link itc4j.LeafIDSerializer}
 *
 * @author Ian Eure <ian.eure@gmail.com>
 */
public class LeafIDSerializer extends StdSerializer<LeafID> {
  public LeafIDSerializer() {
    super(LeafID.class);
  }

  @Override
  public void serialize(final LeafID id,
                        final JsonGenerator jgen,
                        final SerializerProvider provider)
  throws IOException {
    jgen.writeNumber(id.getValue());
  }
}
