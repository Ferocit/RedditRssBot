###Reddit RSS Bot

##About

This is a configurable Reddit bot that will read RSS streams, filters them and then will post the news in a subreddit.

##Configuration

Use *config.xml* to configure filters, rss and subreddits.

Create a *credentials.properties* in the src/main/java/resources folder:

    user = UsernameOfBot
    password = PasswordOfBot
    app_id = AppID from Reddit
    app_secret = AppSecret from Reddit
    
##Credits to

* Rome for an easy to use RSS library: http://rometools.github.io/rome/
* JRAW for a Java Reddit API wrapper: https://github.com/thatJavaNerd/JRAW
