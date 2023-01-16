#!/bin/bash
# check-kafka-topics-created.sh

apt-get update -y

yes | apt-get install kafkacat

kafkacatResult=$(kafkacat -L -b kafka-broker-1:9092)

echo $kafkacatResult

while [[ ! $kafkacatResult == *"twitter-topic"* ]]; do
    echo "kafka topic has not been created yet..."
    sleep 2
    kafkacatResult=$(kafkacat -L -b kafka-broker-1:9092)
done

./usr/local/bin/check-config-server-started.sh