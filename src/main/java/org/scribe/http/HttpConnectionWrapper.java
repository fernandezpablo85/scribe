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

import org.scribe.http.Request.*;

/**
 * Wrapper for {@link HttpURLConnection}s.
 * 
 * @author Pablo Fernandez
 */
public class HttpConnectionWrapper {

  /**
   * Returns a new {@link Request}, created form the parameter.
   * 
   * This class is useful to integrate <em>Scribe</em> with existing applications.
   * 
   * @param connection to wrap
   * @return wrapper
   */
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
