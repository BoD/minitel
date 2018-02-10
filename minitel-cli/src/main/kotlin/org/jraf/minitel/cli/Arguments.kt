/*
 * This source is part of the
 *      _____  ___   ____
 *  __ / / _ \/ _ | / __/___  _______ _
 * / // / , _/ __ |/ _/_/ _ \/ __/ _ `/
 * \___/_/|_/_/ |_/_/ (_)___/_/  \_, /
 *                              /___/
 * repository.
 *
 * Copyright (C) 2018 Benoit 'BoD' Lubek (BoD@JRAF.org)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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