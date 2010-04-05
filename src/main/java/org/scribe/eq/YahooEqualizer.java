package org.scribe.eq;

import org.scribe.http.*;
import org.scribe.oauth.*;

public class YahooEqualizer extends DefaultEqualizer {
  
  @Override
  public String tuneOAuthHeader(Request request, String oAuthHeader, CallType type) {
    return (type == CallType.RESOURCE) ? oAuthHeader.concat(", realm=\"yahooapis.com\"") : oAuthHeader;
  }
  
  @Override
  public String tuneStringToSign(Request request, String toSign, CallType type) {
    return (type == CallType.RESOURCE) ? toSign.concat("%26realm%3Dyahooapis.com") : toSign;
  }

}
