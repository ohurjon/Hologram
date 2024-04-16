package kr.ohurjon.plugin.hologram

import com.google.gson.GsonBuilder
import kr.ohurjon.plugin.hologram.classes.HologramCommand
import kr.ohurjon.plugin.hologram.classes.HologramConfig
import kr.ohurjon.plugin.hologram.classes.HologramManager
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.io.FileReader
import java.nio.file.Files
import java.nio.file.StandardOpenOption

class HologramPlugin : JavaPlugin() {
    companion object {
        lateinit var hologramManager: HologramManager
        lateinit var hologramPlugin: HologramPlugin
    }

    override fun onEnable() {
        super.onEnable()

        hologramPlugin = this
        hologramManager = HologramManager("hologram.json")

        getCommand("hologram").executor = HologramCommand()
    }

    override fun onDisable() {
        hologramManager.saveHologramFile()
    }

}