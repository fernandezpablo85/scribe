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
package org.scribe.providers;

import org.scribe.http.*;
import org.scribe.oauth.*;

public class LinkedInProvider extends DefaultProvider {

  private static final String TILDE_DECODED = "~";
  private static final String SEPARATOR = "&";
  private static final String TILDE_ENCODED = "%7E";

  @Override
  public String tuneStringToSign(Request request, String toSign, CallType type) {
    if (type != CallType.RESOURCE) {
      return toSign;
    } else {
      return decodeURLTilde(toSign);
    }
  }

  private String decodeURLTilde(String toSign) {
    String toSignParts[] = toSign.split(SEPARATOR);

    String verb = toSignParts[0];
    String url = toSignParts[1];
    String params = toSignParts[2];

    url = url.replace(TILDE_ENCODED, TILDE_DECODED);

    return verb + SEPARATOR + url + SEPARATOR + params;
  }
}
