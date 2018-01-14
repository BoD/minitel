package org.jraf.minitel.cli

import com.beust.jcommander.JCommander
import org.jraf.minitel.lib.util.escaping.CLEAR_SCREEN_AND_HOME
import org.jraf.minitel.lib.util.escaping.COLOR_FOREGROUND_2
import org.jraf.minitel.lib.util.escaping.COLOR_FOREGROUND_4
import org.jraf.minitel.lib.util.escaping.COLOR_FOREGROUND_7
import org.jraf.minitel.lib.util.escaping.SCREEN_HEIGHT
import org.jraf.minitel.lib.util.escaping.SCREEN_WIDTH
import org.jraf.minitel.lib.util.escaping.SIZE_DOUBLE
import org.jraf.minitel.lib.util.escaping.SIZE_WIDE
import org.jraf.minitel.lib.util.escaping.moveCursor
import java.io.BufferedWriter
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.text.SimpleDateFormat
import java.util.Date


class Main {
    companion object {
        @JvmStatic
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

            BufferedWriter(OutputStreamWriter(FileOutputStream("/mnt/n/tmp/tmp"))).use {
                it.write(CLEAR_SCREEN_AND_HOME)
                while (true) {
                    val date = Date()
                    val timePlain = timePlain(date)
                    val timeFormatted = timeFormatted(date)
                    it.write(moveCursor((SCREEN_WIDTH - timePlain.length * 2) / 2, SCREEN_HEIGHT / 2))
                    it.write(SIZE_DOUBLE)
                    it.write(timeFormatted)
                    it.flush()

                    Thread.sleep(1000)
                }
            }
        }

        fun timePlain(date: Date) = SimpleDateFormat("HH:mm:ss").format(date)
        fun timeFormatted(date: Date) = SimpleDateFormat("HH'$COLOR_FOREGROUND_2':'$COLOR_FOREGROUND_7'mm'$COLOR_FOREGROUND_2':'$COLOR_FOREGROUND_4$SIZE_WIDE'ss").format(date)
    }
}