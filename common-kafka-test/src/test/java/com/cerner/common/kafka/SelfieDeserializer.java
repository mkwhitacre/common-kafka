package com.cerner.common.kafka;

import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;


public class SelfieDeserializer implements Deserializer<Selfie> {


  @Override
  public void configure(Map<String, ?> map, boolean b) {

  }

  @Override
  public Selfie deserialize(String s, byte[] bytes) {
    return new Selfie("/img/foo.jpg");
  }

  @Override
  public void close() {

  }
}
