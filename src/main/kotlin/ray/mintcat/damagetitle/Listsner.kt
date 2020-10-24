package ray.mintcat.damagetitle

import io.izzel.taboolib.TabooLibAPI
import io.izzel.taboolib.module.inject.TListener
import io.izzel.taboolib.module.locale.TLocale
import io.izzel.taboolib.util.Strings
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.serverct.ersha.jd.event.AttrEntityDamageEvent
import java.math.RoundingMode
import java.text.DecimalFormat

@TListener
class Listsner:Listener {

    @EventHandler
    fun onAttrEntityDamageEvent(event: AttrEntityDamageEvent){
        val player = event.damager
        if (player !is Player || event.damage <= 0){
            return
        }
        val title = foman(player,DamageTitle.settings.getStringColored("title"," "),event)
        val subTitle = foman(player,DamageTitle.settings.getStringColored("subTitle"," "),event)
        val fadin = DamageTitle.settings.getInt("fadin",1)
        val stay = DamageTitle.settings.getInt("stay",1)
        val fadeout = DamageTitle.settings.getInt("fadeout",1)
        TLocale.Display.sendTitle(player,title,subTitle,fadin,stay,fadeout)

    }

    fun foman(player: Player,string: String,event:AttrEntityDamageEvent) : String{
        return TabooLibAPI.getPluginBridge().setPlaceholders(player,string)
                .replace("{damage}",getNoMoreThanTwoDigits(event.damage))
                .replace("{entity}",(if (event.entity.customName == null){
                    event.entity.name
                }else{
                    event.entity.customName
                }
                        ))
    }
    fun getNoMoreThanTwoDigits(number: Double): String {
        val format = DecimalFormat("0.##")
        //未保留小数的舍弃规则，RoundingMode.FLOOR表示直接舍弃。
        format.roundingMode = RoundingMode.FLOOR
        return format.format(number)
    }
}