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
import java.io.FileOutputStream
import java.io.OutputStreamWriter
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

    val messageQueue = BasicMessageQueue(40)
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

    BufferedWriter(OutputStreamWriter(FileOutputStream("/mnt/o/tmp/tmp"))).use {
        //    BufferedWriter(System.out.writer()).use {

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

                message = message.wrap(characterSize)

                val linesHeight = message.linesHeight(characterSize)
                val y = characterSize.maxCharactersVertical - linesHeight / 2 + 1
                it += moveCursor(0, y)

                it += characterSize.characterSizeEscape

                it += message

                it += SHOW_CURSOR

                it.flush()
            }
//            Thread.sleep(TimeUnit.SECONDS.toMillis(12))
            Thread.sleep(TimeUnit.SECONDS.toMillis(10))

        }
    }
}

private operator fun Writer.plusAssign(s: String) = write(s)

private operator fun Writer.plusAssign(c: Char) = write(c.toString())

private fun String.wrap(characterSize: CharacterSize): String {
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