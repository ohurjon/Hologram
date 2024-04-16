package kr.ohurjon.plugin.hologram.classes

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.LingeringPotion
import org.w3c.dom.Text

class Hologram(private val name : String) {
    private val entityList = ArrayList<ArmorStand>()
    private var text : String = "새로운 홀로그램 $name!"
    private var location : Location = Location(Bukkit.getWorld("world"),0.0,0.0,0.0)
    private var lineSpace = 0.5
    private fun summonEntity(location: Location) : ArmorStand{
        val entity = location.world.spawn(location, ArmorStand::class.java)
        entity.setGravity(false)
        entity.isCustomNameVisible = true
        entity.isVisible = false

        entityList.add(entity)

        return entity
    }

    private fun createText(content: String, location: Location) {
        val armorStand = summonEntity(location)
        armorStand.customName = content
    }

    fun createLineText(content : String, location: Location, lineSpace: Double) {
        val lines = content.split("\n")

        for(line in lines){
            Bukkit.broadcastMessage(line)
            createText(line, location)
            location.y -= lineSpace
        }
    }
    fun load(config: HologramConfig) {
        this.lineSpace = config.lineSpace ?: this.lineSpace
        this.location = config.location ?: this.location
        this.text = config.text ?: "홀로그램 ${name}에게 텍스트 값이 존재하지 않습니다."
        reload()
    }

    fun setLineSpace(lineSpace: Double?) {
        this.lineSpace = lineSpace ?: this.lineSpace
        reload()
    }

    fun setText(text: String?, lineSpace: Double?) {
        this.text = text ?: "홀로그램 ${name}에게 텍스트 값이 존재하지 않습니다."
        this.lineSpace = lineSpace ?: this.lineSpace
        reload()
    }

    fun setLocation(location: Location?){
        this.location = location ?: this.location
        reload()
    }
    fun create() {
        text.replace('&',ChatColor.COLOR_CHAR)
        createLineText(text,location,lineSpace)
    }

    fun reload() {
        remove()
        create()
    }

    fun remove() {
        for( entity in entityList){
            entity.remove()
        }
        entityList.clear()
    }

}