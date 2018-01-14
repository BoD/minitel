package org.jraf.minitel.cli

import com.beust.jcommander.JCommander
import org.jraf.minitel.lib.util.escaping.CLEAR_SCREEN_AND_HOME
import org.jraf.minitel.lib.util.escaping.SPECIAL_CHAR_à
import org.jraf.minitel.lib.util.escaping.SPECIAL_CHAR_â
import org.jraf.minitel.lib.util.escaping.SPECIAL_CHAR_è
import org.jraf.minitel.lib.util.escaping.SPECIAL_CHAR_é
import org.jraf.minitel.lib.util.escaping.SPECIAL_CHAR_ê
import org.jraf.minitel.lib.util.escaping.SPECIAL_CHAR_ë
import org.jraf.minitel.lib.util.escaping.SPECIAL_CHAR_î
import org.jraf.minitel.lib.util.escaping.SPECIAL_CHAR_ô
import org.jraf.minitel.lib.util.escaping.SPECIAL_CHAR_ù
import org.jraf.minitel.lib.util.escaping.SPECIAL_CHAR_û
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
                it.write("$CLEAR_SCREEN_AND_HOME")
                it.write("$SPECIAL_CHAR_à\n")
                it.write("$SPECIAL_CHAR_è\n")
                it.write("$SPECIAL_CHAR_ù\n")
                it.write("$SPECIAL_CHAR_é\n")
                it.write("$SPECIAL_CHAR_â\n")
                it.write("$SPECIAL_CHAR_ê\n")
                it.write("$SPECIAL_CHAR_î\n")
                it.write("$SPECIAL_CHAR_ô\n")
                it.write("$SPECIAL_CHAR_û\n")
                it.write("$SPECIAL_CHAR_ë\n")

            }
        }
    }
}