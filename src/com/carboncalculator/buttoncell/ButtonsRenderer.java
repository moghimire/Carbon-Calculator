package com.carboncalculator.buttoncell;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class ButtonsRenderer extends ButtonsPanel implements TableCellRenderer {
	@Override
	public void updateUI() {
		super.updateUI();
		setName("Table.cellRenderer");
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		this.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
		return this;
	}
}