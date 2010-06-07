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
    String encodedString = "oauth_consumer_key=any&oauth_nonce=ee1f212e0ae38d7ccd8b4377e722199d&oauth_signature_method=HMAC-SHA1&oauth_timestamp=12345&oauth_version=1.0";
    assertEqualsIgnoreOAuthNonce(encodedString, params.asSortedFormEncodedString());
  }
  
  @Test
  public void shouldReturnEncodedParams(){
    params.put("q","foo");
    params.put("other param", "bar");
    String encodedString = "oauth_consumer_key=any&oauth_nonce=a4955bf6ae6349f3e0bff8319c53df16&oauth_signature_method=HMAC-SHA1&oauth_timestamp=12345&oauth_version=1.0&other%20param=bar&q=foo";
    assertEqualsIgnoreOAuthNonce(encodedString, params.asSortedFormEncodedString());
  }
  
  @Test
  public void shouldReturnOAuthHeader(){
    String header = "OAuth oauth_consumer_key=\"any\", oauth_nonce=\"4b722ffd811b585b0fbdf98048da65a6\", oauth_signature_method=\"HMAC-SHA1\", oauth_timestamp=\"12345\", oauth_version=\"1.0\"";
    assertEqualsIgnoreOAuthNonce(header,params.asOAuthHeader());
  }

  /**
   * Asserts that the two strings are equal, ignoring any {@code oauth_nonce} parameter.
   * @param expected  expected value
   * @param actual the value to check against {@code expected}
   */
  private static void assertEqualsIgnoreOAuthNonce(String expected, String actual){
    final String regex = "oauth_nonce=\"?[0-9a-fA-F]+\"?";
    String cleanedExpected = expected.replaceAll(regex, "");
    String cleanedActual = actual.replaceAll(regex, "");
    assertEquals(cleanedExpected, cleanedActual);
  }
  
}
