package org.scribe.oauth;

import java.util.*;

import org.scribe.encoders.*;
import org.scribe.http.*;
import org.scribe.providers.*;

public class OAuthSigner {

  private final String consumerSecret;
  private final OAuthParameters params;
  private final DefaultProvider provider;

  public OAuthSigner(String consumerKey, String consumerSecret, DefaultProvider provider){
    this.consumerSecret = consumerSecret;
    this.provider = provider;
    this.params = new OAuthParameters(consumerKey);
  }
  
  public void signForRequestToken(Request request, String callback){
    params.put(OAuth.CALLBACK, callback);
    
    String toSign = getStringToSign(request, CallType.REQUEST_TOKEN);
    String oAuthHeader = getOAuthHeader(request, toSign, OAuth.EMPTY_TOKEN_SECRET, CallType.REQUEST_TOKEN);
    
    request.addHeader(OAuth.HEADER, oAuthHeader);
  }
  
  public void signForAccessToken(Request request, String requestToken, String tokenSecret, String verifier){
    params.put(OAuth.TOKEN, requestToken);
    params.put(OAuth.VERIFIER, verifier);
    
    String toSign = getStringToSign(request, CallType.ACCESS_TOKEN);
    String oAuthHeader = getOAuthHeader(request, toSign, tokenSecret, CallType.ACCESS_TOKEN);
    
    request.addHeader(OAuth.HEADER, oAuthHeader);
  }
  
  public void sign(Request request, String token, String tokenSecret){
    params.put(OAuth.TOKEN, token);
   
    String toSign = getStringToSign(request, CallType.RESOURCE);
    String oAuthHeader = getOAuthHeader(request, toSign, tokenSecret, CallType.RESOURCE);
    request.addHeader(OAuth.HEADER, oAuthHeader);
  }

  public String getOAuthHeader(Request request, String toSign, String tokenSecret, CallType type){    
    String signature = HMAC.sign(toSign, consumerSecret + '&' + tokenSecret);
    params.put(OAuth.SIGNATURE, signature);
    return provider.tuneOAuthHeader(request, params.asOAuthHeader(),type);
  }
  
  public String getStringToSign(Request request, CallType type){
    String verb = URL.percentEncode(request.getVerb().name());
    String url = URL.percentEncode(request.getSanitizedUrl());
    addQueryStringParams(request);
    addBodyParams(request);
    String sortedParams = URL.percentEncode(params.asSortedFormEncodedString());
    return provider.tuneStringToSign(request, String.format("%s&%s&%s", verb, url, sortedParams), type);
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