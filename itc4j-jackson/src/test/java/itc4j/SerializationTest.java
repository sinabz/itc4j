package itc4j;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Ian Eure <ian.eure@gmail.com>
 */
public class SerializationTest {

  private static final ObjectMapper json = new ObjectMapper();

  static {
    json.registerModule(new JacksonModuleITC4J());
  }

  @Test
  public void testSimpleSerialzation() throws Exception {
    final Stamp s = new Stamp();
    final String js = json.writeValueAsString(s);

    final Stamp ns = json.readValue(js, Stamp.class);
    assertEquals(s, ns);
  }

  @Test
  public void testComplexSerialzation() throws Exception {
    Stamp s = new Stamp();
    s = s.event().fork()[0].fork()[1].event().event().fork()[0].event();
    final String js = json.writeValueAsString(s);

    final Stamp ns = json.readValue(js, Stamp.class);
    assertEquals(s, ns);
  }
}
