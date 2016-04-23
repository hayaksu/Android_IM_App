package com.metacert.dev.metacert.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.*;
public class Datum {
  /* Domains */
  private List<Domain> Domains = new ArrayList<Domain>();
  /* Folders */
  private List<String> Folders = new ArrayList<String>();
  /* Urls */
  private List<String> URLs = new ArrayList<String>();
  @JsonProperty("Domains")
  public List<Domain> getDomains() {
    return Domains;
  }
  public void setDomains(List<Domain> Domains) {
    this.Domains = Domains;
  }

  @JsonProperty("Folders")
  public List<String> getFolders() {
    return Folders;
  }
  public void setFolders(List<String> Folders) {
    this.Folders = Folders;
  }

  @JsonProperty("URLs")
  public List<String> getURLs() {
    return URLs;
  }
  public void setURLs(List<String> URLs) {
    this.URLs = URLs;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class Datum {\n");
    sb.append("  Domains: ").append(Domains).append("\n");
    sb.append("  Folders: ").append(Folders).append("\n");
    sb.append("  URLs: ").append(URLs).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}

