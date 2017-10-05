package com.carboncalculator.forms;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import com.carboncalculator.utility.AddEditClient;
import com.carboncalculator.utility.PublicMethods;
import com.carboncalculator.buttoncell.*;

public class ClientViewPage extends JPanel {

	/** connection, statement, resultset variables */
	public static Connection cnClient;

	public static Statement stClient;

	public static ResultSet rsClient;

	/** Client Detail info Table */

	public static String strSQL;

	public static JTable JTClientTable;

	public static JScrollPane ClientTable = new JScrollPane();

	public static JPanel JPContainer = new JPanel();

	public static String dataContent[][];

	public static int total = 0;

	public static JFrame JFParentFrame;

	/** city Map holds the city Id and city name */
	private static Map<Integer, String> cityMap;

	/**
	 * Gridbag layout is used.
	 */
	public ClientViewPage(MainFrame mFrame, Connection SRCN) throws SQLException {

		JFParentFrame = mFrame;
		cnClient = SRCN;
		stClient = cnClient.createStatement();
		PublicMethods.fillClientIDMap("select * from clients order by firstName asc, middlename asc, lastname asc",
				cnClient);

		Color blueC = new Color(48, 126, 204);

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 1.0, 0.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
		JPContainer.setLayout(gridBagLayout);

		GridBagConstraints gbc = new GridBagConstraints();

		JLabel lblClient = new JLabel("Clients");
		lblClient.setForeground(blueC);
		lblClient.setFont(new Font("SansSerif", Font.PLAIN, 20));

		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(0, 5, 5, 5);
		gbc.gridx = 1;
		gbc.gridy = 0;
		JPContainer.add(lblClient, gbc);

		JButton btnAddClient = new JButton("Add New Client");
		btnAddClient.setForeground(Color.WHITE);
		btnAddClient.setBackground(blueC);
		btnAddClient.setFont(new Font("SansSerif", Font.PLAIN, 20));
		btnAddClient.addActionListener(clientActionListener);
		btnAddClient.setActionCommand("ADD");

		gbc.anchor = GridBagConstraints.WEST;
		gbc.gridy++;
		JPContainer.add(btnAddClient, gbc);

		JLabel clientListLabel = new JLabel("Client List");
		clientListLabel.setHorizontalAlignment(SwingConstants.LEFT);
		clientListLabel.setForeground(Color.WHITE);
		clientListLabel.setFont(new Font("SansSerif", Font.PLAIN, 20));
		clientListLabel.setBorder(new EmptyBorder(3, 3, 3, 3));
		clientListLabel.setOpaque(true);
		clientListLabel.setBackground(blueC);

		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.gridy++;
		JPContainer.add(clientListLabel, gbc);

		gbc.gridy++;
		JTClientTable = CreateTable();
		ClientTable.getViewport().add(JTClientTable);
		// ClientTable.setBounds(5,55,727,320);
		JPContainer.add(ClientTable, gbc);

		add(JPContainer);

	}

	/************************** Start event handling **************************/
	/** Listener for add button **/
	ActionListener clientActionListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			String srcObj = e.getActionCommand();
			// Add Record
			if (srcObj == "ADD") {
				JDialog JDAdd = new AddEditClient(true, JFParentFrame, cnClient, "");
				JDAdd.show();
				// Modify Record
			}
		}
	};

	/*************************** End event handling ***************************/

	/** JTable is return after populating data from clients table **/
	public static JTable CreateTable() {
		strSQL = "SELECT * FROM CLIENTS ORDER BY CLIENTID ASC";
		cityMap = PublicMethods.getIdValueMap("select cityId, name from city", cnClient, "cityId", "name");
		String ColumnHeaderName[] = { "client id", "Client Name", "Address", "City", "PostCode", "ContactNo",
				"Mobile No.", "Email", "Action" };
		try {
			total = 0;
			rsClient = stClient.executeQuery("select count(*) max_rows from clients");
			if (rsClient.next()) {
				total = rsClient.getInt("max_rows");
			}

			rsClient = stClient.executeQuery(strSQL);
			int rowNum = 0;
			if (total > 0) {
				dataContent = new String[total][9];
				while (rsClient.next()) {

					String cityName = cityMap.get(rsClient.getInt("cityid")) == null ? "No City Assigned"
							: cityMap.get(rsClient.getInt("cityid"));

					String clientName = rsClient.getString("firstName")
							+ (rsClient.getString("middleName").isEmpty() ? " " + rsClient.getString("lastName")
									: " " + rsClient.getString("middleName") + " " + rsClient.getString("lastName"));
					dataContent[rowNum][0] = "" + rsClient.getString("clientId");
					dataContent[rowNum][1] = "" + clientName;
					dataContent[rowNum][2] = "" + rsClient.getString("address");
					dataContent[rowNum][3] = "" + cityName;
					dataContent[rowNum][4] = "" + rsClient.getString("postcode");
					dataContent[rowNum][5] = "" + rsClient.getString("contactno");
					dataContent[rowNum][6] = "" + rsClient.getString("mobile");
					dataContent[rowNum][7] = "" + rsClient.getString("email");
					dataContent[rowNum][8] = "";
					rowNum++;
				}
			} else {
				dataContent = new String[0][9];
				dataContent[rowNum][0] = " ";
				dataContent[rowNum][1] = " ";
				dataContent[rowNum][2] = " ";
				dataContent[rowNum][3] = " ";
				dataContent[rowNum][4] = " ";
				dataContent[rowNum][5] = " ";
				dataContent[rowNum][6] = " ";
				dataContent[rowNum][7] = " ";
				dataContent[rowNum][8] = " ";
			}

		} catch (Exception eE) {
		}

		DefaultTableModel model = new DefaultTableModel(dataContent, ColumnHeaderName) {
			@Override
			public Class<?> getColumnClass(int column) {
				return getValueAt(0, column).getClass();
			}
		};
		JTable NewTable = new JTable(model);
		NewTable.setRowHeight(80);
		NewTable.setAutoCreateRowSorter(true);

		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		NewTable.setPreferredScrollableViewportSize(new Dimension(screen.width - 100, screen.height - 350));
		NewTable.setBackground(Color.white);

		NewTable.getColumnModel().getColumn(0).setPreferredWidth(50);
		NewTable.getColumnModel().getColumn(1).setPreferredWidth(100);
		NewTable.getColumnModel().getColumn(2).setPreferredWidth(150);
		NewTable.getColumnModel().getColumn(3).setPreferredWidth(50);
		NewTable.getColumnModel().getColumn(4).setPreferredWidth(100);
		NewTable.getColumnModel().getColumn(5).setPreferredWidth(100);
		NewTable.getColumnModel().getColumn(6).setPreferredWidth(100);
		NewTable.getColumnModel().getColumn(7).setPreferredWidth(100);
		NewTable.getColumnModel().getColumn(8).setPreferredWidth(100);

		/** In last column of the table, edit and delete buttons are added **/
		TableColumn column = NewTable.getColumnModel().getColumn(8);
		column.setCellRenderer(new ButtonsRenderer());
		column.setCellEditor(new ButtonsEditor(NewTable, "ClientViewPage"));

		// Disposed variables
		ColumnHeaderName = null;
		dataContent = null;

		return NewTable;
	}

	/************************** Start Custom method ***************************/

	/** reload the record of clients table */
	public static void reloadRecord(String srcSQL) {
		strSQL = srcSQL;
		ClientTable.getViewport().remove(JTClientTable);
		JTClientTable = CreateTable();
		ClientTable.getViewport().add(JTClientTable);
		JPContainer.repaint();
	}

	public static void reloadRecord() {
		ClientTable.getViewport().remove(JTClientTable);
		JTClientTable = CreateTable();
		ClientTable.getViewport().add(JTClientTable);
		JPContainer.repaint();
	}

	/*************************** End Custom method ****************************/
}
