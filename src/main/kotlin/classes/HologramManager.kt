package kr.ohurjon.plugin.hologram.classes

import com.google.gson.GsonBuilder
import kr.ohurjon.plugin.hologram.HologramPlugin.Companion.hologramPlugin
import org.bukkit.Location
import java.io.File
import java.io.FileReader
import java.nio.file.Files
import java.nio.file.StandardOpenOption

class HologramManager(file: String) {
    private val gson = GsonBuilder().setPrettyPrinting().create()
    private val configFile = File(hologramPlugin.dataFolder, file)

    private val list : HashMap<String, Hologram> = HashMap()

    init {
        loadHologramFile()
    }

    fun existHologram(name: String) : Boolean {
        return list.contains(name)
    }
    fun createHologram(name : String) : Hologram{
        if(!list.keys.contains(name))
            list[name] = Hologram(name)
        return list[name]!!
    }

    fun deleteHologram(name: String) : Boolean {
        list[name]?.remove() ?: return false
        list.remove(name)
        return true
    }

    fun editHologram(name: String, text:String, lineSpace: Double?) : Boolean {
        list[name]?.setText(text, lineSpace) ?: return false
        return true
    }

    fun editHologramLineSpace(name: String, lineSpace: Double) : Boolean {
        list[name]?.setLineSpace(lineSpace) ?: return false
        return true
    }

    fun hologramNameList() : MutableList<String>{
        return list.keys.toMutableList()
    }

    fun moveHologram(name: String, location: Location) : Boolean{
        list[name]?.setLocation(location) ?: return false
        return true
    }
    fun load(saveList : Map<String, HologramConfig>){
        for (save in saveList) {
            val hologram = createHologram(save.key)
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
            hologram.value.remove()
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
