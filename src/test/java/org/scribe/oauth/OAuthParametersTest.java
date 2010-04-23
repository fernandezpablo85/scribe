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

import org.junit.*;

public class OAuthParametersTest {
  
  private OAuthParameters params;
  
  @Before
  public void setup(){
    params = new OAuthParameters("any"){
      String getTimestampInSeconds() {
        return "12345";
      }
    };
  }

  @Test
  public void shouldSetupDefaultParams(){
    String encodedString = "oauth_consumer_key=any&oauth_nonce=827ccb0eea8a706c4c34a16891f84e7b&oauth_signature_method=HMAC-SHA1&oauth_timestamp=12345&oauth_version=1.0";
    assertEquals(encodedString, params.asSortedFormEncodedString());
  }
  
  @Test
  public void shouldReturnEncodedParams(){
    params.put("q","foo");
    params.put("other param", "bar");
    String encodedString = "oauth_consumer_key=any&oauth_nonce=827ccb0eea8a706c4c34a16891f84e7b&oauth_signature_method=HMAC-SHA1&oauth_timestamp=12345&oauth_version=1.0&other%20param=bar&q=foo";
    assertEquals(encodedString, params.asSortedFormEncodedString());
  }
  
  @Test
  public void shouldReturnOAuthHeader(){
    String header = "OAuth oauth_consumer_key=\"any\", oauth_nonce=\"827ccb0eea8a706c4c34a16891f84e7b\", oauth_signature_method=\"HMAC-SHA1\", oauth_timestamp=\"12345\", oauth_version=\"1.0\"";
    assertEquals(header,params.asOAuthHeader());
  }
  
}
