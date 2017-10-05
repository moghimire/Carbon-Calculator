package com.carboncalculator.forms;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class WelcomePage extends JPanel {

	String userNameS = "";
	String passWS = "";

	JTextField userName;
	JPasswordField pW;

	JPanel loginInfoPanel;

	/**
	 * It contains Welcome Text, Login Panel with (username, password text
	 * fields and submit button) and Sign Up Page Link
	 * 
	 * Gridbag layout is used as layout
	 */
	public WelcomePage(MainFrame mFrame, Connection SRCN) {
		JLabel welcomeLabel = new JLabel("Welcome To Carbon Calculator", JLabel.CENTER);
		welcomeLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 40));
		this.add(welcomeLabel);

		loginInfoPanel = new JPanel();
		JLabel loginLabel = new JLabel("SIGN IN", JLabel.CENTER);
		loginLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
		userName = new JTextField();
		pW = new JPasswordField();
		JButton loginButton = new JButton("LOGIN");

		JLabel forgotPWLabel = new JLabel("Forgot Password", JLabel.LEFT);
		forgotPWLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 10));

		JLabel createNewLabel = new JLabel("Don't Have Account Then Click Here To Sign Up!", JLabel.CENTER);
		createNewLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));

		createNewLabel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				// mainPanel.getC
				try {
					mFrame.updatePanel("WELCOME_PAGE", "SIGNUP_PAGE");
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

		pW.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					login(mFrame, SRCN);
				}
			}

		});

		GridBagLayout gbLayout = new GridBagLayout();

		loginInfoPanel.setLayout(gbLayout);
		loginInfoPanel.setBackground(Color.LIGHT_GRAY);

		GridBagConstraints gbc = new GridBagConstraints();

		gbc.insets = new Insets(2, 2, 2, 2);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 0;
		loginInfoPanel.add(loginLabel, gbc);

		// gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 6;
		loginInfoPanel.add(userName, gbc);

		// gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 2;
		loginInfoPanel.add(pW, gbc);

		// gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.gridwidth = 1;
		loginInfoPanel.add(forgotPWLabel, gbc);

		gbc.gridx = 3;
		gbc.gridy = 3;
		loginInfoPanel.add(loginButton, gbc);

		loginButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				login(mFrame, SRCN);
			}
		});

		this.add(loginInfoPanel);
		this.add(createNewLabel);

		this.setLayout(new GridLayout(3, 1));
	}

	/**
	 * This function is invoked when user click the login button or press the
	 * ENTER key When user is sucessfully login then it refreshes the mainFrame
	 * by calling mFrame.updatePanel("WELCOME_PAGE", "CLIENTVIEW_PAGE");
	 */
	private void login(MainFrame mFrame, Connection SRCN) {

		userNameS = userName.getText().trim();
		passWS = pW.getText().trim();

		if (userNameS.isEmpty() || passWS.isEmpty()) {
			JOptionPane.showMessageDialog(null, "Username and Password Field Shouldn't be empty.", "Carbon Calculator",
					JOptionPane.WARNING_MESSAGE);

		} else {
			System.out.println(userNameS + " " + passWS);

			try {
				String sql = "select count(*) cnt from user where name=? and password= ?";

				PreparedStatement pstmt = SRCN.prepareStatement(sql);
				pstmt.setString(1, userNameS);
				pstmt.setString(2, passWS);
				System.out.println(sql);
				ResultSet rs = pstmt.executeQuery();
				if (rs.next()) {
					if (rs.getInt(1) == 1) {
						MainFrame.isLogin = true;
						MainFrame.userName = userNameS;
					} else {
						MainFrame.isLogin = false;
						JOptionPane.showMessageDialog(null, "Your Username and Password Doesn't Match.",
								"Carbon Calculator", JOptionPane.WARNING_MESSAGE);
					}
				}
				if (MainFrame.isLogin) {
					sql = "UPDATE user set active = ? where name = ?";
					System.out.println(sql);
					pstmt = SRCN.prepareStatement(sql);
					pstmt.setBoolean(1, true);
					pstmt.setString(2, MainFrame.userName);
					pstmt.executeUpdate();
					mFrame.updatePanel("WELCOME_PAGE", "CLIENTVIEW_PAGE");

				}
				pstmt.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

	}
}
