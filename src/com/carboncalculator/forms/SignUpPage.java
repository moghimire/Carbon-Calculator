package com.carboncalculator.forms;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class SignUpPage extends JPanel {

	String userNameS;
	String pW;
	String cpW;

	Statement stmt;

	/**
	 * SignUp page contains some fields to register the user. Borderlayout is
	 * used
	 */
	public SignUpPage(MainFrame mFrame, Connection SRCN) {

		JPanel signUpForm = new JPanel();

		signUpForm.setLayout(new GridBagLayout());

		JLabel fillUpLabel = new JLabel("Fill Up The Form", JLabel.LEFT);
		fillUpLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 25));

		JLabel unameLabel = new JLabel("Name: ", JLabel.LEFT);
		unameLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));

		JTextField userName = new JTextField(8);

		JLabel pwLabel = new JLabel("Password: ", JLabel.LEFT);
		pwLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));

		JPasswordField pwName = new JPasswordField(8);

		JLabel cpwLabel = new JLabel("Confirm Password: ", JLabel.LEFT);
		cpwLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));

		JPasswordField cpwName = new JPasswordField(8);

		signUpForm.setBackground(Color.LIGHT_GRAY);

		GridBagConstraints gbc = new GridBagConstraints();

		// gbc.insets = new Insets(2, 2, 2, 2);
		// gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.LINE_END;
		gbc.gridx = 0;
		gbc.gridy = 0;
		signUpForm.add(fillUpLabel, gbc);

		gbc.gridy++;
		signUpForm.add(unameLabel, gbc);

		gbc.gridy++;
		signUpForm.add(pwLabel, gbc);

		gbc.gridy++;
		signUpForm.add(cpwLabel, gbc);

		gbc.anchor = GridBagConstraints.LINE_START;

		gbc.gridx = 1;
		gbc.gridy = 1;

		signUpForm.add(userName, gbc);

		gbc.gridy++;
		signUpForm.add(pwName, gbc);

		gbc.gridy++;
		signUpForm.add(cpwName, gbc);

		JButton signUpButton = new JButton("SIGN UP");

		gbc.gridy++;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		signUpForm.add(signUpButton, gbc);

		JLabel pwError = new JLabel("", JLabel.LEFT);
		pwError.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
		pwError.setForeground(Color.RED);

		gbc.gridx = 2;
		gbc.gridy = 3;
		signUpForm.add(pwError, gbc);

		cpwName.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				if (!checkPassword()) {
					pwError.setText("Password Doesn't Match");

				}
			}
		});

		signUpButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				userNameS = userName.getText().trim();
				pW = pwName.getText().trim();
				cpW = cpwName.getText().trim();

				if (!userNameS.isEmpty() && !pW.isEmpty() && !cpW.isEmpty()) {
					try {
						String sql = "INSERT INTO user(name,password,confirmpassword,active) VALUES(?,?,?,?)";
						System.out.println(sql);

						PreparedStatement pstmt = SRCN.prepareStatement(sql);
						pstmt.setString(1, userNameS);
						pstmt.setString(2, pW);
						pstmt.setString(3, cpW);
						pstmt.setBoolean(4, false);

						int row = pstmt.executeUpdate();
						System.out.println("Row Affected: " + row);
						pstmt.close();
						mFrame.updatePanel("SIGNUP_PAGE", "WELCOME_PAGE");

					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}

			}
		});

		this.setLayout(new BorderLayout());
		this.add(signUpForm, BorderLayout.CENTER);
	}

	/**
	 * @return true if the password field and confirm password field contains
	 *         same password else return false
	 */
	private Boolean checkPassword() {
		if (pW == cpW) {
			return true;
		} else
			return false;
	}
}
