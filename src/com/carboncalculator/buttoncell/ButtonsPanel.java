package com.carboncalculator.buttoncell;

import java.util.Arrays;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * Created a Button panel by extending JPanel to hold Edit and Delete Button
 */
class ButtonsPanel extends JPanel {
	public final List<JButton> buttons = Arrays.asList(new JButton("Edit", new ImageIcon("images/edit_client.png")),
			new JButton("Delete", new ImageIcon("images/delete_client.png")));

	protected ButtonsPanel() {
		super();
		setOpaque(true);
		for (JButton b : buttons) {
			b.setFocusable(false);
			b.setRolloverEnabled(false);
			add(b);
		}
	}
}