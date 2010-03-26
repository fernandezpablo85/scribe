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
package org.scribe.encoders;

import javax.crypto.*;
import javax.crypto.spec.*;

import org.apache.commons.codec.binary.*;

/**
 * An utility class for HMAC-SHA1 signature methods.  
 * 
 * @author Pablo Fernandez
 */
public class HMAC {

  private static final String UTF8 = "UTF-8";
  private static final String HMAC_SHA1 = "HmacSHA1";
  
  /**
   * Creates a Base64-encoded MAC-SHA1 for a given string.
   *     
   * @param String to sign
   * @param Key used to sign the string (usually a shared secret)
   * @return HMAC-SHA1 base64-encoded signature
   * @throws RuntimeException if the signature cannot be calculated.
   */
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
    return new String(Base64.encodeBase64(bytes)).replace("\r\n", "");
  }
}
