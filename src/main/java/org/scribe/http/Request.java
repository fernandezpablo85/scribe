/* 
Copyright 2010 Pablo Fernandez

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package org.scribe.http;

import static org.scribe.encoders.URL.*;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Represents an HTTP Request object
 * 
 * @author Pablo Fernandez
 */
public class Request {
  
  private static final String CONTENT_LENGTH ="Content-Length"; 
  
  private String url;
  private Verb verb;
  private Map<String, String> bodyParams;
  private Map<String, String> headers;
  private String payload = null;
  
  public Request(Verb verb, String url){
    this.verb = verb;
    this.url = url;
    this.bodyParams = new HashMap<String, String>();
    this.headers = new HashMap<String, String>();
  }
  
  /**
   * Execute the request and return a {@link Response}
   * 
   * @return Http Response
   * @throws RuntimeException if the connection cannot be created.
   */
  public Response send(){
    try{
      return doSend();
    }catch(IOException ioe){
      throw new RuntimeException("Problems while creating connection", ioe);
    }
  }
  
  Response doSend() throws IOException{
    HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
    connection.setRequestMethod(this.verb.name());
    addHeaders(connection);
    if(verb.equals(Verb.PUT) || verb.equals(Verb.POST)){
      if(payload != null){
        addPayload(connection);
      }else{
        addBody(connection);
      }
    }
      
    return new Response(connection);
  }
  
  void addHeaders (HttpURLConnection conn){
    for(String key: headers.keySet())
      conn.setRequestProperty(key, headers.get(key));
  }
  
  void addBody(HttpURLConnection conn) throws IOException{
    String body = queryString(bodyParams);
    conn.setRequestProperty(CONTENT_LENGTH, String.valueOf(body.getBytes().length));
    conn.setDoOutput(true);
    conn.getOutputStream().write(body.getBytes());
  }
  
  void addPayload(HttpURLConnection conn) throws IOException {
    conn.setRequestProperty(CONTENT_LENGTH, String.valueOf(payload.getBytes().length));
    conn.setDoOutput(true);
    conn.getOutputStream().write(payload.getBytes());
  }
  
  /**
   * Add an HTTP Header to the Request
   * 
   * @param name
   * @param value
   */
  public void addHeader(String key, String value){
    this.headers.put(key, value);
  }
  
  /**
   * Add a body Parameter (for POST/ PUT Requests)
   * 
   * @param name
   * @param value
   */
  public void addBodyParameter(String key, String value){
    this.bodyParams.put(key, value);
  }
  
  /**
   * Add body payload.
   * 
   * This method is used when the HTTP body is not a form-url-encoded string, but another thing.
   * Like for example XML.
   * 
   * Note: The contents are not part of the OAuth signature
   * 
   * @param payload
   */
  public void addPayload(String payload){
    this.payload = payload;
  }
  
  /**
   * Get a {@link Map} of the query string parameters.
   * 
   * @return a map containing the query string parameters
   */
  public Set<Map.Entry<String, String>> getQueryStringParams(){
    try{
      Map<String, String> params = new HashMap<String, String>();
      String query = new URL(url).getQuery();
      if(query != null){
        for(String param : query.split("&")){
          String pair[] = param.split("=");
          params.put(pair[0], pair[1]);
        }
      }
      return params.entrySet();
    }catch(MalformedURLException mue){
      throw new RuntimeException("Malformed URL",mue);
    }
  }
  /**
   * Obtains a {@link Map} of the body parameters.
   * 
   * @return a map containing the body parameters.
   */
  public Set<Map.Entry<String, String>> getBodyParams(){
    return bodyParams.entrySet();
  }
    
  /**
   * Obtains the URL of the HTTP Request.
   * 
   * @return the original URL of the HTTP Request
   */
  public String getUrl(){
    return url;
  }
  
  /**
   * Returns the URL without the port and the query string part.
   * 
   * @return the OAuth-sanitized URL
   */
  public String getSanitizedUrl(){
    return url.replaceAll("\\?.*", "").replace("\\:\\d{4}", "");
  }
  
  /**
   * Returns the HTTP Verb
   * 
   * @return the verb
   */
  public Verb getVerb(){
    return verb; 
  }
   
  /**
   * An enumeration containing the most common HTTP Verbs.
   * 
   * @author Pablo Fernandez
   */
  public static enum Verb{
    GET,
    POST,
    PUT,
    DELETE
  }
}