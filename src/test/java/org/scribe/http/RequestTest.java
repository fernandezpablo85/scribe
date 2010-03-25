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
package org.scribe.http;

import static org.junit.Assert.*;

import org.junit.*;
import org.scribe.http.Request.*;

public class RequestTest {

  @Test
  public void shouldNotAddProtocolIfPresent(){
    String url = "https://yahoo.secure.com";
    assertEquals(url, new Request(Verb.GET, url).getUrl());
  }
  
  @Test 
  public void shouldSanitizeUrl(){
    Request request = new Request(Verb.GET,"http://somewhere/over/the/rainbow?way=up+high&over=the+hills");
    assertEquals("http://somewhere/over/the/rainbow",request.getSanitizedUrl());
  }
  
  @Test
  public void shouldReturn200WhenRequestIsOk(){
    Response response = new Request(Verb.GET, "http://www.google.com").send();
    assertEquals(200,response.getCode());
    assertTrue(response.getBody().length() > 0);
  }
  
  @Test
  public void shouldWorkWithQueryStringParams(){
    Request request = new Request(Verb.GET, "http://stackoverflow.com/search?q=oauth+authentication&more=stuff");
    assertEquals(2,request.getQueryStringParams().size());
    assertEquals("http://stackoverflow.com/search",request.getSanitizedUrl());
    Response response = request.send();
    assertEquals(200, response.getCode());
    assertTrue(response.getBody().length() > 0);
  }
  
  @Test
  public void shouldReturn404ForUnexistentUrls(){
    Response response = new Request(Verb.GET, "http://xkcd.com/404").send();
    assertEquals(404, response.getCode());
  }
  
  @Test
  public void shouldFollowRedirects(){
    Response response = new Request(Verb.GET, "http://bit.ly/whatsup").send();
    assertEquals(200, response.getCode());
    assertTrue(response.getBody().length() > 0);
  }
}
