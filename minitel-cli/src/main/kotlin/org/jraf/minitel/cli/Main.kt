package org.jraf.minitel.cli

import com.beust.jcommander.JCommander
import org.apache.commons.text.WordUtils
import org.jraf.libticker.message.BasicMessageQueue
import org.jraf.libticker.plugin.api.PluginConfiguration
import org.jraf.libticker.plugin.manager.PluginManager
import org.jraf.minitel.lib.util.escaping.CLEAR_SCREEN_AND_HOME
import org.jraf.minitel.lib.util.escaping.COLOR_FOREGROUND_7
import org.jraf.minitel.lib.util.escaping.HIDE_CURSOR
import org.jraf.minitel.lib.util.escaping.SCREEN_HEIGHT_TALL
import org.jraf.minitel.lib.util.escaping.SCREEN_WIDTH_TALL
import org.jraf.minitel.lib.util.escaping.SHOW_CURSOR
import org.jraf.minitel.lib.util.escaping.SIZE_TALL
import org.jraf.minitel.lib.util.escaping.escapeAccents
import org.jraf.minitel.lib.util.escaping.escapeHtml
import org.jraf.minitel.lib.util.escaping.escapeSpecialChars
import org.jraf.minitel.lib.util.escaping.moveCursor
import org.jraf.minitel.lib.util.escaping.textOnlyLength
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

    BufferedWriter(OutputStreamWriter(FileOutputStream("/mnt/n/tmp/tmp"))).use {
        //    BufferedWriter(System.out.writer()).use {

        while (true) {
            var message = messageQueue.next

            if (message != null) {
                println(message)

                it += HIDE_CURSOR
                it += CLEAR_SCREEN_AND_HOME

                message = message.escapeHtml(COLOR_FOREGROUND_7, SIZE_TALL)

                message = message.escapeSpecialChars()

                message = message.escapeAccents()

                message = message.wrap(SCREEN_WIDTH_TALL)

                val linesCount = message.lineCount(SCREEN_WIDTH_TALL)
                val y = SCREEN_HEIGHT_TALL - linesCount + 1
                it += moveCursor(0, y)

                it += SIZE_TALL

                it += message

                it += SHOW_CURSOR

                it.flush()
            }
            Thread.sleep(TimeUnit.SECONDS.toMillis(12))
//            Thread.sleep(TimeUnit.SECONDS.toMillis(4))

        }
    }
}

private operator fun Writer.plusAssign(s: String) {
    write(s)
}

private fun String.wrap(wrapLength: Int): String {
    val wrapped = WordUtils.wrap(this, wrapLength)
    var res = ""
    val split = wrapped.split('\n')
    for ((index, line) in split.withIndex()) {
        var paddedLine = line
        for (i in 1..(wrapLength - line.textOnlyLength) / 2) paddedLine = ' ' + paddedLine
        res += paddedLine
        if (paddedLine.textOnlyLength < wrapLength && index < split.lastIndex) res += "\n\n"
    }
    return res
}

private fun String.lineCount(maxLineLength: Int): Int {
    val split = split(Pattern.compile("\n+"))
    var res = 0
    for (s in split) {
        if (s.textOnlyLength > maxLineLength) {
            res += 2
        } else {
            res++
        }
    }
    return res
}