package com.cerner.common.kafka;


import com.cerner.common.kafka.testing.KafkaBrokerTestHarness;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import java.io.IOException;
import java.time.Duration;
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

  @Rule
  public TestName testName = new TestName();


  @Test
  public void testProduce() throws ExecutionException, InterruptedException {


    Properties producerProps = harness.getProducerProps();


    AdminClient client = AdminClient.create(producerProps);

    String topicName = testName.getMethodName();
    int numPartitions = 3;
    short numReplicas = 1;

    Map<String, String> topicConfig = new HashMap<>();
    topicConfig.put("min.insync.replicas", "1");


    NewTopic newTopic = new NewTopic(topicName, numPartitions, numReplicas).configs(topicConfig);

    CreateTopicsResult topics = client.createTopics(Collections.singleton(newTopic));

    topics.all().get();

    producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, SelfieHashTagSerializer.class.getName());
    producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, SelfieSerializer.class.getName());
    producerProps.put(ProducerConfig.ACKS_CONFIG, "-1");
    producerProps.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, SelfiePartitioner.class.getName());



    Producer<SelfieHashTag, Selfie> producer = new KafkaProducer<>(producerProps);

    SelfieHashTag hashTag = new SelfieHashTag("#blessed");
    Selfie selfie = new Selfie("/img/avocado_toast_100.jpg");

    ProducerRecord<SelfieHashTag, Selfie> record = new ProducerRecord<>(topicName, hashTag, selfie);

    Future<RecordMetadata> result = producer.send(record);

    producer.flush();

    RecordMetadata recordMetadata = result.get();

    int partition = recordMetadata.partition();
    long offset = recordMetadata.offset();

    System.out.println("Partition: " + partition + " Offset: " + offset);

    producer.close();


  }


  @Test
  public void testConsume() throws ExecutionException, InterruptedException {


    Properties producerProps = harness.getProducerProps();


    AdminClient client = AdminClient.create(producerProps);

    String topicName = testName.getMethodName();
    int numPartitions = 3;
    short numReplicas = 1;

    Map<String, String> topicConfig = new HashMap<>();
    topicConfig.put("min.insync.replicas", "1");


    NewTopic newTopic = new NewTopic(topicName, numPartitions, numReplicas).configs(topicConfig);

    CreateTopicsResult topics = client.createTopics(Collections.singleton(newTopic));

    topics.all().get();

    producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, SelfieHashTagSerializer.class.getName());
    producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, SelfieSerializer.class.getName());
    producerProps.put(ProducerConfig.ACKS_CONFIG, "-1");
    producerProps.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, SelfiePartitioner.class.getName());


    Producer<SelfieHashTag, Selfie> producer = new KafkaProducer<>(producerProps);

    SelfieHashTag hashTag = new SelfieHashTag("#blessed");
    Selfie selfie = new Selfie("/img/avocado_toast_100.jpg");

    ProducerRecord<SelfieHashTag, Selfie> producedRecord = new ProducerRecord<>(topicName, hashTag, selfie);

    Future<RecordMetadata> result = producer.send(producedRecord);

    producer.flush();

    RecordMetadata recordMetadata = result.get();

    int partition = recordMetadata.partition();
    long offset = recordMetadata.offset();

    System.out.println("Partition: " + partition + " Offset: " + offset);

    producer.close();


    Properties consumerProperties = harness.getConsumerProps();

    consumerProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, SelfieHashTagDeserializer.class.getName());
    consumerProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, SelfieDeserializer.class.getName());
    consumerProperties.put(ConsumerConfig.GROUP_ID_CONFIG, "adoringFans");
    consumerProperties.put(ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG, FairAssignor.class.getName());

    Consumer<SelfieHashTag, Selfie> consumer = new KafkaConsumer<>(consumerProperties);

    consumer.subscribe(Collections.singleton(topicName));


    ConsumerRecords<SelfieHashTag, Selfie> records = consumer.poll(Duration.ofMillis(5000L));

    for(ConsumerRecord<SelfieHashTag, Selfie> record: records){
      System.out.println("Record Partition: " + record.partition() + " offset: " + record.offset());
    }


    consumer.close();
  }
}
