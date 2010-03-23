package org.scribe.encoders;

import javax.crypto.*;
import javax.crypto.spec.*;

import org.apache.commons.codec.binary.*;

public class HMAC {

  private static final String UTF8 = "UTF-8";
  private static final String HMAC_SHA1 = "HmacSHA1";
  
  public static String sign(String toSign, String key){
    try{
      return doSign(toSign,key);
    }catch(Exception e){
      throw new RuntimeException("Error while signing string: " + toSign,e);
    }
  }
  
  private static String doSign(String toSign, String keyString) throws Exception{
    SecretKeySpec key = new SecretKeySpec((keyString).getBytes(UTF8),HMAC_SHA1);
    Mac mac = Mac.getInstance(HMAC_SHA1);
    mac.init(key);
    byte[] bytes = mac.doFinal(toSign.getBytes(UTF8));
    return Base64.encodeBase64String(bytes).replace("\r\n", "");
  }
}
