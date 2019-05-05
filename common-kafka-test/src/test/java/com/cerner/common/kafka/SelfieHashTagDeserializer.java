package com.cerner.common.kafka;

import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

/**
 * Created by mw010351 on 5/3/19.
 */
public class SelfieHashTagDeserializer implements Deserializer<SelfieHashTag> {

  @Override
  public void configure(Map<String, ?> map, boolean b) {

  }

  @Override
  public SelfieHashTag deserialize(String s, byte[] bytes) {
    return new SelfieHashTag("#blessed");
  }

  @Override
  public void close() {

  }
}
