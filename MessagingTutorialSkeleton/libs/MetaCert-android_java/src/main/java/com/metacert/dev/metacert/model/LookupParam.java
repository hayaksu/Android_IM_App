package com.metacert.dev.metacert.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LookupParam {
  /* URL */
  private String url = null;
  @JsonProperty("url")
  public String getUrl() {
    return url;
  }
  public void setUrl(String url) {
    this.url = url;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class LookupParam {\n");
    sb.append("  url: ").append(url).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}

