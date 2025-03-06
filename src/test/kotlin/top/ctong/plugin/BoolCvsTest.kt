package top.ctong.plugin

import org.junit.Test
import top.ctong.plugin.toggleboolean.utils.BoolCvs


class BoolCvsTest {

    @Test
    fun shouldReturnNullWhenCannotMatch() {
        assert(BoolCvs.cvt("12") == null) { "if the word does not match, it should return null." }
    }

    @Test
    fun cvt() {
        assert(BoolCvs.cvt("1") == "0") { "match result should be '0'" }
        assert(BoolCvs.cvt("0") == "1") { "match result should be '1'" }
        assert(BoolCvs.cvt("true") == "false") { "match result should be 'false'" }
        assert(BoolCvs.cvt("false") == "true") { "match result should be 'true'" }
        assert(BoolCvs.cvt("on") == "off") { "match result should be 'off'" }
        assert(BoolCvs.cvt("off") == "on") { "match result should be 'on'" }
        assert(BoolCvs.cvt("yes") == "no") { "match result should be 'no'" }
        assert(BoolCvs.cvt("no") == "yes") { "match result should be 'yes'" }
        assert(BoolCvs.cvt("enable") == "disable") { "match result should be 'disable'" }
        assert(BoolCvs.cvt("disable") == "enable") { "match result should be 'enable'" }
        assert(BoolCvs.cvt("enabled") == "disabled") { "match result should be 'disabled'" }
        assert(BoolCvs.cvt("disabled") == "enabled") { "match result should be 'enabled'" }
        assert(BoolCvs.cvt("before") == "after") { "match result should be 'after'" }
        assert(BoolCvs.cvt("after") == "before") { "match result should be 'before'" }
        assert(BoolCvs.cvt("first") == "last") { "match result should be 'last'" }
        assert(BoolCvs.cvt("last") == "first") { "match result should be 'first'" }
    }

    @Test
    fun format() {
        assert(BoolCvs.cvt("faLse") == "true") { "match result should be 'true'" }
        assert(BoolCvs.cvt("True") == "False") { "match result should be 'False'" }
        assert(BoolCvs.cvt("BEFORE") == "AFTER") { "match result should be 'AFTER'" }
        assert(BoolCvs.cvt("OfF") == "On") { "match result should be 'On'" }
    }

    @Test
    fun findMapping() {
        val mappings = mapOf(
            "true" to "false",
            "false" to "true",
            "on" to "off",
            "off" to "on"
        )

        assert(BoolCvs.findMapping("true", mappings) == "false") { "match result should be 'false'" }
        assert(BoolCvs.findMapping("false", mappings) == "true") { "match result should be 'true'" }
        assert(BoolCvs.findMapping("on", mappings) == "off") { "match result should be 'off'" }
        assert(BoolCvs.findMapping("off", mappings) == "on") { "match result should be 'on'" }
        assert(BoolCvs.findMapping("unknown", mappings) == null) { "if the word does not match, it should return null." }
    }

}