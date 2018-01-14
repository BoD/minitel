package org.jraf.minitel.cli

import com.beust.jcommander.JCommander
import org.jraf.minitel.lib.util.escaping.CLEAR_SCREEN_AND_HOME
import org.jraf.minitel.lib.util.escaping.COLOR_BACKGROUND_0
import org.jraf.minitel.lib.util.escaping.COLOR_BACKGROUND_1
import org.jraf.minitel.lib.util.escaping.COLOR_BACKGROUND_2
import org.jraf.minitel.lib.util.escaping.COLOR_BACKGROUND_3
import org.jraf.minitel.lib.util.escaping.COLOR_BACKGROUND_4
import org.jraf.minitel.lib.util.escaping.COLOR_BACKGROUND_5
import org.jraf.minitel.lib.util.escaping.COLOR_BACKGROUND_6
import org.jraf.minitel.lib.util.escaping.COLOR_BACKGROUND_7
import org.jraf.minitel.lib.util.escaping.COLOR_FOREGROUND_0
import org.jraf.minitel.lib.util.escaping.COLOR_FOREGROUND_1
import org.jraf.minitel.lib.util.escaping.COLOR_FOREGROUND_2
import org.jraf.minitel.lib.util.escaping.COLOR_FOREGROUND_3
import org.jraf.minitel.lib.util.escaping.COLOR_FOREGROUND_4
import org.jraf.minitel.lib.util.escaping.COLOR_FOREGROUND_5
import org.jraf.minitel.lib.util.escaping.COLOR_FOREGROUND_6
import org.jraf.minitel.lib.util.escaping.COLOR_FOREGROUND_7
import org.jraf.minitel.lib.util.escaping.SIZE_WIDE
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
                it.write("$CLEAR_SCREEN_AND_HOME${SIZE_WIDE}")
                it.write("${COLOR_FOREGROUND_0}Hello, World!\n")
                it.write("${COLOR_FOREGROUND_1}Hello, World!\n")
                it.write("${COLOR_FOREGROUND_2}Hello, World!\n")
                it.write("${COLOR_FOREGROUND_3}Hello, World!\n")
                it.write("${COLOR_FOREGROUND_4}Hello, World!\n")
                it.write("${COLOR_FOREGROUND_5}Hello, World!\n")
                it.write("${COLOR_FOREGROUND_6}Hello, World!\n")
                it.write("${COLOR_FOREGROUND_7}Hello, World!\n")
                it.write("\n")
                it.write("${COLOR_BACKGROUND_0}              \n")
                it.write("${COLOR_BACKGROUND_1}              \n")
                it.write("${COLOR_BACKGROUND_2}              \n")
                it.write("${COLOR_BACKGROUND_3}              \n")
                it.write("${COLOR_BACKGROUND_4}              \n")
                it.write("${COLOR_BACKGROUND_5}              \n")
                it.write("${COLOR_BACKGROUND_6}              \n")
                it.write("${COLOR_BACKGROUND_7}              \n")

            }
        }
    }
}