package kr.ohurjon.plugin.hologram.classes

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.Entity

class Hologram(var name: String) {

    val entityList: ArrayList<ArmorStand> = ArrayList()
    var lineList : ArrayList<String> = arrayListOf("Hologram $name Created!")
    var location : Location = Location(Bukkit.getWorld("world"), 0.0, 0.0, 0.0)
    var lineSpace = 0.5

    constructor(name: String, location: Location) : this(name) {
        this.location = location
    }
    constructor(name: String, entity: Entity) : this(name, entity.location)

    fun create(){
        for(i in 0 until lineList.size) {
            createLine(i)
        }
    }

    fun getHologramName() : String{
        return name
    }

    fun getText() : String {
        var text = lineList[0]

        for(i in  1 until lineList.size){
            text += "\n${lineList[i]}"
        }

        return text
    }

     private fun createLine(line: Int) : ArmorStand {
        val newLocation = location.clone()
        newLocation.y -= lineSpace*line
        val lineEntity = summonEntity(lineList[line], newLocation)
        entityList[line] = lineEntity

        return lineEntity
    }

    private fun summonEntity(content: String, location: Location) : ArmorStand {
        val entity = location.world.spawn(location, ArmorStand::class.java)
        entity.setGravity(false)
        entity.isCustomNameVisible = true
        entity.isVisible = false
        entity.customName = convertColorSign(content)

        return entity
    }

    private fun convertColorSign(text : String) : String{
        return text.replace('&', ChatColor.COLOR_CHAR)
    }

    private fun getLineText(text: String) :  List<String>{
        return convertColorSign(text).split("\n")
    }

    fun load(config: HologramConfig) {
        this.lineSpace = config.lineSpace ?: this.lineSpace
        this.location = config.location ?: this.location
        this.lineList = config.lineList ?: this.lineList
    }

    fun changeText(text: String) {
        lineList.clear()
        getLineText(text).forEach {
            lineList.add(it)
        }
    }

    fun changeLine(lineText: String, lineNumber : Int) {
        lineList[lineNumber] = lineText
    }

    fun changeLineSpace(lineSpace: Double = this.lineSpace) {
        this.lineSpace = lineSpace
    }

    fun changeLocation(location: Location = this.location){
        this.location = location
    }

    fun changeName(name: String = this.name){
        this.name = name
    }

    fun reloadAll() {
        removeAll()
        create()
    }

    fun reload(line : Int) {
        remove(line)
        createLine(line)
    }

    fun remove(line : Int) {
        entityList[line].remove()
    }

    fun removeAll() {
        for(entity in entityList)
            entity.remove()

        entityList.clear()
    }

}