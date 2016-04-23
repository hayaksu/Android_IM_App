package com.metacert.dev.metacert.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.metacert.dev.metacert.model.Status;
import com.metacert.dev.metacert.model.Datum;
public class Lookup {
  /* Data */
  private Datum data = null;
  /* Status */
  private Status status = null;
  @JsonProperty("data")
  public Datum getData() {
    return data;
  }
  public void setData(Datum data) {
    this.data = data;
  }

  @JsonProperty("status")
  public Status getStatus() {
    return status;
  }
  public void setStatus(Status status) {
    this.status = status;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class Lookup {\n");
    sb.append("  data: ").append(data).append("\n");
    sb.append("  status: ").append(status).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}

