package com.cerner.common.kafka;

import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

/**
 * Created by mw010351 on 5/3/19.
 */
public class FoodSelfieHashTagSerializer implements Serializer<FoodSelfieHashTag> {

  @Override
  public void configure(Map<String, ?> map, boolean b) {

  }

  @Override
  public byte[] serialize(String s, FoodSelfieHashTag foodSelfie) {
    return new byte[0];
  }

  @Override
  public void close() {

  }
}
