package com.hps.sentimentweet;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import twitter4j.JSONException;
import twitter4j.JSONObject;

import com.amazonaws.services.sqs.model.Message;

public class TweetSqsWorker implements Runnable {

	private static String queueUrl;
	private static AwsSqsUtil awssqsUtil;
	private static SentimentAnalysis sa;
	private static AwsSnsSender snsSender;
	final static Logger logger = Logger.getLogger(TweetSqsWorker.class);
	
	public TweetSqsWorker() {
		awssqsUtil = AwsSqsUtil.getInstance();
		queueUrl = awssqsUtil.getQueueUrl(awssqsUtil.getQueueName());
		snsSender = new AwsSnsSender();
		sa = new SentimentAnalysis();
	}

	@Override
	public void run() {

		do {
			boolean flag = true;
			while (flag) {
				List<Message> messages = awssqsUtil
						.getMessagesFromQueue(queueUrl);
				if (messages == null || messages.size() == 0) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} else {
					flag = false;
					for (Message message : messages) {
						String tweetSQSString = message.getBody();
						JSONObject tweetData;
						try {
							tweetData = new JSONObject(tweetSQSString);
							// Extract sentiment for a text string.
							String sentiment = sa.getSentiment(tweetData);
							// Add sentiment to json object
							tweetData.put("sentiment", sentiment);
							// Publish it to SNS topic
							snsSender.publishMessage(tweetData.toString());
						} catch (JSONException e) {
							e.printStackTrace();
						} catch (XPathExpressionException e) {
							e.printStackTrace();
						} catch (SAXException e) {
							e.printStackTrace();
						} catch (ParserConfigurationException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}

					/**
					 * finally delete the message
					 */
					for (Message message : messages) {
						awssqsUtil.deleteMessageFromQueue(queueUrl, message);
					}

				}
			}
		} while (true);
	}

}
