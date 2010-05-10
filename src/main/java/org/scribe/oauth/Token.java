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
package org.scribe.oauth;

import java.io.*;
/**
 * This class represents an OAuth Token, which is formed by a token
 * and a token_secret.
 * 
 * This can be either a <em>Request Token</em> or an <em>Access Token</em>
 * 
 * @author Pablo Fernandez
 *
 */
public class Token implements Serializable {
  
  private static final long serialVersionUID = -211981113194439605L;
  
  private String token;
  private String secret;
  private String rawString;
  
  /**
   * Default constructor
   * 
   * @param OAuth Token
   * @param OAuth Token Secret
   */
  public Token(String token, String secret){
    this(token, secret, "");
  }
  
  /**
   * Equalizer Constructor
   * 
   * This is used internally to also store the raw response you get from the request/access token request
   * You should probably use the Default Constructor instead.
   * 
   * @param OAuth Token
   * @param OAuth Secret
   * @param Raw Response
   */
  public Token(String token, String secret, String rawString){
    this.token = token;
    this.secret = secret;
    this.rawString = rawString;
  }

  public String getToken() {
    return token;
  }

  public String getSecret() {
    return secret;
  }
  
  public String getRawString(){
    return rawString;
  }
  
  /**
   * Friendly representation of the Token. Do not depend on the format as it may change.
   */
  public String toString(){
    return String.format("oauth_token -> %s oauth_token_secret -> %s",token,secret);
  }
}
