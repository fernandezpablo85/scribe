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

import java.util.*;

import org.scribe.http.*;
import org.scribe.http.Request.*;
import org.scribe.oauth.*;

public class YahooSpec extends Spec {

  private static final String NETWORK_NAME = "Yahoo";
  private static final String PROPERTIES_FILE = "yahoo.properties";
  private static final String AUTHORIZE_URL = "https://api.login.yahoo.com/oauth/v2/request_auth?oauth_token=";
  private static final String PROTECTED_RESOURCE_URL = "http://social.yahooapis.com/v1/user/A6ROU63MXWDCW3Y5MGCYWVHDJI/profile/status?format=json";

  Scribe scribe;

  public static void main(String[] args) {

    Scribe scribe = createFrom(PROPERTIES_FILE);
    Scanner in = new Scanner(System.in);

    System.out.println("=== " + NETWORK_NAME + "'s OAuth Workflow ===");
    br();

    // Obtain the Request Token
    System.out.println("Fetching the Request Token...");
    Token requestToken = scribe.getRequestToken();
    System.out.println("Got the Request Token!");
    br();

    System.out.println("Now go and authorize Scribe here:");
    System.out.println(AUTHORIZE_URL + requestToken.getToken());
    System.out.println("And paste the verifier here");
    System.out.print(">>");
    String verifier = in.nextLine();
    br();

    // Trade the Request Token and Verfier for the Access Token
    System.out.println("Trading the Request Token for an Access Token...");
    Token accessToken = scribe.getAccessToken(requestToken, verifier);
    System.out.println("Got the Access Token!");
    System.out.println("(if your curious it looks like this: " + accessToken + " )");
    br();

    // Now let's go and ask for a protected resource!
    System.out.println("Now we're going to access a protected resource...");
    Request request = new Request(Verb.GET, PROTECTED_RESOURCE_URL);
    scribe.signRequest(request, accessToken);
    Response response = request.send();
    System.out.println("Got it! Lets see what we found...");
    br();
    System.out.println(response.getBody());

    br();
    System.out.println("Thats it man! Go and build something awesome with Scribe! :)");
  }
}
