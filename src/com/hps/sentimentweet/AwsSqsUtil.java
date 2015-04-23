package com.hps.sentimentweet;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.*;

import java.util.List;

import org.apache.log4j.Logger;

/**
 * Created by IntelliJ IDEA. User: Niraj Singh Date: 3/19/13 Time: 10:44 AM To
 * change this template use File | Settings | File Templates.
 */
public class AwsSqsUtil {
	private AWSCredentials credentials;
	private AmazonSQS sqs;
	private String simpleQueue = "TweetQueue";
	private static volatile AwsSqsUtil awssqsUtil = new AwsSqsUtil();
	final static Logger logger = Logger.getLogger(AwsSqsUtil.class);

	/**
	 * instantiates a AmazonSQSClient
	 * http://docs.aws.amazon.com/AWSJavaSDK/latest
	 * /javadoc/com/amazonaws/services/sqs/AmazonSQSClient.html Currently using
	 * BasicAWSCredentials to pass on the credentials. For SQS you need to set
	 * your regions endpoint for sqs.
	 */
	private AwsSqsUtil() {
		this.credentials = new ProfileCredentialsProvider("default")
				.getCredentials();
		this.sqs = new AmazonSQSClient(this.credentials);

		/**
		 * 
		 * Overrides the default endpoint for this client
		 * ("sqs.us-east-1.amazonaws.com")
		 */
		// this.sqs.setEndpoint("https://sqs.ap-southeast-1.amazonaws.com");
		/**
		 * You can use this in your web app where AwsCredentials.properties is
		 * stored in web-inf/classes
		 */
		// AmazonSQS sqs = new AmazonSQSClient(new
		// ClasspathPropertiesFileCredentialsProvider());

	}

	public static AwsSqsUtil getInstance() {
		return awssqsUtil;
	}

	public AmazonSQS getAWSSQSClient() {
		return awssqsUtil.sqs;
	}

	public String getQueueName() {
		return awssqsUtil.simpleQueue;
	}

	/**
	 * Creates a queue in your region and returns the url of the queue
	 * 
	 * @param queueName
	 * @return
	 */
	public String createQueue(String queueName) {
		CreateQueueRequest createQueueRequest = new CreateQueueRequest(
				queueName);
		String queueUrl = this.sqs.createQueue(createQueueRequest)
				.getQueueUrl();
		return queueUrl;
	}

	/**
	 * returns the queueurl for for sqs queue if you pass in a name
	 * 
	 * @param queueName
	 * @return
	 */
	public String getQueueUrl(String queueName) {
		GetQueueUrlRequest getQueueUrlRequest = new GetQueueUrlRequest(
				queueName);
		return this.sqs.getQueueUrl(getQueueUrlRequest).getQueueUrl();
	}

	/**
	 * lists all your queue.
	 * 
	 * @return
	 */
	public ListQueuesResult listQueues() {
		return this.sqs.listQueues();
	}

	/**
	 * send a single message to your sqs queue
	 * 
	 * @param queueUrl
	 * @param message
	 */
	public void sendMessageToQueue(String queueUrl, String message) {
		SendMessageResult messageResult = this.sqs
				.sendMessage(new SendMessageRequest(queueUrl, message));
		logger.info("Sent: " + messageResult.getMessageId());
	}

	/**
	 * gets messages from your queue
	 * 
	 * @param queueUrl
	 * @return
	 */
	public List<Message> getMessagesFromQueue(String queueUrl) {
		ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(
				queueUrl);
		List<Message> messages = sqs.receiveMessage(receiveMessageRequest)
				.getMessages();
		return messages;
	}

	/**
	 * deletes a single message from your queue.
	 * 
	 * @param queueUrl
	 * @param message
	 */
	public void deleteMessageFromQueue(String queueUrl, Message message) {
		String messageRecieptHandle = message.getReceiptHandle();
		sqs.deleteMessage(new DeleteMessageRequest(queueUrl,
				messageRecieptHandle));
		logger.info("message processed: " + message.getMessageId());

	}
}