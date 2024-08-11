package org.ovida.auth.infra.jackson;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonValueFormat;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

  @Bean
  public ObjectMapper objectMapper() {
    var simpleModule = new SimpleModule("ovida")
      .addSerializer(LocalDateTime.class, new LocalDateTimeSerializer())
      .addSerializer(BigDecimal.class, new BigDecimalSerializer());

    return new ObjectMapper()
      .findAndRegisterModules()
      .registerModule(simpleModule)
      .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
      .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
      .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
      .setDefaultPropertyInclusion(Include.NON_EMPTY);
  }

  public static class LocalDateTimeSerializer extends StdSerializer<LocalDateTime> {

    public LocalDateTimeSerializer() {
      this(LocalDateTime.class);
    }

    public LocalDateTimeSerializer(Class<LocalDateTime> t) {
      super(t);
    }

    /**
     * Serializes the {@link LocalDateTime} value to the ISO-8601 string representation with UTC time zone.
     */
    @Override
    public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider provider) throws IOException {
      gen.writeString(DateTimeFormatter.ISO_DATE_TIME.format(value.atOffset(ZoneOffset.UTC)));
    }

    @Override
    public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
      throws JsonMappingException {
      var v2 = visitor.expectStringFormat(typeHint);
      if (v2 != null) {
        v2.format(JsonValueFormat.DATE_TIME);
      }
    }
  }

  public static class BigDecimalSerializer extends StdSerializer<BigDecimal> {

    public BigDecimalSerializer() {
      this(BigDecimal.class);
    }

    public BigDecimalSerializer(Class<BigDecimal> t) {
      super(t);
    }

    @Override
    public void serialize(BigDecimal value, JsonGenerator gen, SerializerProvider serializers)
      throws IOException {
      gen.writeString(value.stripTrailingZeros().toPlainString());
    }
  }
}