package com.carboncalculator.utility;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.carboncalculator.forms.ClientViewPage;

public class AddEditClient extends JDialog {
	JButton JBUpdate = new JButton(new ImageIcon("images/save.png"));
	JButton JBReset = new JButton("Reset", new ImageIcon("images/reset.png"));
	JButton JBCancel = new JButton("Cancel", new ImageIcon("images/cancel.png"));

	JLabel JLPic1 = new JLabel();
	JLabel JLBanner = new JLabel("Please fill-up all the required fields.");

	JLabel JLFirstName = new JLabel("First Name:");
	JLabel JLMiddleName = new JLabel("Middle Name:");
	JLabel JLLastName = new JLabel("Last Name:");
	JLabel JLAddr = new JLabel("Address:");
	JLabel JLPostC = new JLabel("PostCode:");
	JLabel JLCity = new JLabel("City:");
	JLabel JLContactNo = new JLabel("Contact No:");
	JLabel JLMobileNo = new JLabel("Mobile No:");
	JLabel JLEmail = new JLabel("Email:");

	JTextField JTFFirstName = new JTextField();
	JTextField JTFMiddleName = new JTextField();
	JTextField JTFLastName = new JTextField();
	JTextField JTFAddr = new JTextField();
	JTextField JTFPostC = new JTextField();
	JComboBox JCBCity;
	JTextField JTFContactNo = new JTextField();
	JTextField JTFMobileNo = new JTextField();
	JTextField JTFEmail = new JTextField();

	Connection cnAEC;
	Statement stAEC;
	ResultSet rsAEC;

	Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();

	boolean ADDING_STATE;

	public AddEditClient(boolean ADD_STATE, JFrame OwnerForm, Connection srcCN, String srcSQL) {
		super(OwnerForm, true);
		cnAEC = srcCN;
		ADDING_STATE = ADD_STATE;
		try {
			stAEC = cnAEC.createStatement();
		} catch (SQLException sqlEx) {
			System.out.println("\nERROR IN frm_add_edit_client(frm_add_edit_client):" + sqlEx + "\n");
		}

		JCBCity = PublicMethods.fillCombo("SELECT * FROM city order by name asc", cnAEC, "Name");

		if (ADD_STATE == true) {
			JLPic1.setIcon(new ImageIcon("images/bNew.png"));
			setTitle("Add New Client");
			JBUpdate.setText("Add");

		} else {
			JLPic1.setIcon(new ImageIcon("images/bModify.png"));
			setTitle("Modify Customer");
			JBUpdate.setText("Save");
			try {
				rsAEC = stAEC.executeQuery(srcSQL);
				rsAEC.next();
				JTFFirstName.setText("" + rsAEC.getString("firstName"));
				JTFMiddleName.setText("" + rsAEC.getString("middleName"));
				JTFLastName.setText("" + rsAEC.getString("lastName"));
				JTFAddr.setText("" + rsAEC.getString("Address"));
				JTFPostC.setText("" + rsAEC.getString("PostCode"));
				JTFContactNo.setText("" + rsAEC.getString("ContactNo"));
				JTFMobileNo.setText("" + rsAEC.getString("Mobile"));
				JTFEmail.setText("" + rsAEC.getString("Email"));

			} catch (SQLException sqlEx) {
				System.out.println(sqlEx.getMessage());
			}
		}
		JPanel JPContainer = new JPanel();
		JPContainer.setLayout(null);
		// -- Add the JLPic1
		JLPic1.setBounds(5, 5, 32, 32);
		JPContainer.add(JLPic1);

		// -- Add the JLBanner
		JLBanner.setBounds(55, 5, 268, 48);
		JLBanner.setFont(new Font("Dialog", Font.PLAIN, 12));
		JLBanner.setForeground(Color.RED);
		JPContainer.add(JLBanner);

		// ******************** Start adding of input field

		int height = 30;
		int heightGap = 32;
		int y = 50;

		// -- Add FirstName Input Field
		JLFirstName.setBounds(5, y, 105, height);
		JLFirstName.setFont(new Font("Dialog", Font.PLAIN, 12));

		JTFFirstName.setBounds(110, y, 200, height);
		JTFFirstName.setFont(new Font("Dialog", Font.PLAIN, 12));

		JPContainer.add(JLFirstName);
		JPContainer.add(JTFFirstName);

		// -- Add Middle Name Input Field
		y += heightGap;
		JLMiddleName.setBounds(5, y, 105, height);
		JLMiddleName.setFont(new Font("Dialog", Font.PLAIN, 12));

		JTFMiddleName.setBounds(110, y, 200, height);
		JTFMiddleName.setFont(new Font("Dialog", Font.PLAIN, 12));

		JPContainer.add(JLMiddleName);
		JPContainer.add(JTFMiddleName);

		// -- Add Last Name Input Field
		y += heightGap;
		JLLastName.setBounds(5, y, 105, height);
		JLLastName.setFont(new Font("Dialog", Font.PLAIN, 12));

		JTFLastName.setBounds(110, y, 200, height);
		JTFLastName.setFont(new Font("Dialog", Font.PLAIN, 12));

		JPContainer.add(JLLastName);
		JPContainer.add(JTFLastName);

		// -- Add Address Title Input Field
		y += heightGap;
		JLAddr.setBounds(5, y, 105, height);
		JLAddr.setFont(new Font("Dialog", Font.PLAIN, 12));

		JTFAddr.setBounds(110, y, 200, height);
		JTFAddr.setFont(new Font("Dialog", Font.PLAIN, 12));

		JPContainer.add(JLAddr);
		JPContainer.add(JTFAddr);

		// -- Add PostCOde Input Field
		y += heightGap;
		JLPostC.setBounds(5, y, 105, height);
		JLPostC.setFont(new Font("Dialog", Font.PLAIN, 12));

		JTFPostC.setBounds(110, y, 200, height);
		JTFPostC.setFont(new Font("Dialog", Font.PLAIN, 12));

		JPContainer.add(JLPostC);
		JPContainer.add(JTFPostC);

		// -- Add City Input Field
		y += heightGap;
		JLCity.setBounds(5, y, 105, height);
		JLCity.setFont(new Font("Dialog", Font.PLAIN, 12));

		JCBCity.setBounds(110, y, 200, height);
		JCBCity.setFont(new Font("Dialog", Font.PLAIN, 12));

		JPContainer.add(JLCity);
		JPContainer.add(JCBCity);

		// -- Add ContactNo Input Field
		y += heightGap;
		JLContactNo.setBounds(5, y, 105, height);
		JLContactNo.setFont(new Font("Dialog", Font.PLAIN, 12));

		JTFContactNo.setBounds(110, y, 200, height);
		JTFContactNo.setFont(new Font("Dialog", Font.PLAIN, 12));

		JPContainer.add(JLContactNo);
		JPContainer.add(JTFContactNo);

		// -- Add Mobile No Input Field
		y += heightGap;
		JLMobileNo.setBounds(5, y, 105, height);
		JLMobileNo.setFont(new Font("Dialog", Font.PLAIN, 12));

		JTFMobileNo.setBounds(110, y, 200, height);
		JTFMobileNo.setFont(new Font("Dialog", Font.PLAIN, 12));

		JPContainer.add(JLMobileNo);
		JPContainer.add(JTFMobileNo);

		// -- Add Email Input Field
		y += heightGap;
		JLEmail.setBounds(5, y, 105, height);
		JLEmail.setFont(new Font("Dialog", Font.PLAIN, 12));

		JTFEmail.setBounds(110, y, 200, height);
		JTFEmail.setFont(new Font("Dialog", Font.PLAIN, 12));

		System.out.println("Y: " + y);

		JPContainer.add(JLEmail);
		JPContainer.add(JTFEmail);

		// ******************** End adding of input field

		// -- Add the JBUpdate
		y += heightGap;

		JBUpdate.setBounds(5, y, 105, 25);
		JBUpdate.setFont(new Font("Dialog", Font.PLAIN, 12));
		JBUpdate.setMnemonic(KeyEvent.VK_A);
		JBUpdate.addActionListener(JBActionListener);
		JBUpdate.setActionCommand("update");
		JPContainer.add(JBUpdate);

		// -- Add the JBReset
		JBReset.setBounds(112, y, 99, 25);
		JBReset.setFont(new Font("Dialog", Font.PLAIN, 12));
		JBReset.setMnemonic(KeyEvent.VK_R);
		JBReset.addActionListener(JBActionListener);
		JBReset.setActionCommand("reset");
		JPContainer.add(JBReset);

		// -- Add the JBCancel
		JBCancel.setBounds(212, y, 99, 25);
		JBCancel.setFont(new Font("Dialog", Font.PLAIN, 12));
		JBCancel.setMnemonic(KeyEvent.VK_C);
		JBCancel.addActionListener(JBActionListener);
		JBCancel.setActionCommand("cancel");
		JPContainer.add(JBCancel);

		getContentPane().add(JPContainer);
		setSize(325, 405);
		setResizable(false);
		setLocation((screen.width - 325) / 2, ((screen.height - 383) / 2));
	}

	private boolean RequiredFieldEmpty() {
		if (JTFFirstName.getText().equals("") || JTFLastName.getText().equals("")) {
			JOptionPane.showMessageDialog(null, "Some required fields is/are empty.\nPlease check it and try again.",
					"Carbon Calculator", JOptionPane.WARNING_MESSAGE);
			JTFFirstName.requestFocus();
			return true;
		} else {
			return false;
		}
	}

	/** reset the fields **/
	private void clearFields() {
		JTFFirstName.setText("");
		JTFMiddleName.setText("");
		JTFLastName.setText("");
		JTFAddr.setText("");
		JTFPostC.setText("");
		JCBCity.setSelectedIndex(0);
		JTFContactNo.setText("");
		JTFMobileNo.setText("");
		JTFEmail.setText("");
	}

	//
	ActionListener JBActionListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			String srcObj = e.getActionCommand();

			/** if add button is click **/
			if (srcObj == "update") {
				if (RequiredFieldEmpty() == false) {
					// If new entry is added
					if (ADDING_STATE == true) {
						try {
							int cityID = 0;
							if (JCBCity.getSelectedIndex() != -1) {
								String citySql = "Select cityID from city where name='"
										+ JCBCity.getSelectedItem().toString() + "'";
								cityID = PublicMethods.getId(citySql, cnAEC, "cityID");
							}

							String insertSql = "INSERT INTO CLIENTS(firstname, middlename , lastname, address, postcode,cityid, contactno, mobile, email) "
									+ "VALUES ('" + JTFFirstName.getText() + "', '" + JTFMiddleName.getText() + "', '"
									+ JTFLastName.getText() + "', '" + JTFAddr.getText() + "', '" + JTFPostC.getText()
									+ "', " + cityID + ", '" + JTFContactNo.getText() + "', '" + JTFMobileNo.getText()
									+ "',  '" + JTFEmail.getText() + "')";

							System.out.println(insertSql);
							stAEC.executeUpdate(insertSql);
							// reload the clients table
							ClientViewPage.reloadRecord("SELECT * FROM CLIENTS ORDER BY CLIENTID ASC");

							// fill Global Client Id Map
							PublicMethods.fillClientIDMap(
									"select * from clients order by firstName asc, middlename asc, lastname asc",
									cnAEC);

							JOptionPane.showMessageDialog(null, "New Client record has been successfully added.",
									"Carbon Calculator", JOptionPane.INFORMATION_MESSAGE);
							String ObjButtons[] = { "Yes", "No" };
							int PromptResult = JOptionPane.showOptionDialog(null, "Do you want add another record?",
									"Carbon Calculator", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null,
									ObjButtons, ObjButtons[0]);
							if (PromptResult == 0) {
								clearFields();
								JTFFirstName.requestFocus(true);
							} else {
								dispose();
							}
						} catch (SQLException sqlEx) {
							System.out.println(sqlEx.getMessage());
							JOptionPane.showMessageDialog(null,
									"Sorry we are not able to insert the record. \n Please contact your support team.",
									"Carbon Calculator", JOptionPane.INFORMATION_MESSAGE);
						}
					} else {
						// old entry is updated
						try {
							String RowIndex;
							RowIndex = rsAEC.getString("ClientID");
							int cityID = 0;
							if (JCBCity.getSelectedIndex() != -1) {
								String citySql = "Select cityID from city where name='"
										+ JCBCity.getSelectedItem().toString() + "'";
								cityID = PublicMethods.getId(citySql, cnAEC, "cityID");
							}

							String updateSql = "UPDATE CLIENTS SET FirstName = '" + JTFFirstName.getText()
									+ "', MiddleName = '" + JTFMiddleName.getText() + "', LastName = '"
									+ JTFLastName.getText() + "', Address = '" + JTFAddr.getText() + "', PostCode = '"
									+ JTFPostC.getText() + "', " + "CityID = " + cityID + ", ContactNo = '"
									+ JTFContactNo.getText() + "', Mobile = '" + JTFMobileNo.getText() + "', Email = '"
									+ JTFEmail.getText() + "' WHERE clientID = " + RowIndex;

							System.out.println(updateSql);
							stAEC.executeUpdate(updateSql);

							// reload the clients table
							ClientViewPage.reloadRecord("SELECT * FROM clients ORDER BY clientID DESC");

							// Update Global Client Id Map
							PublicMethods.fillClientIDMap(
									"select * from clients order by firstName asc, middlename asc, lastname asc",
									cnAEC);

							JOptionPane.showMessageDialog(null, "Changes in the record has been successfully save.",
									"Carbon Calculator", JOptionPane.INFORMATION_MESSAGE);
							RowIndex = "";
							dispose();
						} catch (SQLException sqlEx) {
							System.out.println(sqlEx.getMessage());
							JOptionPane.showMessageDialog(null,
									"Sorry we are not able to update the record. \n Please contact your support team.",
									"Carbon Calculator", JOptionPane.INFORMATION_MESSAGE);
						}
					}
				}
			} else if (srcObj == "reset") {
				clearFields();
			} else if (srcObj == "cancel") {
				dispose();
			}
		}
	};

}
