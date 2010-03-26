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

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Represents an HTTP Response.
 * 
 * @author Pablo Fernandez
 */
public class Response {
  
  private static final String EMPTY = "";
  
  private int code;
  private String body;
  private Map<String, String> headers;
  
  
  Response(HttpURLConnection connection) throws IOException{
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
  
  /**
   * Checks if the code is in the successful or redirect range.
   * 
   * @return true if successful
   */
  private boolean wasSuccessful(){
    return code >= 200 && code < 400;
  }
  
  /**
   * Obtains the HTTP Response body
   * 
   * @return response body
   */
  public String getBody(){
    return body;
  }
  
  /**
   * Obtains the HTTP status code
   * 
   * @return the status code
   */
  public int getCode(){
    return code;
  }
  
  /**
   * Obtains a {@link Map} containing the HTTP Response Headers
   * 
   * @return headers
   */
  public Map<String, String> getHeaders(){
    return headers;
  }
  
  /**
   * Obtains a single HTTP Header value, or null if undefined
   * 
   * @param header name
   * @return header value or null
   */
  public String getHeader(String name){
    return headers.get(name);
  }

}
