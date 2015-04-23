package com.hps.sentimentweet;

import org.apache.log4j.Logger;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.CreateTopicRequest;
import com.amazonaws.services.sns.model.CreateTopicResult;
import com.amazonaws.services.sns.model.PublishRequest;

// Example SNS Sender
public class AwsSnsSender {

	// AWS credentials -- replace with your credentials
	// static String ACCESS_KEY = "<Your AWS Access Key>";
	// static String SECRET_KEY = "<Your AWS Secret Key>";
	private static AWSCredentials credentials;
	private static AmazonSNSClient snsClient;
	private static CreateTopicRequest createReq;
	private static CreateTopicResult createRes;

	final static Logger logger = Logger.getLogger(AwsSnsSender.class);
	
	// Sender loop
	public AwsSnsSender() {

		try {
			credentials = new ProfileCredentialsProvider("default")
					.getCredentials();

			// Create a client
			snsClient = new AmazonSNSClient(credentials);

		} catch (Exception e) {
			System.out
					.println("exception while creating aws sqs client : " + e);
		}

		// Create a topic
		createReq = new CreateTopicRequest("Sentiments");
		createRes = snsClient.createTopic(createReq);
		// print TopicArn
		logger.info(createRes);
		// get request id for CreateTopicRequest from SNS metadata
		logger.info("CreateTopicRequest - "
				+ snsClient.getCachedResponseMetadata(createReq));

	}

	public void publishMessage(String msg) {
		// Publish to a topic
		PublishRequest publishReq = new PublishRequest().withTopicArn(
				createRes.getTopicArn()).withMessage(msg);
		snsClient.publish(publishReq);

	}
}
