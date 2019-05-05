package com.cerner.common.kafka;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;

import java.util.Map;


public class SelfiePartitioner implements Partitioner {
  @Override
  public int partition(String topics, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
    return 0;
  }

  @Override
  public void close() {

  }

  @Override
  public void configure(Map<String, ?> map) {

  }
}
