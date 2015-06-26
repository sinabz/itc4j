package itc4j;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.Module;

/**
 * Module for serializing/deserializing itc4j objects.
 *
 * @author Ian Eure <ian.eure@gmail.com>
 */
public class JacksonModuleITC4J extends SimpleModule {
  public JacksonModuleITC4J() {
    super("JacksonModuleITC4J", new Version(0, 0, 0, null, "itc4j", "itc4j"));

    addSerializer(Stamp.class, new StampSerializer());
    addSerializer(NonLeafID.class, new NonLeafIDSerializer());
    addSerializer(LeafID.class, new LeafIDSerializer());
    addSerializer(NonLeafEvent.class, new NonLeafEventSerializer());
    addSerializer(LeafEvent.class, new LeafEventSerializer());

    addDeserializer(Stamp.class, new StampDeserializer());
  }
}
