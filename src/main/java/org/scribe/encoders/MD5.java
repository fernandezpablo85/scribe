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

import java.security.*;

public class MD5 {
  
  private static final char[] HEX_DIGITS = "0123456789abcdef".toCharArray();
  
  public static String hexHash(Object obj){
    String toHash = obj.toString();
    try{
      MessageDigest dg = MessageDigest.getInstance("MD5");
      dg.update(toHash.getBytes("UTF-8"));
      return bytesToHex(dg.digest());
    }catch(Exception e){
      throw new RuntimeException("Error while hashing string: " + toHash,e);
    }
  }
  
  private static String bytesToHex(byte[] bytes) {
    char[] chars = new char[bytes.length * 2];
    for (int i = 0; i < bytes.length; ++i) {
      int iByte = bytes[i] & 0xff;
      chars[i * 2 + 0] = HEX_DIGITS[iByte >> 4];
      chars[i * 2 + 1] = HEX_DIGITS[iByte & 0x0f];
    }
    return new String(chars);
  }
}
