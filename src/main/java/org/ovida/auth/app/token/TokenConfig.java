package org.ovida.auth.app.token;

import java.time.Duration;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("app.app.token")
public class TokenConfig {

  private Duration expiration = Duration.ofHours(2);
}
