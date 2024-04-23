package top.ctong.plugin.toggleboolean.action

import com.intellij.openapi.util.Ref
import com.intellij.openapi.util.TextRange
import com.intellij.util.Processor

class ProcessorHandler(private var lineRange: TextRange, private var minimumRange: Ref<TextRange>) :
    Processor<TextRange> {

    /**
     * @param t consequently takes value of each element of the set this processor is passed to for processing.
     * @return `true` to continue processing or `false` to stop.
     */
    override fun process(range: TextRange): Boolean {
        if (lineRange.contains(range) && lineRange != range) {
            if (minimumRange.get().contains(range)) {
                minimumRange.set(range)
                return true
            }
        }
        return false
    }
}