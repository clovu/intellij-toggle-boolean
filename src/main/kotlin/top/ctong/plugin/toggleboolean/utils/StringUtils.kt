package top.ctong.plugin.toggleboolean.utils

/**
 * String utils
 *
 * @author Clover You
 * @date 2025-03-07 10:11
 */
object StringUtils {
    private fun isBlank(str: String?): Boolean {
        val strLen = str?.length ?: return true
        if (strLen == 0) return true

        for (i in 0 until strLen) {
            if (!str[i].isWhitespace()) {
                return false
            }
        }
        return true
    }

    fun isNotBlank(str: String?): Boolean {
        return !isBlank(str)
    }
}