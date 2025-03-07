package top.ctong.plugin.toggleboolean.settings

import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.*
import com.intellij.openapi.options.Configurable
import com.intellij.ui.table.JBTable
import com.intellij.util.ui.components.BorderLayoutPanel
import top.ctong.plugin.toggleboolean.utils.BoolCvs
import top.ctong.plugin.toggleboolean.utils.StringUtils
import java.awt.BorderLayout
import javax.swing.*
import javax.swing.table.DefaultTableModel

/**
 * Plugin Configuration
 *
 * @author Clover You
 * @date 2025-03-06 13:52
 */
class BoolCvsConfigurable: Configurable {

    private var mainPanel: JPanel? = null
    private var mappingTable: JTable? = null
    private var tableModel: DefaultTableModel? = null

    private val columnNames = arrayOf("Source", "Target")

    /**
     * Creates new Swing form that enables user to configure the settings.
     * Usually this method is called on the EDT, so it should not take a long time.
     *
     *
     * Also this place is designed to allocate resources (subscriptions/listeners etc.)
     *
     * @return new Swing form to show, or `null` if it cannot be created
     * @see .disposeUIResources
     */
    override fun createComponent(): JComponent? {
        if (mainPanel == null)
            createUI()
        return mainPanel
    }

    /**
     * Creates and initializes the UI components
     */
    private fun createUI() {
        val panel = BorderLayoutPanel()

        // Create the table model with the column names
        tableModel = DefaultTableModel(columnNames, 0)
        // Initialize the JBTable with the table model
        mappingTable = JBTable(tableModel)

        // Add the table to a scroll pane
        val scrollPane = JScrollPane(mappingTable)

        // Create and add toolbar with actions
        val toolbar = createToolbar()
        panel.add(toolbar.component, BorderLayout.NORTH)
        panel.add(scrollPane, BorderLayout.CENTER)

        mainPanel = panel
    }

    /**
     * Creates action toolbar with add/remove buttons
     */
    private fun createToolbar(): ActionToolbar {
        // Create the toolbar with the add button
        val actionGroup = DefaultActionGroup().apply {
            add(createAddAction())
            add(createRemoveAction())
        }

        return  ActionManager.getInstance().createActionToolbar(
            "BoolCvsConfigurable",
            actionGroup,
            true
        )
    }

    /**
     * Creates the "Remove Row" action
     */
    private fun createRemoveAction(): AnAction {
        return object : AnAction("Remove Row", "Remove current selected row", AllIcons.General.Remove) {
            override fun actionPerformed(e: AnActionEvent) {
                val selectedRow = mappingTable?.selectedRow ?: -1
                if (selectedRow == -1) return
                tableModel?.removeRow(selectedRow)
            }

            override fun update(e: AnActionEvent) {
                e.presentation.isEnabled = (mappingTable?.selectedRow ?: -1) != -1
            }
        }
    }

    /**
     * Creates the "Add Row" action
     */
    private fun createAddAction(): AnAction {
        return object : AnAction("Add Row", "Add a new row", AllIcons.General.Add) {
            override fun actionPerformed(e: AnActionEvent) {
                tableModel?.addRow(arrayOf("", "", false))
            }
        }
    }

    /**
     * Gets default word mappings from BoolCvs utility
     */
    private fun getDefaultMappings(): MutableList<MappingRow> {
        return BoolCvs.WORD_RECORDS.map { (key, value) ->
            MappingRow(key, value)
        }.toMutableList()
    }

    /**
     * Gets current mappings from settings, or defaults if empty
     */
    private fun getMappings(): MutableList<MappingRow> {
        val settings = BoolCvsSettings.getInstance()
        return if (settings.mappings.isEmpty()) {
            getDefaultMappings()
        } else {
            settings.mappings
        }
    }

    /**
     * Indicates whether the Swing form was modified or not.
     * This method is called very often, so it should not take a long time.
     *
     * @return `true` if the settings were modified, `false` otherwise
     */
    override fun isModified(): Boolean {
        if (mainPanel == null || mappingTable == null) return false

        val mappings = getMappings()
        val tableModel = mappingTable!!.model as DefaultTableModel

        if (tableModel.rowCount != mappings.size) {
            return true
        }

        for (row in 0 until tableModel.rowCount) {
            val source = tableModel.getValueAt(row, 0) as String
            val target = tableModel.getValueAt(row, 1) as String

            val mapping = mappings[row]
            if (source != mapping.source || target != mapping.target) {
                return true
            }
        }

        return false
    }

    /**
     * Stores the settings from the Swing form to the configurable component.
     * This method is called on EDT upon user's request.
     *
     * @throws ConfigurationException if values cannot be applied
     */
    override fun apply() {
        if (mappingTable == null) return
        val settings = BoolCvsSettings.getInstance()
        val tableModel = mappingTable!!.model as DefaultTableModel
        settings.mappings.clear()

        // Save the data in the table to storage
        for (row in 0 until tableModel.rowCount) {
            val source = tableModel.getValueAt(row, 0) as String
            val target = tableModel.getValueAt(row, 1) as String
            if (StringUtils.isNotBlank(source) && StringUtils.isNotBlank(target))
                settings.mappings.add(MappingRow(source.trim(), target.trim()))
        }
    }

    /**
     * Loads the settings from the configurable component to the Swing form.
     * This method is called on EDT immediately after the form creation or later upon user's request.
     */
    override fun reset() {
        if (mappingTable == null) return
        val mappings = getMappings()
        val tableModel = mappingTable!!.model as DefaultTableModel

        // Clear the current table data
        tableModel.rowCount = 0

        // Reload the data from settings
        mappings.forEach {
            tableModel.addRow(arrayOf(it.source, it.target))
        }
    }

    /**
     * Returns the visible name of the configurable component.
     * Note, that this method must return the display name
     * that is equal to the display name declared in XML
     * to avoid unexpected errors.
     *
     * @return the visible name of the configurable component
     */
    override fun getDisplayName(): String {
        return "Toggle Boolean Settings"
    }

    /**
     * Clean up resources when the UI is no longer needed
     */
    override fun disposeUIResources() {
        mainPanel = null
        mappingTable = null
        tableModel = null
    }
}