package org.jraf.minitel.cli

import com.beust.jcommander.Parameter


class Arguments {
    @Parameter(
        names = ["-h", "--help"],
        description = "Show this help",
        help = true
    )
    var help: Boolean = false

    @Parameter(
        names = ["--weather-api-key"],
        required = true
    )
    var weatherApiKey: String = ""

    @Parameter(
        names = ["--twitter-oauth-consumer-key"],
        required = true
    )
    var twitterOAuthConsumerKey: String = ""

    @Parameter(
        names = ["--twitter-oauth-consumer-secret"],
        required = true
    )
    var twitterOAuthConsumerSecret: String = ""

    @Parameter(
        names = ["--twitter-oauth-access-token"],
        required = true
    )
    var twitterOAuthAccessToken: String = ""

    @Parameter(
        names = ["--twitter-oauth-access-token-secret"],
        required = true
    )
    var twitterOAuthAccessTokenSecret: String = ""
}