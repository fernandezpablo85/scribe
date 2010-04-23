package org.scribe.specs;

import java.io.*;
import java.util.*;

import org.scribe.oauth.*;

public abstract class Spec {

  private static final String EQUALIZER_RESOURCES_ROOT = "src/main/resources/org/scribe/eq/";
  
  Scribe createFrom(String properties){
    try{
      Properties props = new Properties();
      FileInputStream fis = new FileInputStream(EQUALIZER_RESOURCES_ROOT + properties);
      props.load(fis);
      return new Scribe(props);
    }catch(IOException ioe){
      throw new RuntimeException("Error while creating the Scribe instance", ioe);
    }
  }
}
