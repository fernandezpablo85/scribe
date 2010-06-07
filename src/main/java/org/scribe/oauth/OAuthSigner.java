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

import org.scribe.encoders.*;
import org.scribe.eq.*;
import org.scribe.http.*;

class OAuthSigner {

  private final String consumerSecret;
  private final OAuthParameters params;
  private final DefaultEqualizer eq;

  public OAuthSigner(String consumerKey, String consumerSecret, DefaultEqualizer eq){
    this.consumerSecret = consumerSecret;
    this.eq = eq;
    this.params = new OAuthParameters(consumerKey);
  }
  
  public void signForRequestToken(Request request, String callback){
    params.put(OAuth.CALLBACK, callback);
    
    String toSign = getStringToSign(request, CallType.REQUEST_TOKEN);
    String oAuthHeader = getOAuthHeader(request, toSign, OAuth.EMPTY_TOKEN_SECRET, CallType.REQUEST_TOKEN);
    
    request.addHeader(OAuth.HEADER, oAuthHeader);
  }
  
  public void signForAccessToken(Request request, Token requestToken, String verifier){
    params.put(OAuth.TOKEN, requestToken.getToken());
    params.put(OAuth.VERIFIER, verifier);
    
    String toSign = getStringToSign(request, CallType.ACCESS_TOKEN);
    String oAuthHeader = getOAuthHeader(request, toSign, requestToken.getSecret(), CallType.ACCESS_TOKEN);
    
    request.addHeader(OAuth.HEADER, oAuthHeader);
  }
  
  public void sign(Request request, Token accessToken){
    params.put(OAuth.TOKEN, accessToken.getToken());
   
    String toSign = getStringToSign(request, CallType.RESOURCE);
    String oAuthHeader = getOAuthHeader(request, toSign, accessToken.getSecret(), CallType.RESOURCE);
    request.addHeader(OAuth.HEADER, oAuthHeader);
  }

  public String getOAuthHeader(Request request, String toSign, String tokenSecret, CallType type){    
    String signature = getSignature(toSign, tokenSecret);
    params.put(OAuth.SIGNATURE, signature);
    return eq.tuneOAuthHeader(request, params.asOAuthHeader(),type);
  }
  
  public String getSignature(String toSign, String tokenSecret){
	  return HMAC.sign(toSign, URL.percentEncode(consumerSecret) + '&' + URL.percentEncode(tokenSecret));
  }
  
  public String getStringToSign(Request request, CallType type){
    String verb = URL.percentEncode(request.getVerb().name());
    String url = URL.percentEncode(request.getSanitizedUrl());
    addQueryStringParams(request);
    addBodyParams(request);
    String sortedParams = URL.percentEncode(params.asSortedFormEncodedString());
    return eq.tuneStringToSign(request, String.format("%s&%s&%s", verb, url, sortedParams), type);
  }
  
  private void addQueryStringParams(Request request){
    for(Map.Entry<String, String> entry : request.getQueryStringParams()){
      params.put(entry.getKey(), entry.getValue());
    }
  }
  
  private void addBodyParams(Request request){
    for(Map.Entry<String, String> entry : request.getBodyParams()){
      params.put(entry.getKey(), entry.getValue());
    }
  }
}