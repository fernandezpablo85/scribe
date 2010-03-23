package org.scribe.providers;

import org.scribe.http.*;
import org.scribe.oauth.*;

public class DefaultProvider {
  
  public String tuneStringToSign(Request request, String toSign, CallType type){
    return toSign;
  }
  
  public String tuneOAuthHeader(Request request, String oAuthHeader, CallType type){
    return oAuthHeader;
  }
  
  public void tuneRequest(Request request, CallType type){}
}
