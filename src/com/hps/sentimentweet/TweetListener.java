package com.hps.sentimentweet;

import org.apache.log4j.Logger;

import twitter4j.JSONException;
import twitter4j.JSONObject;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;

public class TweetListener implements StatusListener {
	private static AwsSqsUtil awssqsUtil;
	private static String queueUrl;
	final static Logger logger = Logger.getLogger(TweetListener.class);

	public TweetListener() {
		awssqsUtil = AwsSqsUtil.getInstance();
		queueUrl = awssqsUtil.getQueueUrl(awssqsUtil.getQueueName());
	}

	@Override
	public void onStatus(Status status) {

		// if(status.getGeoLocation() != null || status.getPlace() != null
		// || !status.getUser().getLocation().equals("")) {
		if (status.getGeoLocation() != null) {
			Double lat = status.getGeoLocation() == null ? null : status
					.getGeoLocation().getLatitude();
			Double lng = status.getGeoLocation() == null ? null : status
					.getGeoLocation().getLongitude();

			/*
			 * @todo get place info or user location (for geolocation) when no
			 * lat, lng String place = status.getPlace() == null ? "" :
			 * status.getPlace() .getFullName(); String userlocation =
			 * status.getUser().getLocation() == null ? "" :
			 * status.getUser().getLocation();
			 */

			if (status.getHashtagEntities().length != 0) {
				logger.info(status.getUser().getScreenName() + ": "
						+ status.getText());
				/*
				 * logger.info("\n\t#Hashtags: ");
				 * 
				 * HashtagEntity hashtags[] = status.getHashtagEntities(); for
				 * (int i = 0; i < hashtags.length; i++) { logger.info("#" +
				 * hashtags[i].getText() + " "); }
				 */
				/*
				 * logger.info("@" + status.getUser().getScreenName() + ": " +
				 * status.getText());
				 */
				/*
				 * logger.info("\t#Place: " + place + "\n\t#Lat:" + lat +
				 * "\t#Lng: " + lng); if (status.getUser().getLocation() !=
				 * null) { logger.info("\t#UserLocation: " + userlocation); }
				 * logger.info("#Date: " + status.getCreatedAt().toString());
				 */
			}

			JSONObject tweetData = new JSONObject();
			try {
				tweetData.put("text", status.getText());
				tweetData.put("lat", lat);
				tweetData.put("lng", lng);

			} catch (JSONException e) {
				e.printStackTrace();
			}

			logger.info(tweetData);
			awssqsUtil.sendMessageToQueue(queueUrl, tweetData.toString());

		}

		/*
		 * @todo find the user's location when tweet doesn't have location info
		 * 
		 * if (status.getUser().isGeoEnabled()) { // logger.info("#User's Geo: "
		 * + // status.getUser().getLocation() ); }
		 */

	}

	@Override
	public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
		// logger.info("Got a status deletion notice id:"
		// + statusDeletionNotice.getStatusId());
	}

	@Override
	public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
		// logger.info("Got track limitation notice:"
		// + numberOfLimitedStatuses);
	}

	@Override
	public void onScrubGeo(long userId, long upToStatusId) {
		// logger.info("Got scrub_geo event userId:" + userId
		// + " upToStatusId:" + upToStatusId);
	}

	@Override
	public void onStallWarning(StallWarning warning) {
		logger.info("Got stall warning:" + warning);
	}

	@Override
	public void onException(Exception ex) {
		ex.printStackTrace();
	}
}
