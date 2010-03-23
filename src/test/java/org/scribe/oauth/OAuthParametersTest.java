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
    String encodedString = "oauth_consumer_key=any&oauth_nonce=827ccb0eea8a706c4c34a16891f84e7b&oauth_signature_method=HMAC-SHA1&oauth_timestamp=12345&oauth_version=1.0&other+param=bar&q=foo";
    assertEquals(encodedString, params.asSortedFormEncodedString());
  }
  
  @Test
  public void shouldReturnOAuthHeader(){
    String header = "OAuth oauth_consumer_key=\"any\", oauth_nonce=\"827ccb0eea8a706c4c34a16891f84e7b\", oauth_signature_method=\"HMAC-SHA1\", oauth_timestamp=\"12345\", oauth_version=\"1.0\"";
    assertEquals(header,params.asOAuthHeader());
  }
  
}
