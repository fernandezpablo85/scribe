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

import java.util.*;

import org.scribe.http.*;
import org.scribe.http.Request.*;
import org.scribe.providers.*;

/**
 * The main class. Provides an access point for all OAuth methods.
 * 
 * Is intended to be used as a Singleton, and instantiated with the {@link Scribe#getInstance(Properties)} method.
 * 
 * @author Pablo Fernandez
 */
public class Scribe {

  private static final String PROVIDER = "scribe.provider";
  private static final String REQUEST_TOKEN_URL = "request.token.url";
  private static final String CALLBACK_URL = "callback.url";
  private static final String REQUEST_TOKEN_VERB = "request.token.verb";
  private static final String ACCESS_TOKEN_URL = "access.token.url";
  private static final String ACCESS_TOKEN_VERB = "access.token.verb";
  private static final String CONSUMER_SECRET = "consumer.secret";
  private static final String CONSUMER_KEY = "consumer.key";
  private static Scribe instance;
  
  private final Properties config;
  private DefaultProvider provider;
  
  /**
   * Factory method
   * 
   * @param Scribe properties
   * @return The Scribe single instance
   */
  public static synchronized Scribe getInstance(Properties props){
    if(instance == null)
        instance = new Scribe(props);
    return instance;
  }
  
  private Scribe(Properties props) throws RuntimeException{
    this.config = props;
    initProvider();
  }
  
  private void initProvider(){
    String providerName = config.getProperty(PROVIDER);
    if(isEmpty(providerName))
      this.provider = new DefaultProvider();
    else
      try{
        this.provider = (DefaultProvider) Class.forName(providerName).newInstance();
      }catch(Exception e){
        throw new RuntimeException("Could not initalize Scribe provider",e);
      }
  }
  
  private boolean isEmpty(String string){
    return (string == null || string.trim().length() <= 0);
  }
  
  /**
   * Obtains the request token and token secret.
   * 
   * @return the request token
   */
  public Token getRequestToken(){
    Request request = getRTRequest();
    OAuthSigner signer = getOAuthSigner();
    signer.signForRequestToken(request, config.getProperty(CALLBACK_URL));
    provider.tuneRequest(request, CallType.REQUEST_TOKEN);
    Response response = request.send();
    return provider.parseRequestTokens(response.getBody());
  }
  
  private Request getRTRequest() {
    return new Request(Verb.valueOf(config.getProperty(REQUEST_TOKEN_VERB)), config.getProperty(REQUEST_TOKEN_URL));
  }

  private OAuthSigner getOAuthSigner() {
    return new OAuthSigner(config.getProperty(CONSUMER_KEY),config.getProperty(CONSUMER_SECRET),provider);
  }

  /**
   * Obtains the access token and access token secret.
   * 
   * @param request token
   * @param verifier
   * @return access token
   */
  public Token getAccessToken(Token token, String verifier){
    Request request = getATRequest();
    OAuthSigner signer = getOAuthSigner();
    signer.signForAccessToken(request, token.getToken(), token.getSecret(), verifier);
    provider.tuneRequest(request, CallType.ACCESS_TOKEN);
    Response response = request.send();
    return provider.parseAccessTokens(response.getBody());
  }

  private Request getATRequest() {
    return new Request(Verb.valueOf(config.getProperty(ACCESS_TOKEN_VERB)), config.getProperty(ACCESS_TOKEN_URL));
  }
  
  /**
   * Adds an OAuth header to the {@link Request} 
   * 
   * @param Request to sign
   * @param access token
   */
  public void signRequest(Request request, Token tokens){
    OAuthSigner signer = getOAuthSigner();
    signer.sign(request, tokens.getToken(), tokens.getSecret());
    provider.tuneRequest(request, CallType.RESOURCE);
  }
}