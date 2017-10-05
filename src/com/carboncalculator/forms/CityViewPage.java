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
import com.carboncalculator.utility.AddEditCity;

public class CityViewPage extends JPanel {

	/** connection, statement, resultset variables */
	public static Connection cnCity;

	public static Statement stCity;

	public static ResultSet rsCity;

	/** City Detail info Table */
	public static String strSQL;

	public static JScrollPane CityTable = new JScrollPane();

	public static JPanel JPContainer = new JPanel();

	public static JTable JTCityTable;

	public static String dataContent[][];

	public static int total = 0;

	public static JFrame JFParentFrame;

	public CityViewPage(MainFrame mFrame, Connection SRCN) throws SQLException {

		JFParentFrame = mFrame;
		cnCity = SRCN;
		stCity = cnCity.createStatement();

		Color blueC = new Color(48, 126, 204);

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 1.0, 0.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
		JPContainer.setLayout(gridBagLayout);

		GridBagConstraints gbc = new GridBagConstraints();

		JLabel lblCity = new JLabel("Cities");
		lblCity.setForeground(blueC);
		lblCity.setFont(new Font("SansSerif", Font.PLAIN, 20));

		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(0, 5, 5, 5);
		gbc.gridx = 1;
		gbc.gridy = 0;
		JPContainer.add(lblCity, gbc);

		JButton btnAddCity = new JButton("Add New City");
		btnAddCity.setForeground(Color.WHITE);
		btnAddCity.setBackground(blueC);
		btnAddCity.setFont(new Font("SansSerif", Font.PLAIN, 20));
		btnAddCity.addActionListener(CITYActionListener);
		btnAddCity.setActionCommand("ADD");

		gbc.anchor = GridBagConstraints.WEST;
		gbc.gridy++;
		JPContainer.add(btnAddCity, gbc);

		JLabel CITYListLabel = new JLabel("City List");
		CITYListLabel.setHorizontalAlignment(SwingConstants.LEFT);
		CITYListLabel.setForeground(Color.WHITE);
		CITYListLabel.setFont(new Font("SansSerif", Font.PLAIN, 20));
		CITYListLabel.setBorder(new EmptyBorder(3, 3, 3, 3));
		CITYListLabel.setOpaque(true);
		CITYListLabel.setBackground(blueC);

		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.gridy++;
		JPContainer.add(CITYListLabel, gbc);

		gbc.gridy++;
		JTCityTable = CreateTable();
		CityTable.getViewport().add(JTCityTable);
		JPContainer.add(CityTable, gbc);

		add(JPContainer);

	}

	/************************** Start event handling **************************/
	/** Listener for add button **/
	ActionListener CITYActionListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			String srcObj = e.getActionCommand();
			// Add Record
			if (srcObj == "ADD") {
				JDialog JDAdd = new AddEditCity(true, JFParentFrame, cnCity, "");
				JDAdd.show();
				// Modify Record
			}
		}
	};

	/*************************** End event handling ***************************/
	/** JTable is return after populating data from city table **/
	public static JTable CreateTable() {
		strSQL = "SELECT * FROM CITY ORDER BY CITYID ASC";
		String ColumnHeaderName[] = { "CITY ID", "City Name", "Action" };
		try {
			total = 0;
			rsCity = stCity.executeQuery("select count(*) max_rows from CITY");
			if (rsCity.next()) {
				total = rsCity.getInt("max_rows");
			}

			System.out.println(total);
			rsCity = stCity.executeQuery(strSQL);
			int rowNum = 0;
			if (total > 0) {
				dataContent = new String[total][3];
				while (rsCity.next()) {
					dataContent[rowNum][0] = "" + rsCity.getString("CITYId");
					dataContent[rowNum][1] = "" + rsCity.getString("name");
					dataContent[rowNum][2] = "";
					rowNum++;
				}
			} else {
				dataContent = new String[0][3];
				dataContent[rowNum][0] = " ";
				dataContent[rowNum][1] = " ";
				dataContent[rowNum][2] = " ";
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
		NewTable.setRowHeight(50);
		NewTable.setAutoCreateRowSorter(true);

		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		NewTable.setPreferredScrollableViewportSize(new Dimension(screen.width - 100, screen.height - 350));
		NewTable.setBackground(Color.white);

		NewTable.getColumnModel().getColumn(0).setPreferredWidth(100);
		NewTable.getColumnModel().getColumn(1).setPreferredWidth(300);
		NewTable.getColumnModel().getColumn(2).setPreferredWidth(200);

		/** In last column of the table, edit and delete buttons are added **/
		TableColumn column = NewTable.getColumnModel().getColumn(2);
		column.setCellRenderer(new ButtonsRenderer());
		column.setCellEditor(new ButtonsEditor(NewTable, "CityViewPage"));

		// Disposed variables
		ColumnHeaderName = null;
		dataContent = null;

		return NewTable;
	}

	// static ButtonsRenderer bRender;

	/************************** Start Custom method ***************************/

	/** reload the record of city table */
	public static void reloadRecord(String srcSQL) {
		strSQL = srcSQL;
		CityTable.getViewport().remove(JTCityTable);
		JTCityTable = CreateTable();
		CityTable.getViewport().add(JTCityTable);
		JPContainer.repaint();
	}

	public static void reloadRecord() {
		CityTable.getViewport().remove(JTCityTable);
		JTCityTable = CreateTable();
		CityTable.getViewport().add(JTCityTable);
		JPContainer.repaint();
	}

	/*************************** End Custom method ****************************/
}
