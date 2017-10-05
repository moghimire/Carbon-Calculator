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

import com.carboncalculator.buttoncell.ButtonsEditor;
import com.carboncalculator.buttoncell.ButtonsRenderer;
import com.carboncalculator.utility.AddEditHouseType;

public class HouseTypeViewPage extends JPanel {

	/** connection, statement, resultset variables */
	public static Connection cnHouseType;

	public static Statement stHouseType;

	public static ResultSet rsHouseType;

	/** House Detail info Table */
	public static String strSQL;

	public static JScrollPane HouseTypeTable = new JScrollPane();

	public static JPanel JPContainer = new JPanel();

	public static JTable JTHouseTypeTable;

	public static String dataContent[][];

	public static int total = 0;

	public static JFrame JFParentFrame;

	public HouseTypeViewPage(MainFrame mFrame, Connection SRCN) throws SQLException {

		JFParentFrame = mFrame;
		cnHouseType = SRCN;
		stHouseType = cnHouseType.createStatement();

		Color blueC = new Color(48, 126, 204);

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 1.0, 0.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
		JPContainer.setLayout(gridBagLayout);

		GridBagConstraints gbc = new GridBagConstraints();

		JLabel lblHouseType = new JLabel("House");
		lblHouseType.setForeground(blueC);
		lblHouseType.setFont(new Font("SansSerif", Font.PLAIN, 20));

		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(0, 5, 5, 5);
		gbc.gridx = 1;
		gbc.gridy = 0;
		JPContainer.add(lblHouseType, gbc);

		JButton btnAddHouseType = new JButton("Add New House");
		btnAddHouseType.setForeground(Color.WHITE);
		btnAddHouseType.setBackground(blueC);
		btnAddHouseType.setFont(new Font("SansSerif", Font.PLAIN, 20));
		btnAddHouseType.addActionListener(HouseTypeActionListener);
		btnAddHouseType.setActionCommand("ADD");

		gbc.anchor = GridBagConstraints.WEST;
		gbc.gridy++;
		JPContainer.add(btnAddHouseType, gbc);

		JLabel HouseTypeListLabel = new JLabel("House List");
		HouseTypeListLabel.setHorizontalAlignment(SwingConstants.LEFT);
		HouseTypeListLabel.setForeground(Color.WHITE);
		HouseTypeListLabel.setFont(new Font("SansSerif", Font.PLAIN, 20));
		HouseTypeListLabel.setBorder(new EmptyBorder(3, 3, 3, 3));
		HouseTypeListLabel.setOpaque(true);
		HouseTypeListLabel.setBackground(blueC);

		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.gridy++;
		JPContainer.add(HouseTypeListLabel, gbc);

		gbc.gridy++;
		JTHouseTypeTable = CreateTable();
		HouseTypeTable.getViewport().add(JTHouseTypeTable);
		JPContainer.add(HouseTypeTable, gbc);

		add(JPContainer);

	}

	/************************** Start event handling **************************/
	/** Listener for add button **/
	ActionListener HouseTypeActionListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			String srcObj = e.getActionCommand();
			// Add Record
			if (srcObj == "ADD") {
				JDialog JDAdd = new AddEditHouseType(true, JFParentFrame, cnHouseType, "");
				JDAdd.show();
				// Modify Record
			}
		}
	};

	/*************************** End event handling ***************************/
	/** JTable is return after populating data from housetype table **/
	public static JTable CreateTable() {
		strSQL = "SELECT * FROM HouseType ORDER BY House_ID ASC";
		String ColumnHeaderName[] = { "House id", "House Name", "No of Beds", "Action" };
		try {
			total = 0;
			rsHouseType = stHouseType.executeQuery("select count(*) max_rows from HouseType");
			if (rsHouseType.next()) {
				total = rsHouseType.getInt("max_rows");
			}

			System.out.println(total);
			rsHouseType = stHouseType.executeQuery(strSQL);
			int rowNum = 0;
			if (total > 0) {
				dataContent = new String[total][4];
				while (rsHouseType.next()) {

					dataContent[rowNum][0] = "" + rsHouseType.getString("House_Id");
					dataContent[rowNum][1] = "" + rsHouseType.getString("House_Type");
					dataContent[rowNum][2] = "" + rsHouseType.getString("no_of_beds");
					dataContent[rowNum][3] = "";
					rowNum++;
				}
			} else {
				dataContent = new String[0][4];
				dataContent[rowNum][0] = " ";
				dataContent[rowNum][1] = " ";
				dataContent[rowNum][2] = " ";
				dataContent[rowNum][3] = " ";
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

		NewTable.getColumnModel().getColumn(0).setPreferredWidth(100);
		NewTable.getColumnModel().getColumn(1).setPreferredWidth(200);
		NewTable.getColumnModel().getColumn(2).setPreferredWidth(100);
		NewTable.getColumnModel().getColumn(3).setPreferredWidth(200);

		/** In last column of the table, edit and delete buttons are added **/
		TableColumn column = NewTable.getColumnModel().getColumn(3);
		column.setCellRenderer(new ButtonsRenderer());
		column.setCellEditor(new ButtonsEditor(NewTable, "HouseTypeViewPage"));

		// Disposed variables
		ColumnHeaderName = null;
		dataContent = null;

		return NewTable;
	}

	// static ButtonsRenderer bRender;

	/************************** Start Custom method ***************************/

	/** reload the record of house table */
	public static void reloadRecord(String srcSQL) {
		strSQL = srcSQL;
		HouseTypeTable.getViewport().remove(JTHouseTypeTable);
		JTHouseTypeTable = CreateTable();
		HouseTypeTable.getViewport().add(JTHouseTypeTable);
		JPContainer.repaint();
	}

	public static void reloadRecord() {
		HouseTypeTable.getViewport().remove(JTHouseTypeTable);
		JTHouseTypeTable = CreateTable();
		HouseTypeTable.getViewport().add(JTHouseTypeTable);
		JPContainer.repaint();
	}

	/*************************** End Custom method ****************************/
}
