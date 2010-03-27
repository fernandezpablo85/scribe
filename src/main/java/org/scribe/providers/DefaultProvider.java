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
package org.scribe.providers;

import java.util.regex.*;

import org.scribe.http.*;
import org.scribe.oauth.*;


/**
 * Default OAuth provider.
 * 
 * The methods defined here are hooks for subclasses to override. They do nothing by default.
 * 
 * @author Pablo Fernandez
 */
public class DefaultProvider {
  
  private static final String TOKEN_REGEX = "oauth_token=(\\S*)&oauth_token_secret=(\\S*?)(&(.*))?";  

  /**
   * Hook that lets you modify the string to sign before the HMAC-SHA1 signature is calculated.
   * 
   * @param Request 
   * @param String to sign
   * @param the call type
   * @return The modified string to sign
   */
  public String tuneStringToSign(Request request, String toSign, CallType type){
    return toSign;
  }
  
  /**
   * Hook that lets you modify the OAuth header beofre adding it to the {@link Request}
   * 
   * @param Request
   * @param The OAuth header
   * @param the call type
   * @return the modified OAuth header
   */
  public String tuneOAuthHeader(Request request, String oAuthHeader, CallType type){
    return oAuthHeader;
  }
  
  /**
   * Hook that lets you modify the Response object before sending it.
   * 
   * @param Request
   * @param the call type
   */
  public void tuneRequest(Request request, CallType type){}
  
  public Token parseRequestTokens(String response){
    return parseTokens(response);
  }
  
  public Token parseAccessTokens(String response){
    return parseTokens(response);
  }
  
  private Token parseTokens(String response){
    Matcher matcher = Pattern.compile(TOKEN_REGEX).matcher(response);
    if(matcher.matches()){
      return new Token(matcher.group(1), matcher.group(2));
    }else{
      throw new RuntimeException("Could not find request token or secret in response: " + response);
    }  
  }
}
