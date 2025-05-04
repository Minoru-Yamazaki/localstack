#!/bin/bash
awslocal s3 mb s3://meu-bucket
awslocal sns create-topic --name topico-1
awslocal sqs create-queue --queue-name fila-1
awslocal sqs create-queue --queue-name fila-2

TOPIC_ARN=$(awslocal sns list-topics --query "Topics[0].TopicArn" --output text)
QUEUE_1_URL=$(awslocal sqs get-queue-url --queue-name fila-1 --output text)
QUEUE_2_URL=$(awslocal sqs get-queue-url --queue-name fila-2 --output text)
QUEUE_1_ARN=$(awslocal sqs get-queue-attributes --queue-url "$QUEUE_1_URL" --attribute-name QueueArn --query "Attributes.QueueArn" --output text)
QUEUE_2_ARN=$(awslocal sqs get-queue-attributes --queue-url "$QUEUE_2_URL" --attribute-name QueueArn --query "Attributes.QueueArn" --output text)

POLICY_1=$(cat <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Principal": { "Service": "sns.amazonaws.com" },
      "Action": "sqs:SendMessage",
      "Resource": "$QUEUE_1_ARN",
      "Condition": {
        "ArnEquals": {
          "aws:SourceArn": "$TOPIC_ARN"
        }
      }
    }
  ]
}
EOF
)

POLICY_2=$(cat <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Principal": { "Service": "sns.amazonaws.com" },
      "Action": "sqs:SendMessage",
      "Resource": "$QUEUE_2_ARN",
      "Condition": {
        "ArnEquals": {
          "aws:SourceArn": "$TOPIC_ARN"
        }
      }
    }
  ]
}
EOF
)

awslocal sqs set-queue-attributes --queue-url "$QUEUE_1_URL" --attributes Policy="$POLICY_1"
awslocal sqs set-queue-attributes --queue-url "$QUEUE_2_URL" --attributes Policy="$POLICY_2"

awslocal sns subscribe --topic-arn "$TOPIC_ARN" --protocol sqs --notification-endpoint "$QUEUE_1_ARN"
awslocal sns subscribe --topic-arn "$TOPIC_ARN" --protocol sqs --notification-endpoint "$QUEUE_2_ARN"
