package com.carboncalculator.buttoncell;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.EventObject;
import java.util.Objects;

import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableCellEditor;

/*
 * implements table celleditor to edit the cell of the table to include the buttonpanel
 * **/
public class ButtonsEditor extends ButtonsPanel implements TableCellEditor {

	protected transient ChangeEvent changeEvent;
	private final JTable table;

	private class EditingStopHandler extends MouseAdapter implements ActionListener {
		@Override
		public void mousePressed(MouseEvent e) {
			Object o = e.getSource();
			if (o instanceof TableCellEditor) {
				actionPerformed(null);
			} else if (o instanceof JButton) {
				ButtonModel m = ((JButton) e.getComponent()).getModel();
				if (m.isPressed() && table.isRowSelected(table.getEditingRow()) && e.isControlDown()) {
					setBackground(table.getBackground());
				}
			}
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			EventQueue.invokeLater(() -> fireEditingStopped());
		}
	}

	public ButtonsEditor(JTable table, String pageName) {
		super();
		this.table = table;
		buttons.get(0).setAction(new EditAction(table));
		buttons.get(1).setAction(new DeleteAction(table));

		EditingStopHandler handler = new EditingStopHandler();
		for (JButton b : buttons) {
			b.setActionCommand(pageName);
			b.addMouseListener(handler);
			b.addActionListener(handler);
		}
		addMouseListener(handler);
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		this.setBackground(table.getSelectionBackground());
		return this;
	}

	@Override
	public Object getCellEditorValue() {
		return "";
	}

	// Copied from AbstractCellEditor
	// protected EventListenerList listenerList = new EventListenerList();
	// protected transient ChangeEvent changeEvent;
	@Override
	public boolean isCellEditable(EventObject e) {
		return true;
	}

	@Override
	public boolean shouldSelectCell(EventObject anEvent) {
		return true;
	}

	@Override
	public boolean stopCellEditing() {
		fireEditingStopped();
		return true;
	}

	@Override
	public void cancelCellEditing() {
		fireEditingCanceled();
	}

	@Override
	public void addCellEditorListener(CellEditorListener l) {
		listenerList.add(CellEditorListener.class, l);
	}

	@Override
	public void removeCellEditorListener(CellEditorListener l) {
		listenerList.remove(CellEditorListener.class, l);
	}

	public CellEditorListener[] getCellEditorListeners() {
		return listenerList.getListeners(CellEditorListener.class);
	}

	protected void fireEditingStopped() {
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();
		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == CellEditorListener.class) {
				// Lazily create the event:
				if (Objects.isNull(changeEvent)) {
					changeEvent = new ChangeEvent(this);
				}
				((CellEditorListener) listeners[i + 1]).editingStopped(changeEvent);
			}
		}
	}

	protected void fireEditingCanceled() {
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();
		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == CellEditorListener.class) {
				// Lazily create the event:
				if (Objects.isNull(changeEvent)) {
					changeEvent = new ChangeEvent(this);
				}
				((CellEditorListener) listeners[i + 1]).editingCanceled(changeEvent);
			}
		}
	}

}