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

import com.carboncalculator.buttoncell.ButtonsEditor;
import com.carboncalculator.buttoncell.ButtonsRenderer;
import com.carboncalculator.utility.AddEditVehicle;
import com.carboncalculator.utility.PublicMethods;

public class VehicleViewPage extends JPanel {

	/** connection, statement, resultset variables */
	public static Connection cnVehicle;

	public static Statement stVehicle;

	public static ResultSet rsVehicle;

	/** Vehicle Detail info Table */
	public static String strSQL;

	public static JScrollPane VehicleTable = new JScrollPane();

	public static JPanel JPContainer = new JPanel();

	public static JTable JTVehicleTable;

	public static String dataContent[][];

	public static int total = 0;

	public static JFrame JFParentFrame;

	private static Map<Integer, String> fuelMap;

	public VehicleViewPage(MainFrame mFrame, Connection SRCN) throws SQLException {

		JFParentFrame = mFrame;
		cnVehicle = SRCN;
		stVehicle = cnVehicle.createStatement();

		Color blueC = new Color(48, 126, 204);

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 1.0, 0.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
		JPContainer.setLayout(gridBagLayout);

		GridBagConstraints gbc = new GridBagConstraints();

		JLabel lblVehicle = new JLabel("Vehicles");
		lblVehicle.setForeground(blueC);
		lblVehicle.setFont(new Font("SansSerif", Font.PLAIN, 20));

		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(0, 5, 5, 5);
		gbc.gridx = 1;
		gbc.gridy = 0;
		JPContainer.add(lblVehicle, gbc);

		JButton btnAddVehicle = new JButton("Add New Vehicle");
		btnAddVehicle.setForeground(Color.WHITE);
		btnAddVehicle.setBackground(blueC);
		btnAddVehicle.setFont(new Font("SansSerif", Font.PLAIN, 20));
		btnAddVehicle.addActionListener(VehicleActionListener);
		btnAddVehicle.setActionCommand("ADD");

		gbc.anchor = GridBagConstraints.WEST;
		gbc.gridy++;
		JPContainer.add(btnAddVehicle, gbc);

		JLabel VehicleListLabel = new JLabel("Vehicle List");
		VehicleListLabel.setHorizontalAlignment(SwingConstants.LEFT);
		VehicleListLabel.setForeground(Color.WHITE);
		VehicleListLabel.setFont(new Font("SansSerif", Font.PLAIN, 20));
		VehicleListLabel.setBorder(new EmptyBorder(3, 3, 3, 3));
		VehicleListLabel.setOpaque(true);
		VehicleListLabel.setBackground(blueC);

		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.gridy++;
		JPContainer.add(VehicleListLabel, gbc);

		gbc.gridy++;
		JTVehicleTable = CreateTable();
		VehicleTable.getViewport().add(JTVehicleTable);
		JPContainer.add(VehicleTable, gbc);

		add(JPContainer);

	}

	/************************** Start event handling **************************/
	/** Listener for add button **/
	ActionListener VehicleActionListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			String srcObj = e.getActionCommand();
			// Add Record
			if (srcObj == "ADD") {
				JDialog JDAdd = new AddEditVehicle(true, JFParentFrame, cnVehicle, "");
				JDAdd.show();
				// Modify Record
			}
		}
	};

	/*************************** End event handling ***************************/
	/** JTable is return after populating data from vehicles table **/
	public static JTable CreateTable() {
		strSQL = "SELECT * FROM Vehicles ORDER BY VehicleID ASC";
		fuelMap = PublicMethods.getIdValueMap("select fueltype_Id, name from fueltype", cnVehicle, "fueltype_Id",
				"name");

		String ColumnHeaderName[] = { "Vehicle id", "Vehicle Name", "Fuel Type", "Action" };
		try {
			total = 0;
			rsVehicle = stVehicle.executeQuery("select count(*) max_rows from Vehicles");
			if (rsVehicle.next()) {
				total = rsVehicle.getInt("max_rows");
			}

			System.out.println(total);
			rsVehicle = stVehicle.executeQuery(strSQL);
			int rowNum = 0;
			if (total > 0) {
				dataContent = new String[total][4];
				while (rsVehicle.next()) {

					String fuelName = fuelMap.get(rsVehicle.getInt("fueltype_id")) == null ? "No Fuel Type Assigned"
							: fuelMap.get(rsVehicle.getInt("fueltype_id"));

					dataContent[rowNum][0] = "" + rsVehicle.getString("VehicleId");
					dataContent[rowNum][1] = "" + rsVehicle.getString("Vehicle_Name");
					dataContent[rowNum][2] = "" + fuelName;
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
		column.setCellEditor(new ButtonsEditor(NewTable, "VehicleViewPage"));

		// Disposed variables
		ColumnHeaderName = null;
		dataContent = null;

		return NewTable;
	}

	// static ButtonsRenderer bRender;

	/************************** Start Custom method ***************************/

	/** reload the record of vehicle table */
	public static void reloadRecord(String srcSQL) {
		strSQL = srcSQL;
		VehicleTable.getViewport().remove(JTVehicleTable);
		JTVehicleTable = CreateTable();
		VehicleTable.getViewport().add(JTVehicleTable);
		JPContainer.repaint();
	}

	public static void reloadRecord() {
		VehicleTable.getViewport().remove(JTVehicleTable);
		JTVehicleTable = CreateTable();
		VehicleTable.getViewport().add(JTVehicleTable);
		JPContainer.repaint();
	}

	/*************************** End Custom method ****************************/
}