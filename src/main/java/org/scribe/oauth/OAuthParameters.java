package org.scribe.oauth;

import java.util.*;

import org.scribe.encoders.*;

public class OAuthParameters {

  Map<String, String> params = new HashMap<String, String>();
  
  public OAuthParameters(String consumerKey){
    initDefaultParams(consumerKey);
  }
  
  private void initDefaultParams(String consumerKey){
    params.put(OAuth.TIMESTAMP,   getTimestampInSeconds());
    params.put(OAuth.SIGN_METHOD, "HMAC-SHA1");
    params.put(OAuth.VERSION,     "1.0");
    params.put(OAuth.NONCE,       MD5.hexHash(getTimestampInSeconds()));
    params.put(OAuth.CONSUMER_KEY, consumerKey);
  }
  
  String getTimestampInSeconds(){
    return String.valueOf(System.currentTimeMillis() / 1000);
  }
  
  public String asSortedFormEncodedString(){
    StringBuffer buffer = new StringBuffer();
    for(String key : sortedKeys())
      buffer.append(URL.percentEncode(key)).append('=').append(URL.percentEncode(params.get(key))).append('&');
    return removeLast(buffer,"&");
  }
  
  private List<String> sortedKeys(){ 
    LinkedList<String> keys = new LinkedList<String>(params.keySet());
    Collections.sort(keys);
    return keys;
  }

  private String removeLast(StringBuffer buffer, String tail){
    String result = buffer.toString();
    return result.substring(0,result.length()-tail.length());
  }
  
  public String asOAuthHeader() {
    StringBuffer buffer = new StringBuffer();
    buffer.append("OAuth ");
    for(String key : sortedKeys()){
      if(key.startsWith(OAuth.PARAM_PREFIX))
        buffer.append(key).append('=').append('"').append(URL.percentEncode(params.get(key))).append("\", ");
    }
    return removeLast(buffer,", ");
  }
  
  public void put(String key, String value){
    params.put(key, value);
  }
}