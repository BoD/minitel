package org.jraf.minitel.cli

import com.beust.jcommander.JCommander
import org.apache.commons.text.WordUtils
import org.jraf.libticker.message.BasicMessageQueue
import org.jraf.libticker.plugin.api.PluginConfiguration
import org.jraf.libticker.plugin.manager.PluginManager
import org.jraf.minitel.lib.util.escaping.CLEAR_SCREEN_AND_HOME
import org.jraf.minitel.lib.util.escaping.HIDE_CURSOR
import org.jraf.minitel.lib.util.escaping.SCREEN_WIDTH
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
//            "org.jraf.libticker.plugin.frc.FrcPlugin" to null,
//            "org.jraf.libticker.plugin.weather.WeatherPlugin" to PluginConfiguration().apply {
//                put("apiKey", arguments.weatherApiKey)
//            },
//            "org.jraf.libticker.plugin.btc.BtcPlugin" to null,
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
                it += CLEAR_SCREEN_AND_HOME
                it += HIDE_CURSOR

                message = message.escapeSpecialChars()

                message = message.escapeHtml()

                message = message.wrap(SCREEN_WIDTH)

                it += moveCursor(0, 1)

                it += SIZE_TALL + message.escapeAccents()

                it += SHOW_CURSOR

                it.flush()
            }
            Thread.sleep(TimeUnit.SECONDS.toMillis(5))
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