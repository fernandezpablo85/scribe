package org.scribe;

import java.util.*;

import org.scribe.http.*;
import org.scribe.http.Request.*;
import org.scribe.oauth.*;
import org.scribe.providers.*;

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
  
  public String getRequestToken(){
    Request request = getRTRequest();
    OAuthSigner signer = getOAuthSigner();
    signer.signForRequestToken(request, config.getProperty(CALLBACK_URL));
    provider.tuneRequest(request, CallType.REQUEST_TOKEN);
    Response response = request.send();
    return response.getBody();
  }
  
  private Request getRTRequest() {
    return new Request(Verb.valueOf(config.getProperty(REQUEST_TOKEN_VERB)), config.getProperty(REQUEST_TOKEN_URL));
  }

  private OAuthSigner getOAuthSigner() {
    return new OAuthSigner(config.getProperty(CONSUMER_KEY),config.getProperty(CONSUMER_SECRET),provider);
  }

  public String getAccessToken(String requestToken, String tokenSecret, String verifier){
    Request request = getATRequest();
    OAuthSigner signer = getOAuthSigner();
    signer.signForAccessToken(request, requestToken, tokenSecret, verifier);
    provider.tuneRequest(request, CallType.ACCESS_TOKEN);
    Response response = request.send();
    return response.getBody();
  }

  private Request getATRequest() {
    return new Request(Verb.valueOf(config.getProperty(ACCESS_TOKEN_VERB)), config.getProperty(ACCESS_TOKEN_URL));
  }
  
  public void signRequest(Request request, String token, String tokenSecret){
    OAuthSigner signer = getOAuthSigner();
    signer.sign(request, token, tokenSecret);
    provider.tuneRequest(request, CallType.RESOURCE);
  }
}