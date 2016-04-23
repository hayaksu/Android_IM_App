package com.metacert.dev.metacert.api;

import com.metacert.dev.client.ApiException;
import com.metacert.dev.client.ApiInvoker;
import com.metacert.dev.metacert.model.Lookup;
import com.metacert.dev.metacert.model.LookupParam;
import java.math.BigDecimal;
import java.util.*;
import java.io.File;
import org.apache.http.HttpEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.Consts;
import org.apache.http.entity.ContentType;

public class CheckApi {
  String basePath = "https://dev.metacert.com/v4";
  ApiInvoker apiInvoker = ApiInvoker.getInstance();

  public void addHeader(String key, String value) {
    getInvoker().addDefaultHeader(key, value);
  }

  public ApiInvoker getInvoker() {
    return apiInvoker;
  }

  public void setBasePath(String basePath) {
    this.basePath = basePath;
  }

  public String getBasePath() {
    return basePath;
  }

  /**
   * Look up the reputation of a URL
   * Look up the reputation of a single URL against different subscribed categories.
   * @param body Parameters used to look up URL reputation
   * @return Lookup
   */
  public Lookup lookup (LookupParam body) throws ApiException {
    
    Object postBody = body;

    // verify required params are set
    if(body == null ) {
       throw new ApiException(400, "missing required params");
    }
    // create path and map variables
    String path = "/check/".replaceAll("\\{format\\}","json");

    // query params
    Map<String, String> queryParams = new HashMap<String, String>();
    Map<String, String> headerParams = new HashMap<String, String>();
    Map<String, String> formParams = new HashMap<String, String>();

    // set contentType
    String[] contentTypes = {
      "application/json"};

    // set content type
    String contentType = contentTypes.length > 0 ? contentTypes[0] : "application/json";

    // form parameter
    if(contentType.startsWith("multipart/form-data")) {
      // file upload
      ContentType textUTF8 = ContentType.create("text/plain", Consts.UTF_8);
      MultipartEntityBuilder builder = MultipartEntityBuilder.create();
      HttpEntity httpEntity = builder.build();
      postBody = httpEntity;
    }
    else {
      }

    // authentication setting
    boolean requireAuth = true;

    try {
      String response = apiInvoker.invokeAPI(basePath, path, "POST", queryParams, postBody, headerParams, formParams, contentType, requireAuth);
      if(response != null){
        return (Lookup) ApiInvoker.deserialize(response, "", Lookup.class);
      }
      else {
        return null;
      }
    } catch (ApiException ex) {
      if(ex.getCode() == 404) {
        return null;
      }
      else {
        throw ex;
      }
    }
  }
  }

