package com.hps.sentimentweet;

import org.apache.log4j.Logger;

/**
 * Tweet Listener and Sentiment Analyzer
 * 
 * @author Harshit Sanghvi
 */
public final class Main {

	final static Logger logger = Logger.getLogger(Main.class);

	/**
	 * Main entry of this application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		TweetGetUtil tweetGetHelper = new TweetGetUtil();
		tweetGetHelper.setListener(new TweetListener());
		tweetGetHelper.startSample();

		Thread sqsWorkerThread = new Thread(new TweetSqsWorker());
		sqsWorkerThread.start();

	}

}