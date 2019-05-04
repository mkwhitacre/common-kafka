package com.cerner.common.kafka;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

/**
 * Created by mw010351 on 5/3/19.
 */
public class FoodSelfieHashTagDeserializer implements Deserializer<FoodSelfieHashTag> {

  @Override
  public void configure(Map<String, ?> map, boolean b) {

  }

  @Override
  public FoodSelfieHashTag deserialize(String s, byte[] bytes) {
    return new FoodSelfieHashTag("#blessed");
  }

  @Override
  public void close() {

  }
}
