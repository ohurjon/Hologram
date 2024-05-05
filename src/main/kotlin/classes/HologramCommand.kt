package kr.ohurjon.plugin.hologram.classes

import kr.ohurjon.plugin.hologram.HologramPlugin.Companion.hologramManager
import org.bukkit.Bukkit

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Entity

class HologramCommand : CommandExecutor, TabCompleter {
    override fun onCommand(commandSender: CommandSender, command: Command?, label: String?, args: Array<out String>?): Boolean {
        if(commandSender.isOp && command?.name == "hologram" && args?.isNotEmpty() == true){
            if(args[0] == "create") {
                hologramManager.create(commandSender, args[1])
                return false
            }
            if(args[0] == "edit"){
                val hologram = hologramManager.getSelectedHologram(commandSender.uniqueID())
                if(hologram != null) {
                    commandSender.sendMessage("수정 할 홀로그램을 입력해주세요.")
                    return false
                } else {
                    val text = fullArg(2, args.toList())
                    if (!hologramManager.editText(commandSender,text)) {
                        commandSender.sendMessage("홀로그램을 수정했습니다")
                        return false
                    }
                }
            }
//            if(args[0] == "editLine") {
//                val lineSpace = args[2].toDoubleOrNull()
//                if (lineSpace == null) {
//                    commandSender.sendMessage("값이 존재하지 않거나 유효하지 않은 간격입니다.")
//                    return false
//                } else {
//                    if(!hologramManager.edit(args[1], lineSpace)) {
//                        commandSender.sendMessage("홀로그램이 존재하지 않습니다.")
//                        return false
//                    }
//                }
//            }
//            if(args[0] == "delete") {
//                if(!hologramManager.deleteHologram(args[1])) {
//                    commandSender.sendMessage("홀로그램이 존재하지 않습니다.")
//                    return false
//                }
//            }
            if(args[0] == "reload") {
                hologramManager.reload()
                return true
            }
        }
        return false
    }

    private fun fullArg(n : Int, args: List<String>) : String{
        Bukkit.broadcastMessage(n.toString())
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
            if(args?.size == 1) {
                return mutableListOf("create", "edit", "editLine", "delete","reload")
            }
            if(args?.size!! >= 2) {
                return hologramManager.hologramNameList()
            }
        }
        return mutableListOf()
    }

}