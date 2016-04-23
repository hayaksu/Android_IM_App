package com.metacert.dev.metacert.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Domain {
  /* Domain */
  private String domain = null;
  /* Type */
  private String type = null;
  @JsonProperty("domain")
  public String getDomain() {
    return domain;
  }
  public void setDomain(String domain) {
    this.domain = domain;
  }

  @JsonProperty("type")
  public String getType() {
    return type;
  }
  public void setType(String type) {
    this.type = type;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class Domain {\n");
    sb.append("  domain: ").append(domain).append("\n");
    sb.append("  type: ").append(type).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}

