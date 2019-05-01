package com.cerner.common.kafka;


import com.cerner.common.kafka.testing.KafkaBrokerTestHarness;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.KafkaAdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.common.KafkaFuture;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class ExampleTest {

  private static KafkaBrokerTestHarness harness;

  @BeforeClass
  public static void setup() throws IOException {
    harness = new KafkaBrokerTestHarness();
    harness.setUp();
  }

  @AfterClass
  public static void cleanup() throws IOException {


    harness.tearDown();
  }



  @Test
  public void test() throws ExecutionException, InterruptedException {


    Properties producerProps = harness.getProducerProps();


    AdminClient client = AdminClient.create(producerProps);

    String topicName = "iamatopic";
    int numPartitions = 3;
    short numReplicas = 3;

    NewTopic newTopic = new NewTopic(topicName, numPartitions, numReplicas);

    CreateTopicsResult topics = client.createTopics(Collections.singleton(newTopic));

    topics.all().get();


    Producer<String, String> producer = new KafkaProducer<>(producerProps);


  }


  
}
