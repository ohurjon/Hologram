package kr.ohurjon.plugin.hologram

import kr.ohurjon.plugin.hologram.classes.HologramCommand
import kr.ohurjon.plugin.hologram.classes.HologramManager

import org.bukkit.plugin.java.JavaPlugin

import java.util.*
import java.util.logging.Logger

class HologramPlugin : JavaPlugin() {
    companion object {
        lateinit var hologramManager: HologramManager
        lateinit var hologramPlugin: HologramPlugin
        lateinit var hologramLogger: Logger
        val hologramServerUUID : UUID = UUID.fromString("4e2a2e49-09e0-4e1b-8823-81bbc4080b06")
    }

    override fun onEnable() {
        super.onEnable()

        hologramPlugin = this
        hologramLogger = this.logger
        hologramManager = HologramManager("hologram.json")


        getCommand("hologram").executor = HologramCommand()
    }

    override fun onDisable() {
        hologramManager.saveHologramFile()
    }

}