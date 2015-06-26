package itc4j;

import java.io.IOException;
import java.util.Arrays;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.DeserializationContext;

/**
 * Deserialize {@link itc4j.Stamp}.
 *
 * @author Ian Eure <ian.eure@gmail.com>
 */
@SuppressWarnings("serial")
public class StampDeserializer extends StdDeserializer<Stamp>
{
  public StampDeserializer() { super(Stamp.class); }

  @Override
  public Stamp deserialize(JsonParser jp, DeserializationContext ctxt)
    throws IOException, JsonProcessingException {

    JsonToken t = jp.getCurrentToken();
    assert t == JsonToken.START_ARRAY;

    return new Stamp(handleID(jp, ctxt), handleEvent(jp, ctxt));
  }

  /**
   * Deserialize an {@link itc4j.ID}
   */
  private ID handleID(JsonParser jp, DeserializationContext ctxt)
  throws IOException {
    final JsonToken t = jp.nextToken();
    if (t == JsonToken.START_ARRAY) {
      // Non-leaf, recursively deser left/right
      final ID left = handleID(jp, ctxt);
      final ID right = handleID(jp, ctxt);
      assert jp.nextToken() == JsonToken.END_ARRAY;
      return new NonLeafID(left, right);
    } else {
      // Leaf, base case
      return new LeafID(jp.getIntValue());
    }
  }

  /**
   * Deserialize an {@link itc4j.Event}
   */
  private Event handleEvent(JsonParser jp, DeserializationContext ctxt)
  throws IOException {
    JsonToken t = jp.nextToken();
    if (t == JsonToken.START_ARRAY) {
      t = jp.nextToken();
      // Value is always present
      final int value = jp.getIntValue();
      // Non-leaf, recursively deser left/right
      final Event left = handleEvent(jp, ctxt);
      final Event right = handleEvent(jp, ctxt);
      t = jp.nextToken();
      return new NonLeafEvent(value, left, right);
    } else {
      // Leaf, base case
      return new LeafEvent(jp.getIntValue());
    }
  }
}
