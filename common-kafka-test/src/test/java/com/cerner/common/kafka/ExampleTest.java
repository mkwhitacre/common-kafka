package com.cerner.common.kafka;


import com.cerner.common.kafka.testing.KafkaBrokerTestHarness;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

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
    short numReplicas = 1;

    Map<String, String> topicConfig = new HashMap<>();
    topicConfig.put("min.insync.replicas", "1");


    NewTopic newTopic = new NewTopic(topicName, numPartitions, numReplicas).configs(topicConfig);

    CreateTopicsResult topics = client.createTopics(Collections.singleton(newTopic));

    topics.all().get();

    producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, FoodSelfieHashTagSerializer.class.getName());
    producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, FoodSelfieSerializer.class.getName());
    producerProps.put(ProducerConfig.ACKS_CONFIG, "-1");
    producerProps.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, FoodSelfiePartitioner.class.getName());



    Producer<FoodSelfieHashTag, FoodSelfie> producer = new KafkaProducer<>(producerProps);

    FoodSelfieHashTag hashTag = new FoodSelfieHashTag("#blessed");
    FoodSelfie foodSelfie = new FoodSelfie("/img/avocado_toast_100.jpg");

    ProducerRecord<FoodSelfieHashTag, FoodSelfie> record = new ProducerRecord<>(topicName, hashTag, foodSelfie);

    Future<RecordMetadata> result = producer.send(record);

    producer.flush();

    RecordMetadata recordMetadata = result.get();

    int partition = recordMetadata.partition();
    long offset = recordMetadata.offset();

    System.out.println("Partition: " + partition + " Offset: " + offset);


  }


  
}
