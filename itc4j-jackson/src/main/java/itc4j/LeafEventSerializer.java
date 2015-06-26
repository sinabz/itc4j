package itc4j;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * Serialize {@link itc4j.LeafEvent}
 *
 * @author Ian Eure <ian.eure@gmail.com>
 */
public class LeafEventSerializer extends StdSerializer<LeafEvent> {
  public LeafEventSerializer() {
    super(LeafEvent.class);
  }

  @Override
  public void serialize(final LeafEvent ev,
                        final JsonGenerator jgen,
                        final SerializerProvider provider)
  throws IOException {
    jgen.writeNumber(ev.getValue());
  }
}
