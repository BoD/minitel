package org.jraf.minitel.cli

import com.beust.jcommander.JCommander
import org.apache.commons.text.WordUtils
import org.jraf.libticker.message.BasicMessageQueue
import org.jraf.libticker.plugin.api.PluginConfiguration
import org.jraf.libticker.plugin.manager.PluginManager
import org.jraf.minitel.lib.util.color.AwtColor
import org.jraf.minitel.lib.util.escaping.CLEAR_SCREEN_AND_HOME
import org.jraf.minitel.lib.util.escaping.COLOR_FOREGROUND_7
import org.jraf.minitel.lib.util.escaping.HIDE_CURSOR
import org.jraf.minitel.lib.util.escaping.SCREEN_WIDTH
import org.jraf.minitel.lib.util.escaping.SHOW_CURSOR
import org.jraf.minitel.lib.util.escaping.SIZE_TALL
import org.jraf.minitel.lib.util.escaping.colorForeground
import org.jraf.minitel.lib.util.escaping.escapeAccents
import org.jraf.minitel.lib.util.escaping.moveCursor
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import org.xml.sax.InputSource
import java.io.BufferedWriter
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.io.StringReader
import java.io.Writer
import java.util.concurrent.TimeUnit
import javax.xml.parsers.DocumentBuilderFactory

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

                val documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
                val document = documentBuilder.parse(InputSource(StringReader("<root>$message</root>")))
                message = asMinitelText(document.childNodes)

                val textWidth = message.length * 2
                message = message.wrap(SCREEN_WIDTH)
                it += moveCursor(0, 1)

                it += SIZE_TALL + message.escapeAccents()

                it += SHOW_CURSOR

                it.flush()
            }
            Thread.sleep(TimeUnit.SECONDS.toMillis(10))
//            Thread.sleep(TimeUnit.SECONDS.toMillis(4))

        }
    }
}

private fun asMinitelText(nodeList: NodeList): String {
    var res = ""
    for (i in 0 until nodeList.length) {
        val node = nodeList.item(i)
        res += when (node.nodeType) {
            Node.TEXT_NODE -> node.textContent

            else -> {
                val element = node as Element
                when (node.nodeName) {
                    "font" -> {
                        val color = element.getAttribute("color")
                        colorForeground(AwtColor.decode(color)) + asMinitelText(node.childNodes) + COLOR_FOREGROUND_7
                    }

                    else -> asMinitelText(node.childNodes)
                }
            }
        }
    }
    return res
}

private operator fun Writer.plusAssign(s: String) {
    write(s)
}

private fun String.escapeSpecialChars() = replace("฿", " btc")
    .replace("€", " eur")

private fun String.wrap(wrapLength: Int): String {
    val wrapped = WordUtils.wrap(this, wrapLength)
    var res = ""
    val split = wrapped.split('\n')
    for ((index, line) in split.withIndex()) {
        res += line
        if (line.length < wrapLength && index < split.lastIndex) res += "\n\n"
    }
    return res
}