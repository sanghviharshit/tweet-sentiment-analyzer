package com.hps.sentimentweet;

import org.apache.log4j.Logger;

import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

/**
 * 
 */

/**
 * @author hps
 * 
 */
public class TweetGetUtil {

	private ConfigurationBuilder cb;
	private Configuration conf;
	private TwitterStream twitterStream;
	private StatusListener listener;
	final static Logger logger = Logger.getLogger(TweetGetUtil.class);
	
	public TweetGetUtil() {
		cb = new ConfigurationBuilder();

		cb.setDebugEnabled(true)
				.setOAuthConsumerKey("d6ojgJdrVFJIJ21gFglfXrPZi")
				.setOAuthConsumerSecret(
						"sYfqv0G255lpRr9dF6SFLuW5oSMYB1e8pgj8uCVYvS0GMAf2Gl")
				.setOAuthAccessToken(
						"26041939-lckTCNpJbaOAhqi4yHrIQQYwD5RfZzLawM0l2iJYH")
				.setOAuthAccessTokenSecret(
						"iYABJyfmlBBTGIUfS2t4IX2Y7CTSnDZF7yQ2fqQMKnqNV");

		conf = cb.build();
		twitterStream = new TwitterStreamFactory(conf).getInstance();

	}

	/**
	 * @return the listener
	 */
	public StatusListener getListener() {
		return listener;
	}

	/**
	 * @param listener
	 *            the listener to set
	 */
	public void setListener(StatusListener listener) {
		twitterStream.removeListener(this.listener);
		this.listener = listener;
		twitterStream.addListener(this.listener);
	}

	public void startSample() {
		twitterStream.sample();
	}

}
