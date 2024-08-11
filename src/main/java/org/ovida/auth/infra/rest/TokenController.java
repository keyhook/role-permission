package org.ovida.auth.infra.rest;

import jakarta.validation.Valid;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.ovida.auth.app.token.CreateTokenCmd;
import org.ovida.auth.app.token.TokenAppSvc;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/tokens")
class TokenController {

  private final TokenAppSvc tokenAppSvc;

  @PostMapping
  public Object create(@Valid @RequestBody CreateTokenRequest request) {
    var cmd = new CreateTokenCmd()
      .setEmail(request.getEmail())
      .setPassword(request.getPassword());

    return Map.of("token", tokenAppSvc.create(cmd));
  }
}
