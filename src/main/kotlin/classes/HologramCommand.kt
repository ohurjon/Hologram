package kr.ohurjon.plugin.hologram.classes

import kr.ohurjon.plugin.hologram.HologramPlugin.Companion.hologramManager
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter

class HologramCommand : CommandExecutor, TabCompleter {
    override fun onCommand(commandSender: CommandSender?, command: Command?, label: String?, args: Array<out String>?): Boolean {
        if(commandSender?.isOp == true && command?.name == "hologram" && args?.isNotEmpty() == true){
            if(args[0] == "create")
                if(!hologramManager.existHologram(args[1])) {
                    val hologram = hologramManager.createHologram(args[1])
                    hologram.create()
                    commandSender.sendMessage("홀로그램 ${args[1]}이 생성되었습니다!")
                } else {
                    commandSender.sendMessage("홀로그램이 이미 존재합니다.")
                    return false
                }
            if(args[0] == "edit")
                if(!hologramManager.editHologram(args[1], fullArg(2, args.toList()), null)) {
                    commandSender.sendMessage("홀로그램이 존재하지 않습니다.")
                    return false
                }
            if(args[0] == "editLine") {
                val lineSpace = args[2].toDoubleOrNull()
                if (lineSpace == null) {
                    commandSender.sendMessage("값이 존재하지 않거나 유효하지 않은 간격입니다.")
                    return false
                } else {
                    if(!hologramManager.editHologramLineSpace(args[1], lineSpace)) {
                        commandSender.sendMessage("홀로그램이 존재하지 않습니다.")
                        return false
                    }
                }
            }
            if(args[0] == "delete") {
                if(!hologramManager.deleteHologram(args[1])) {
                    commandSender.sendMessage("홀로그램이 존재하지 않습니다.")
                    return false
                }
            }
            if(args[0] == "reload") {
                hologramManager.reload()
            }
        }
        return false
    }

    private fun fullArg(n : Int, args: List<String>) : String{
        var text : String = args[n]
        for (i in n+1 until args.size)
            text += (" " + args[n])
        return text
    }

    override fun onTabComplete(
        commandSender: CommandSender?,
        command: Command?,
        label: String?,
        args: Array<out String>?
    ): MutableList<String> {
        if(commandSender?.isOp == true && command?.name == "hologram"){
            if (args != null) {
                if(args.isEmpty()) {
                    return mutableListOf("creat", "edit", "editLine")
                } else {
                    if(args.size == 1) {
                        return hologramManager.hologramNameList()
                    }
                }
            }
        }
        return mutableListOf()
    }

}