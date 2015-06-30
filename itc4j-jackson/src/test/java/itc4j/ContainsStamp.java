package itc4j;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Test class containing a Stamp, to ensure that they're correctly deserialized.
 *
 * @author Ian Eure <ian.eure@gmail.com>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"clock", "foo"})
public class ContainsStamp {

  @JsonProperty("foo")
  private String foo;

  @JsonProperty("clock")
  private Stamp clock;

  public ContainsStamp(Stamp clock,
                       String foo) {
    this.clock = clock;
    this.foo = foo;
  }


  public ContainsStamp() {}

  @JsonProperty("foo")
  public String getFoo() {
    return foo;
  }

  @JsonProperty("foo")
  public void setFoo(String foo) {
    this.foo = foo;
  }

  @JsonProperty("clock")
  public Stamp getClock() {
    return clock;
  }

  @JsonProperty("clock")
  public void setClock(Stamp clock) {
    this.clock = clock;
  }
}
