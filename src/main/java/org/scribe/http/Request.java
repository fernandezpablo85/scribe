package org.scribe.http;

import static org.scribe.encoders.URL.*;

import java.io.*;
import java.net.*;
import java.util.*;

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
  
  public void addHeader(String key, String value){
    this.headers.put(key, value);
  }
  
  public void addBodyParameter(String key, String value){
    this.bodyParams.put(key, value);
  }
  
  public void addPayload(String payload){
    this.payload = payload;
  }
  
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
  
  public Set<Map.Entry<String, String>> getBodyParams(){
    return bodyParams.entrySet();
  }
    
  public String getUrl(){
    return url;
  }
  
  public String getSanitizedUrl(){
    return url.replaceAll("\\?.*", "").replace("\\:\\d{4}", "");
  }
  
  public Verb getVerb(){
    return verb; 
  }
    
  public static enum Verb{
    GET,
    POST,
    PUT,
    DELETE
  }
}