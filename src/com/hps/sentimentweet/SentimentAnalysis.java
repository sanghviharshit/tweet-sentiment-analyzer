package com.hps.sentimentweet;

import com.alchemyapi.api.*;

import org.xml.sax.SAXException;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import twitter4j.JSONException;
import twitter4j.JSONObject;

import java.io.*;
import java.util.Random;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

public class SentimentAnalysis {

	private static AlchemyAPI alchemyObj;

	private static Random randomGenerator;
	final static Logger logger = Logger.getLogger(SentimentAnalysis.class);

	public SentimentAnalysis() {
		// Create an AlchemyAPI object.
		try {
			alchemyObj = AlchemyAPI.GetInstanceFromFile("api_key.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
		// alchemyObj = AlchemyAPI.GetInstanceFromString("");
		randomGenerator = new Random();
	}

	public String getSentiment(JSONObject tweetData) throws SAXException,
			ParserConfigurationException, XPathExpressionException,
			IOException, JSONException {

		String sentiment;

		try {
			Document doc = alchemyObj.TextGetTextSentiment(tweetData
					.getString("text"));
			/*
			 * logger.info("Getting Sentiment for: " +
			 * tweetData.getString("text"));
			 */
			sentiment = getStringFromDocument(doc);
			logger.info(sentiment);
		} catch (IOException ioe) {
			logger.error(ioe);
			/*
			 * @todo check if exception due to daily limit, if yes - send random
			 * sentiment.
			 */
			logger.warn("Sending random sentiment for this tweet");
			int r = randomGenerator.nextInt(100);
			if (r <= 33) {
				sentiment = "positive";
			} else if (r > 33 && r <= 66) {
				sentiment = "negative";
			} else {
				sentiment = "neutral";
			}
		}
		return sentiment;

	}

	// get sentiment from response
	private static String getStringFromDocument(Document doc) {
		NodeList dNodes = doc.getElementsByTagName("docSentiment");
		Node dNode = dNodes.item(0);

		if (dNode.getNodeType() == Node.ELEMENT_NODE) {
			return ((Element) dNode).getElementsByTagName("type").item(0)
					.getTextContent();
		} else {
			return "unknown";
		}
	}
}
