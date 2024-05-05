package kr.ohurjon.plugin.hologram.classes

import com.google.gson.GsonBuilder
import kr.ohurjon.plugin.hologram.HologramPlugin
import kr.ohurjon.plugin.hologram.HologramPlugin.Companion.hologramPlugin
import org.bukkit.Location
import org.bukkit.Server
import org.bukkit.command.CommandSender
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import java.io.File
import java.io.FileReader
import java.nio.file.Files
import java.nio.file.StandardOpenOption
import java.util.UUID

class HologramManager(file: String) {
    private val gson = GsonBuilder().setPrettyPrinting().create()
    private val configFile = File(hologramPlugin.dataFolder, file)

    private val list : HashMap<String, Hologram> = HashMap()
    private val selector : HashMap<UUID, String> = HashMap()

    private val logger = HologramPlugin.hologramLogger

    init {
        loadHologramFile()
    }

    private fun existHologram(name: String?) : Boolean {
        return list.contains(name)
    }

    fun getSelectedHologram(uuid: UUID) : Hologram? {
        return list[selector[uuid]]
    }

    fun create(sender: CommandSender, name: String) : Boolean {
        if(existHologram(name)){
            sender.sendMessage("$name Hologram Already Exists.")
            return true
        }

        val hologram = Hologram(name)

        if(sender is Entity){
            hologram.location = sender.location
            select(sender, hologram.name)
        }

        sender.sendMessage("$name Hologram Created.")

        return true
    }

    fun select(sender : CommandSender, name: String) : Boolean{
        val uniqueId = sender.uniqueID()

        if(selector[uniqueId] == name) {
            sender.sendMessage("Already Selected.")
            return true
        }

        selector[uniqueId] = name
        sender.sendMessage("Select $name Hologram!")
        return true
    }

    fun delete(sender: CommandSender, name: String? = this.selector[sender.uniqueID()]) : Boolean {
        if(!existHologram(name)){
            sender.sendMessage("Not Found Selected Hologram!")
            return true
        }

        selector.remove(sender.uniqueID())
        val hologram = list.remove(name)
        hologram?.removeAll()

        sender.sendMessage("Successfully Removed $name Hologram!")
        return true
    }

    fun editText(sender: CommandSender, text: String, name: String? = this.selector[sender.uniqueID()]) : Boolean {
        if(!existHologram(name)) {
            sender.sendMessage("Not Found Hologram!")
            return true
        }

        val hologram = list[name]!!

        hologram.changeText(text)

        sender.sendMessage("Successfully Edited Text $name Hologram!")

        return true
    }

    fun editLine(sender: CommandSender, lineText: String, linerNumber : Int, name: String? = this.selector[sender.uniqueID()]) : Boolean {
        if(!existHologram(name)){
            sender.sendMessage("Not Found Selected Hologram!")
            return true
        }

        val hologram = list[name]!!

        hologram.changeLine(lineText, linerNumber)

        sender.sendMessage("Successfully Edited Line $lineText Hologram!")
        return true
    }

    fun hologramNameList() : MutableList<String>{
        return list.keys.toMutableList()
    }

    fun load(saveList : Map<String, HologramConfig>){
        for (save in saveList) {
            val hologram = Hologram(save.key)
            val hologramConfig = save.value
            hologram.load(hologramConfig)
        }
    }

    fun reload() {
        saveHologramFile()
        removeAll()
        loadHologramFile()
    }

    fun removeAll() {
        for(hologram in list){
            hologram.value.removeAll()
        }
        list.clear()
    }

    fun loadHologramFile() {
        if (!configFile.exists()) hologramPlugin.saveResource(configFile.name, false);

        val saveList = gson.fromJson(FileReader(configFile),HashMap<String, HologramConfig>()::class.java)

        load(saveList)
    }

    fun saveHologramFile() {
        configFile.delete()
        val json = gson.toJson(list.toList())
        Files.write(configFile.toPath(), json.toByteArray(), StandardOpenOption.CREATE, StandardOpenOption.WRITE)
    }
}


fun CommandSender.uniqueID() : UUID{
    var uniqueId : UUID? = null

    if(this is Server)
        uniqueId = HologramPlugin.hologramServerUUID
    if(this is Entity)
        uniqueId = this.uniqueId

    if(uniqueId == null)
        error("Not Found Unique ID!")

    return uniqueId
}