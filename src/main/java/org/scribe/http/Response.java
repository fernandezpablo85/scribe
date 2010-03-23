package org.scribe.http;

import java.io.*;
import java.net.*;
import java.util.*;

public class Response {
  
  private static final String EMPTY = "";
  
  private int code;
  private String body;
  private Map<String, String> headers;
  
  public Response(HttpURLConnection connection) throws IOException{
    try{
      connection.connect();
      code = connection.getResponseCode();
      body = parseBody(connection);
      headers = parseHeaders(connection);
    }catch(UnknownHostException e){
      code = 404;
      body = EMPTY;
    }
  }
  
  String parseBody(HttpURLConnection connection) throws IOException{
    InputStream is = wasSuccessful()? connection.getInputStream() : 
                                      connection.getErrorStream();
    
    return readStream(is);
  }
  
  String readStream(InputStream is) throws IOException{
    final char[] buffer = new char[0x10000];
    StringBuilder out = new StringBuilder();
    Reader in = new InputStreamReader(is, "UTF-8");
    int read;

    do {
      read = in.read(buffer, 0, buffer.length);
      if (read > 0) {
        out.append(buffer, 0, read);
      }
    } while (read >= 0);
    return out.toString();
  }
  
  private Map<String, String> parseHeaders(HttpURLConnection conn){
    Map<String,String> headers = new HashMap<String, String>();
    for(String key : conn.getHeaderFields().keySet()){
      headers.put(key, conn.getHeaderFields().get(key).get(0));
    }
    return headers;
  }
  
  private boolean wasSuccessful(){
    return code >= 200 && code < 400;
  }
  
  public String getBody(){
    return body;
  }
  
  public int getCode(){
    return code;
  }
  
  public Map<String, String> getHeaders(){
    return headers;
  }
  
  public String getHeader(String name){
    return headers.get(name);
  }

}
