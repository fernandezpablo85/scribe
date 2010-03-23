package org.scribe.http;

import java.io.*;
import java.net.*;

import org.scribe.http.Request.*;

public class HttpConnectionWrapper {

  public static Request wrap(HttpURLConnection connection){
    Request result = new Request(Verb.valueOf(connection.getRequestMethod()), connection.getURL().toExternalForm());
    
    for(String header : connection.getHeaderFields().keySet()){
      result.addHeader(header, connection.getHeaderField(header));
    }
    
    String body = getRequestBody(connection);
    if(body != null && !body.equals("")){
      for(String pair :body.split("&")){ 
        String[] values = pair.split("=");
        result.addBodyParameter(values[0], values[1]);
      }
    } 
    
    return result;
  }
  private static String getRequestBody(HttpURLConnection request){
    try{
      final char[] buffer = new char[0x10000];
      StringBuilder out = new StringBuilder();
      Reader in = new InputStreamReader(request.getInputStream(), "UTF-8");
      int read;
      do {
        read = in.read(buffer, 0, buffer.length);
        if(read>0) {
          out.append(buffer, 0, read);
        }
      } while (read>=0);
      return out.toString();
    }catch(IOException ioe){
      throw new RuntimeException("Failed to access request inputStream",ioe);
    }
  }
}
