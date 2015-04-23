# SentimenTweetAnalyzer
### Twitter Stream Listener, Sentiment Analyzer, SNS Publisher

1. The program listens to random sample of all public statuses using twitter streaming API.
2. It adds the tweets containing location information to Amazon SQS for processing. 
3. Another thread periodically retrieves these tweets from SQS for sentiment analysis on the text of these tweets.
4. It uses Alchemy API for sentiment analysis.
  
  > Note: add 'api_key.txt' with your API key from Alchemy.
5. It publishes the tweet's text and location along with the sentiment to all the endpoints subscribed to AWS SNS topic.
