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
package org.scribe.eq;

import org.junit.*;
import org.scribe.oauth.*;

public class DefaultEqualizerTest {

  private DefaultEqualizer eq = new DefaultEqualizer();
  
  @Test
  public void shouldProcessDefaultRequestTokenResponse(){
    String response = "oauth_token=f30d5067-ba09-4706-86b1-5bec36d9ffb7&oauth_token_secret=c5e26ad6-c1a9-4c25-8650-b4b098323321&oauth_callback_confirmed=true&xoauth_request_auth_url=https%3A%2F%2Fapi.linkedin.com%2Fuas%2Foauth%2Fauthorize";
    Token pair = eq.parseRequestTokens(response);
    Assert.assertEquals("f30d5067-ba09-4706-86b1-5bec36d9ffb7", pair.getToken());
    Assert.assertEquals("c5e26ad6-c1a9-4c25-8650-b4b098323321", pair.getSecret());
  }
  
  @Test
  public void shouldProcessDefaultAccessTokenResponse(){
    String response = "oauth_token=a0637ba7-2e07-4be1-8fcc-87eee817350e&oauth_token_secret=eb88ed1e-ce87-4b69-9e62-6f57deca8105";
    Token pair = eq.parseAccessTokens(response);
    Assert.assertEquals("a0637ba7-2e07-4be1-8fcc-87eee817350e", pair.getToken());
    Assert.assertEquals("eb88ed1e-ce87-4b69-9e62-6f57deca8105", pair.getSecret());
  }
}
