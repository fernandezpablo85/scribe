package org.scribe.oauth;

import java.util.Map;

import org.scribe.http.Request;

public class RequestInspector {

  public static String getJsonInfo(OAuthSigner signer, Request request, Token accessToken) {
    StringBuilder info = new StringBuilder();
    // Main
    info.append("{");
    String toSign = signer.getStringToSign(request, CallType.RESOURCE);
    addField("stringToSign", toSign, info);
    addField("signature", signer.getSignature(toSign, accessToken.getSecret()), info);

    // HttpRequest
    info.append("\"httpRequest\":{");
    addField("verb", request.getVerb().name(), info);
    addField("url", request.getUrl(), info);
    addField("body", request.getBodyContents(), info);

    // HttpRequestHeaders
    info.append("\"headers\":[");
    for (Map.Entry<String, String> entry : request.getHeaders().entrySet())
      addObject(entry.getKey(), entry.getValue().replace("\"", "'"), info);

    info.deleteCharAt(info.length() - 2); // WTF: Remove trailing ','
    info.append("]");
    info.append("}");
    info.append("}");
    return info.toString();
  }

  private static final void addField(String key, String val, StringBuilder info) {
    info.append("\"" + key + "\":\"" + val + "\"");
    info.append(",");
  }

  private static final void addObject(String key, String val, StringBuilder info) {
    info.append("{");
    addField(key, val, info);
    info.append("}");
  }
}
