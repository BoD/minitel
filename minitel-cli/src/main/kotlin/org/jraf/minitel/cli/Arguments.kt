package org.jraf.minitel.cli

import com.beust.jcommander.Parameter


class Arguments {
    @Parameter(
            names = arrayOf("-h", "--help"),
            description = "Show this help",
            help = true
    )
    var help: Boolean = false

}