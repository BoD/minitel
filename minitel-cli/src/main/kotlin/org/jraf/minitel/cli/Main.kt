package org.jraf.minitel.cli

import com.beust.jcommander.JCommander
import org.jraf.minitel.lib.util.escaping.CLEAR_SCREEN_AND_HOME
import org.jraf.minitel.lib.util.escaping.SCREEN_HEIGHT
import org.jraf.minitel.lib.util.escaping.SCREEN_WIDTH
import org.jraf.minitel.lib.util.escaping.moveCursor
import java.io.BufferedWriter
import java.io.FileOutputStream
import java.io.OutputStreamWriter


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
                it.write(moveCursor(0, 0))
                it.write("0,0")
                it.write(moveCursor(1, 1))
                it.write("1,1")
                it.write(moveCursor(2, 2))
                it.write("2,2")
                it.write(moveCursor(0, SCREEN_WIDTH - 1))
                it.write("7")
                it.write(moveCursor(SCREEN_HEIGHT - 1, 0))
                it.write("L")
                it.write(moveCursor(SCREEN_HEIGHT - 1, SCREEN_WIDTH - 1))
                it.write("J")

            }
        }
    }
}