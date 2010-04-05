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
package org.scribe.specs;

import org.junit.*;
import org.scribe.http.*;
import org.scribe.http.Request.*;
import org.scribe.oauth.*;

public class TwitterSpec extends Spec{

  private static final String TWITTER_PROPERTIES = "twitter.properties";
  private static final String AUTHORIZE_URL = "https://twitter.com/oauth/authorize?oauth_token=";
  private static final String PROTECTED_RESOURCE_URL = "http://api.twitter.com/1/account/verify_credentials.xml";
  
  Scribe scribe;
  
  @Before
  public void setup(){
    scribe = createFrom(TWITTER_PROPERTIES);
  }
  
  @Test @Ignore
  public void getRequestToken(){
    
    Token requestToken = scribe.getRequestToken();
    
    System.out.println("Got the Request Token:");
    System.out.println(requestToken);
    System.out.println("Go and authorize it here:");
    System.out.println(AUTHORIZE_URL + requestToken.getToken());
  }
  
  @Test @Ignore
  public void getAccessToken(){
    
    //You got this token in the previous step.
    Token requestToken = new Token("token", "secret");
    
    //You got the verifier after authenticating the request token.
    String verifier = "verifier";
    Token accessToken = scribe.getAccessToken(requestToken, verifier);
    
    System.out.println("You've exchanged the request token for an access token:");
    System.out.println(accessToken);
  }
  
  @Test @Ignore
  public void makeSignedCall(){

    //The protected resource.
    Request request = new Request(Verb.GET, PROTECTED_RESOURCE_URL);
    
    //You got this token in the previous step.
    Token accessToken = new Token("token", "secret");
    
    //Sign and send the request.
    scribe.signRequest(request, accessToken);
    Response response = request.send();
    
    System.out.println("Request executed, got:");
    System.out.println(response.getBody());
  }
}
