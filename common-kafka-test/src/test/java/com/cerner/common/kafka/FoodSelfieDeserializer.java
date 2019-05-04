package com.cerner.common.kafka;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;


public class FoodSelfieDeserializer implements Deserializer<FoodSelfie> {


  @Override
  public void configure(Map<String, ?> map, boolean b) {

  }

  @Override
  public FoodSelfie deserialize(String s, byte[] bytes) {
    return new FoodSelfie("/img/foo.jpg");
  }

  @Override
  public void close() {

  }
}
