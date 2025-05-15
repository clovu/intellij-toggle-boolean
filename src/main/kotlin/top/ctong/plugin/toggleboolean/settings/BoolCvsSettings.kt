package top.ctong.plugin.toggleboolean.settings

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil
import top.ctong.plugin.toggleboolean.utils.BoolCvs
import kotlin.collections.component1
import kotlin.collections.component2

/**
 * Custom toggle map
 *
 * @author Clover You
 * @date 2025-03-06 13:45
 */
@State(name = "ToggleBoolean", storages = [Storage("toggle-boolean.xml")])
class BoolCvsSettings : PersistentStateComponent<BoolCvsSettings> {

    var mappings: MutableList<MappingRow> = getDefaultMappings()

    /**
     * @return a component state. All properties, public and annotated fields are serialized.
     * Only values which differ from the default (i.e. the value of newly instantiated class) are serialized.
     * `null` value indicates that the returned state won't be stored, as a result previously stored state will be used.
     * @see com.intellij.util.xmlb.XmlSerializer
     */
    override fun getState(): BoolCvsSettings {
        return this
    }

    /**
     * This method is called when a new component state is loaded.
     * The method can and will be called several times if config files are externally changed while the IDE is running.
     *
     *
     * State object should be used directly, defensive copying is not required.
     *
     * @param state loaded component state
     * @see com.intellij.util.xmlb.XmlSerializerUtil.copyBean
     */
    override fun loadState(state: BoolCvsSettings) {
        XmlSerializerUtil.copyBean(state, this)
    }

    /**
     * Gets default word mappings from BoolCvs utility
     */
    fun getDefaultMappings(): MutableList<MappingRow> {
        return BoolCvs.WORD_RECORDS.map { (key, value) ->
            MappingRow(key, value)
        }.toMutableList()
    }

    companion object {
        fun getInstance(): BoolCvsSettings {
            return com.intellij.openapi.application.ApplicationManager.getApplication().getService(BoolCvsSettings::class.java)
        }
    }
}

data class MappingRow(
    var source: String = "",
    var target: String = "",
    var mutual: Boolean = false
)