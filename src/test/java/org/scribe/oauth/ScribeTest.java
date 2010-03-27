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

import static org.junit.Assert.*;

import java.io.*;
import java.util.*;

import org.junit.*;
import org.scribe.http.*;
import org.scribe.http.Request.*;

public class ScribeTest {

  Scribe scribe;
  
  @Before
  public void setup() throws Exception{
    Properties props = new Properties();
    FileInputStream fis = new FileInputStream("src/main/resources/org/scribe/providers/twitter.properties");
    props.load(fis);
    scribe = Scribe.getInstance(props);  
  }
  
  @Test
  public void shouldRetrieveRequestToken(){
    Token token = scribe.getRequestToken();
    System.out.println(token);
    assertTrue(token.getSecret().length() > 0);
  }
  
  @Test @Ignore
  public void shouldRetrieveAccessToken(){
    //MAKE SURE YOU VALIDATE THE TOKEN IN THE PREVIOUS TEST, AND COPY THE VERIFIER
    Token tokens = new Token("requestToken", "requestTokenSecret");
    Token accessToken = scribe.getAccessToken(tokens, "Verifier");
    System.out.println(accessToken);
    assertTrue(accessToken.toString().length() > 0);
  }
  
  @Test @Ignore
  public void shouldAccessProtectedResource(){
    //MAKE SURE YOU GET THE ACCESS TOKEN IN THE PREVIOUS TEST
    Request request = new Request(Verb.GET, "https://api.linkedin.com/v1/people/~/network?count=50");
    Token token = new Token("accessToken","accessTokenSecret"); 
    scribe.signRequest(request, token);
    Response response = request.send();
    System.out.println(response.getBody());
    assertTrue(response.getCode() == 200);
  }
}