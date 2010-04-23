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
package org.scribe.encoders;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Utility class to perform URL-encoding operations
 * 
 * @author Pablo Fernandez
 */
public class URL {

  private static final String UTF8 = "UTF-8";
  private static final String URL_SPACE_ENCODING = "+";
  private static final String OAUTH_SPEC_SPACE_ENCODING = "%20";
  
  /**
   * Encode a given string as specified in OAuth Spec section 3.6.
   * 
   * @param string to encode
   * @return encoded string
   * @throws RuntimeException if the string cannot be encoded
   */
  public static String percentEncode(String string){
    try{
      return URLEncoder.encode(string,UTF8)
      				   .replace(URL_SPACE_ENCODING, OAUTH_SPEC_SPACE_ENCODING);
    }catch(UnsupportedEncodingException uee){
      throw new RuntimeException("Wrong encoding: " + UTF8);
    }
  }
  
  /**
   * Transforms a {@link Map<String, String>} to a querystring-like (form-url-encoded) string 
   * 
   * The Map keys are the parameter names, and the values the parameter values.
   * 
   * @param parameter map
   * @return Querystring
   */
  public static String queryString(Map<String, String > params){
    return (params.size() <= 0) ? "" : "?" + getFormEncodedString(params);
  }
  
  private static String getFormEncodedString(Map<String, String> params){
    StringBuffer buffer = new StringBuffer();
    for(Map.Entry<String, String> entry : params.entrySet()){
      buffer.append(percentEncode(entry.getKey()))
            .append('=')
            .append(percentEncode(entry.getValue()))
            .append('&');
    }
    String query = buffer.toString();
    return query.substring(0, query.length() - 1);
  }
}
