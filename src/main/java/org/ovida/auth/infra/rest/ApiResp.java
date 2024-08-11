package org.ovida.auth.infra.rest;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class ApiResp {

  private String code;

  private String msg;

  private Object data;
}