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

import com.beust.jcommander.JCommander
import org.jraf.libticker.message.BasicMessageQueue
import org.jraf.libticker.plugin.api.PluginConfiguration
import org.jraf.libticker.plugin.manager.PluginManager
import org.jraf.minitel.lib.util.escaping.ACCENT
import org.jraf.minitel.lib.util.escaping.CLEAR_SCREEN_AND_HOME
import org.jraf.minitel.lib.util.escaping.COLOR_FOREGROUND_7
import org.jraf.minitel.lib.util.escaping.CharacterSize
import org.jraf.minitel.lib.util.escaping.ESC
import org.jraf.minitel.lib.util.escaping.HIDE_CURSOR
import org.jraf.minitel.lib.util.escaping.SHOW_CURSOR
import org.jraf.minitel.lib.util.escaping.escapeAccents
import org.jraf.minitel.lib.util.escaping.escapeHtml
import org.jraf.minitel.lib.util.escaping.escapeSpecialChars
import org.jraf.minitel.lib.util.escaping.getWidth
import org.jraf.minitel.lib.util.escaping.moveCursor
import java.io.BufferedWriter
import java.io.Writer
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

fun main(av: Array<String>) {
    val arguments = Arguments()
    val jCommander = JCommander.newBuilder()
        .addObject(arguments)
        .build()
    jCommander.parse(*av)

    if (arguments.help) {
        jCommander.usage()
        return
    }

    val messageQueue = BasicMessageQueue(50)
    PluginManager(messageQueue)
        .addPlugins(
            "org.jraf.libticker.plugin.datetime.DateTimePlugin" to PluginConfiguration().apply {
                put("dateLocale", "fr")
            },
            "org.jraf.libticker.plugin.frc.FrcPlugin" to null,
            "org.jraf.libticker.plugin.weather.WeatherPlugin" to PluginConfiguration().apply {
                put("apiKey", arguments.weatherApiKey)
                put("formattingLocale", "fr")
            },
            "org.jraf.libticker.plugin.btc.BtcPlugin" to null,
            "org.jraf.libticker.plugin.twitter.TwitterPlugin" to PluginConfiguration().apply {
                put("oAuthConsumerKey", arguments.twitterOAuthConsumerKey)
                put("oAuthConsumerSecret", arguments.twitterOAuthConsumerSecret)
                put("oAuthAccessToken", arguments.twitterOAuthAccessToken)
                put("oAuthAccessTokenSecret", arguments.twitterOAuthAccessTokenSecret)
            }
        )
        .start()

    BufferedWriter(System.out.writer()).use {
        while (true) {
            var message = messageQueue.next

            if (message != null) {
                println(message)

                it += HIDE_CURSOR
                it += CLEAR_SCREEN_AND_HOME

                val width = message.escapeHtml()
                    .escapeSpecialChars()
                    .escapeAccents()
                    .getWidth(CharacterSize.NORMAL)

                val characterSize =
                    if (width < CharacterSize.DOUBLE.maxCharactersHorizontal) CharacterSize.DOUBLE else CharacterSize.TALL

                message = message.escapeHtml(COLOR_FOREGROUND_7, characterSize.characterSizeEscape)
                    .escapeSpecialChars()
                    .escapeAccents()

                message = message.wrapAndCenter(characterSize)

                val linesHeight = message.linesHeight(characterSize)
                val y = characterSize.maxCharactersVertical - linesHeight / 2 + 1
                it += moveCursor(0, y)

                it += characterSize.characterSizeEscape

                it += message

                it += SHOW_CURSOR

                it.flush()
            }
            Thread.sleep(TimeUnit.SECONDS.toMillis(12))
        }
    }
}

private operator fun Writer.plusAssign(s: String) = write(s)

private operator fun Writer.plusAssign(c: Char) = write(c.toString())

private fun String.wrapAndCenter(characterSize: CharacterSize): String {
    val lines = mutableListOf<String>()
    var line = ""
    var lastSpaceIdx = -1
    var i = 0
    while (i < length) {
        val c = this[i]
        when (c) {
            ESC, ACCENT -> {
                line += c
                line += this[i + 1]
                i += 2
            }

            ' ' -> {
                lastSpaceIdx = i
                line += c
                i++
            }

            else -> {
                line += c
                if (line.getWidth(characterSize) > CharacterSize.NORMAL.maxCharactersHorizontal) {
                    line = line.substringBeforeLast(' ')
                    lines += line
                    line = ""
                    i = lastSpaceIdx + 1
                } else {
                    i++
                }
            }
        }
    }
    lines += line
    val newLine = when (characterSize) {
        CharacterSize.TALL, CharacterSize.DOUBLE -> "\n\n"
        else -> "\n"
    }
    var res = ""
    for ((index, l) in lines.withIndex()) {
        val paddingWidth =
            ((CharacterSize.NORMAL.maxCharactersHorizontal - l.getWidth(characterSize)) / 2) / characterSize.characterWidth
        var paddedLine = l
        (1..paddingWidth).forEach { _ -> paddedLine = ' ' + paddedLine }
        res += paddedLine
        if (paddedLine.getWidth(characterSize) < CharacterSize.NORMAL.maxCharactersHorizontal && index < lines.lastIndex) {
            res += newLine
        }
    }
    return res
}

private fun String.linesHeight(characterSize: CharacterSize): Int {
    val split = split(Pattern.compile("\n+"))
    var res = 0
    for (s in split) {
        if (s.getWidth(characterSize) > CharacterSize.NORMAL.maxCharactersHorizontal) {
            res += characterSize.characterHeight * 2
        } else {
            res += characterSize.characterHeight
        }
    }
    return res
}