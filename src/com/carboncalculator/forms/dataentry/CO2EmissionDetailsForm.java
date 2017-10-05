package com.carboncalculator.forms.dataentry;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.Properties;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.SqlDateModel;

import com.carboncalculator.buttoncell.ButtonsEditor;
import com.carboncalculator.buttoncell.ButtonsRenderer;
import com.carboncalculator.utility.DateLabelFormatter;
import com.carboncalculator.utility.GlobalValues;
import com.carboncalculator.utility.PublicMethods;

/*
 * It contains two table
 * One for the date range which shows CO2Emission for particular Energysource within the selected date range
 * Another is for the particular date
 * **/
public class CO2EmissionDetailsForm extends javax.swing.JPanel {

	public static JFrame JFParentFrame;
	private Connection cnCO2Emission;
	public static Statement stCO2Emission;

	public static ResultSet rsCO2Emission;

	public CO2EmissionDetailsForm(JFrame mFrame, Connection SRCN) {
		this.JFParentFrame = mFrame;
		this.cnCO2Emission = SRCN;
		initComponents();

	}

	JLabel JLHeader1 = new JLabel("List of CO2 Emissions Within The Date Range");

	JLabel JLDateRange = new JLabel("Select the Date Range:");
	JLabel JLFromDate = new JLabel("Start Date:");
	JLabel JLToDate = new JLabel("End Date:");

	JLabel JLSelectClient1 = new JLabel("Select Client:");

	JLabel JLHeader2 = new JLabel("CO2 Emissions Day Wise");
	JLabel JLSelectClient2 = new JLabel("Select Client:");
	JLabel JLSelectDate = new JLabel("Select Date:");

	JLabel JLEnergyType = new JLabel("Select Energy Type:");
	JLabel JLEnergySource = new JLabel("Select Energy Source:");

	JComboBox JCBClient1;
	JComboBox JCBClient2;
	JComboBox JCBEnergyType;
	JComboBox JCBEnergySource;

	JDatePickerImpl fromDatePicker;
	JDatePickerImpl toDatePicker;
	JDatePickerImpl selectDatePicker;

	Date fromDate;
	Date toDate;
	Date selectDate;

	int clientId1;
	int energySource;
	int energyType;

	int clientId2;

	private void initComponents() {
		try {
			stCO2Emission = cnCO2Emission.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		java.awt.GridBagConstraints gridBagConstraints;

		SqlDateModel fromSqlModel = new SqlDateModel();
		Properties pFrom = new Properties();
		pFrom.put("text.today", "Today");
		pFrom.put("text.month", "Month");
		pFrom.put("text.year", "Year");
		JDatePanelImpl fromDatePanel = new JDatePanelImpl(fromSqlModel, pFrom);

		// Don't know about the formatter, but there it is...
		fromDatePicker = new JDatePickerImpl(fromDatePanel, new DateLabelFormatter());

		SqlDateModel toSqlModel = new SqlDateModel();
		Properties pTo = new Properties();
		pTo.put("text.today", "Today");
		pTo.put("text.month", "Month");
		pTo.put("text.year", "Year");
		JDatePanelImpl toDatePanel = new JDatePanelImpl(toSqlModel, pTo);
		toDatePicker = new JDatePickerImpl(toDatePanel, new DateLabelFormatter());

		SqlDateModel selectSqlModel = new SqlDateModel();
		Properties pSelect = new Properties();
		pTo.put("text.today", "Today");
		pTo.put("text.month", "Month");
		pTo.put("text.year", "Year");
		JDatePanelImpl selectDatePanel = new JDatePanelImpl(selectSqlModel, pSelect);
		selectDatePicker = new JDatePickerImpl(selectDatePanel, new DateLabelFormatter());

		// JCBClient = new JComboBox<>();

		JCBClient1 = PublicMethods.fillComboClientAll(
				"Select clientId, firstName, middleName, lastName from clients order by firstName asc, middlename asc, lastname asc",
				this.cnCO2Emission);
		JCBClient1.addActionListener(actionListener);
		JCBClient1.setActionCommand("CLIENT1");

		JCBClient2 = PublicMethods.fillComboClientAll(
				"Select clientId, firstName, middleName, lastName from clients order by firstName asc, middlename asc, lastname asc",
				this.cnCO2Emission);
		JCBClient2.addActionListener(actionListener);
		JCBClient2.setActionCommand("CLIENT2");

		JCBEnergyType = PublicMethods.fillComboEnergy("Select description from energytype order by energytypeid asc",
				cnCO2Emission, "description");
		JCBEnergyType.addActionListener(actionListener);
		JCBEnergyType.setActionCommand("ENERGYTYPE");

		JCBEnergySource = PublicMethods.fillComboEnergy("Select description from energysource order by sourceid asc",
				cnCO2Emission, "description");
		JCBEnergySource.addActionListener(actionListener);
		JCBEnergySource.setActionCommand("ENERGYSOURCE");

		fromDatePicker.addActionListener(fromDateActionListener);
		toDatePicker.addActionListener(toDateActionListener);

		selectDatePicker.addActionListener(selectDateActionListener);

		jScrollPane1 = new javax.swing.JScrollPane();
		rangeTable = new javax.swing.JTable();
		jScrollPane2 = new javax.swing.JScrollPane();
		dateTable = new javax.swing.JTable();

		setLayout(new java.awt.GridBagLayout());

		JPanel headerPanel1 = new JPanel();
		headerPanel1.setLayout(new java.awt.GridBagLayout());

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		JLHeader1.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 25));
		gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 0);
		headerPanel1.add(JLHeader1, new java.awt.GridBagConstraints());

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 3;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
		add(headerPanel1, gridBagConstraints);

		JPanel dateNClientPanel1 = new JPanel();

		dateNClientPanel1.setLayout(new java.awt.GridBagLayout());

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		JLDateRange.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 0);
		dateNClientPanel1.add(JLDateRange, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 4;
		gridBagConstraints.gridy = 0;
		JLSelectClient1.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 0);
		dateNClientPanel1.add(JLSelectClient1, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		JLFromDate.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		// gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
		gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 0);
		dateNClientPanel1.add(JLFromDate, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 1;
		JLToDate.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 0);
		dateNClientPanel1.add(JLToDate, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		fromDatePicker.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
		gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 0);
		dateNClientPanel1.add(fromDatePicker, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 3;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		// gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
		fromDatePicker.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
		gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 0);
		dateNClientPanel1.add(toDatePicker, gridBagConstraints);

		gridBagConstraints.gridx = 4;
		gridBagConstraints.gridy = 1;
		dateNClientPanel1.add(JCBClient1, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 3;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.gridwidth = 4;
		gridBagConstraints.gridheight = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
		add(dateNClientPanel1, gridBagConstraints);

		JPanel energyPanel = new JPanel();
		energyPanel.setLayout(new java.awt.GridBagLayout());

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
		JLEnergyType.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
		gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 0);
		energyPanel.add(JLEnergyType, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
		JCBEnergyType.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
		gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 0);
		energyPanel.add(JCBEnergyType, gridBagConstraints);
		//
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
		JLEnergySource.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
		gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 0);
		energyPanel.add(JLEnergySource, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 3;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
		JCBEnergySource.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
		gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 0);
		energyPanel.add(JCBEnergySource, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 3;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
		add(energyPanel, gridBagConstraints);

		//
		tablePanel1 = new JPanel();
		//
		tablePanel1.setLayout(new java.awt.GridBagLayout());
		rangeTable = CreateRangeTable();
		jScrollPane1.setViewportView(rangeTable);
		//
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		tablePanel1.add(jScrollPane1, gridBagConstraints);
		//
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 3;
		gridBagConstraints.gridy = 6;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
		add(tablePanel1, gridBagConstraints);
		//
		JPanel headerPanel2 = new JPanel();
		headerPanel2.setLayout(new java.awt.GridBagLayout());

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		JLHeader2.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 25));
		gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 0);
		headerPanel2.add(JLHeader2, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 3;
		gridBagConstraints.gridy = 8;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
		add(headerPanel2, gridBagConstraints);
		//
		JPanel dateNClientPanel2 = new JPanel();
		dateNClientPanel2.setLayout(new java.awt.GridBagLayout());

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		JLSelectDate.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 0);
		dateNClientPanel2.add(JLSelectDate, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		selectDatePicker.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
		gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 0);
		dateNClientPanel2.add(selectDatePicker, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 0;
		JLSelectClient2.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 0);
		dateNClientPanel2.add(JLSelectClient2, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 3;
		gridBagConstraints.gridy = 0;
		JCBClient2.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
		gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 0);
		dateNClientPanel2.add(JCBClient2, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 3;
		gridBagConstraints.gridy = 9;
		gridBagConstraints.gridwidth = 4;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
		add(dateNClientPanel2, gridBagConstraints);

		tablePanel2 = new JPanel();
		//
		tablePanel2.setLayout(new java.awt.GridBagLayout());
		dateTable = CreateDateTable();
		jScrollPane2.setViewportView(dateTable);
		//
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		tablePanel2.add(jScrollPane2, gridBagConstraints);
		//
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 3;
		gridBagConstraints.gridy = 10;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
		add(tablePanel2, gridBagConstraints);

	}

	// Variables declaration
	private JPanel tablePanel1;
	private javax.swing.JScrollPane jScrollPane1;
	private JPanel tablePanel2;
	private javax.swing.JScrollPane jScrollPane2;
	private javax.swing.JTable rangeTable;
	private javax.swing.JTable dateTable;
	private static String strRangeTableQuery;
	private static int totalRange;
	public static String rangeDataContent[][];
	private static String strDateTableQuery;
	private static int totalDate;
	public static String dateDataContent[][];
	// End of variables declaration

	/**
	 * generates query for date range table
	 */
	private String getRangeSQL() {

		String rangeSqL = "SELECT s.clientid, " + "       s.firstname, " + "       s.middlename, "
				+ "       s.lastname, " + "f.energytypeid, " + "       f.energy_typedescription, " + "f.sourceid,"
				+ "       f.energy_sourcedescription, " + " s.from_date," + " s.to_date," + "       s.unit, "
				+ "       s.totalconsumption, " + "       s.conversionfactor, " + "       s.co2eproduce, "
				+ "       s.recycle, " + "       s.landfill " + "  FROM ( " + "           SELECT c.clientid, "
				+ "                  c.firstname, " + "                  c.middlename, "
				+ "                  c.lastname, " + "                  d.energysourceid, "
				+ "                  d.unit, " + "                  d.from_date, " + "                  d.to_date, "
				+ "                  d.totalconsumption, " + "                  d.conversionfactor, "
				+ "                  d.co2eproduce, " + "                  d.recycle, "
				+ "                  d.landfill " + "             FROM ( " + "                      SELECT clientid, "
				+ "                             energysourceid, " + "                             unit, "
				+ "                             from_date, " + "                             to_date, "
				+ "                             totalconsumption, " + "                             conversionfactor, "
				+ "                             co2eproduce, " + "                             recycle, "
				+ "                             landfill " + "                        FROM energyreadingdetail "
				+ "                  ) " + "                  d " + "                  JOIN " + "                  ( "
				+ "                      SELECT clientid, " + "                             firstname, "
				+ "                             middlename, " + "                             lastname "
				+ "                        FROM clients " + "                  ) "
				+ "                  c ON d.clientid = c.clientid " + "       ) " + "       s " + "       JOIN "
				+ "       ( " + "           SELECT a.energytypeid, " + "                  a.energy_typedescription, "
				+ "                  b.sourceid, " + "                  b.energy_sourcedescription "
				+ "             FROM ( " + "                      SELECT energytypeid, "
				+ "                             description AS energy_typedescription "
				+ "                        FROM energytype " + "                  ) " + "                  a "
				+ "                  JOIN " + "                  ( " + "                      SELECT sourceid, "
				+ "                             energytypeid, "
				+ "                             description AS energy_sourcedescription "
				+ "                        FROM energysource " + "                  ) "
				+ "                  b ON a.energytypeid = b.energytypeid " + "       ) "
				+ "       f ON S.energysourceid = f.sourceid";

		String filter = "";

		if (clientId1 > 0) {
			filter = filter + "s.clientid = " + clientId1;
		}

		if (energyType > 0) {
			System.out.println("Energy Type:" + energyType);
			if (filter != "") {
				filter += " AND ";
			}

			filter = filter + "f.energytypeid = '" + energyType + "'";
		}

		if (energySource > 0) {
			if (filter != "") {
				filter += " AND ";
			}

			filter = filter + "f.sourceid = '" + energySource + "'";
		}

		if (fromDate != null) {
			if (filter != "") {
				filter += " AND ";
			}

			filter = filter + "s.from_date >= '" + fromDate + "'";
		}

		if (toDate != null) {
			if (filter != "") {
				filter += " AND ";
			}

			filter = filter + "s.to_date <= '" + toDate + "'";
		}

		if (filter != "") {
			rangeSqL += " where " + filter;
		}

		return rangeSqL;

	}

	/*
	 * creates date range table which shows the co2emission for the energysource
	 * within the selected date range
	 **/
	public JTable CreateRangeTable() {

		String ColumnHeaderName[] = { "Client Name", "Energy Type", "Energy Source", "Unit", "Total Consumption",
				"Conversion Factor", "CO2 Emission" };

		strRangeTableQuery = getRangeSQL();
		try {
			totalRange = 0;
			rsCO2Emission = stCO2Emission.executeQuery("select count(*) max_rows from energyreadingdetail");
			if (rsCO2Emission.next()) {
				totalRange = rsCO2Emission.getInt("max_rows");
			}

			System.out.println(strRangeTableQuery);
			rsCO2Emission = stCO2Emission.executeQuery(strRangeTableQuery);
			int rowNum = 0;
			if (totalRange > 0) {
				rangeDataContent = new String[totalRange][7];
				while (rsCO2Emission.next()) {

					String clientName = rsCO2Emission.getString("firstName")
							+ (rsCO2Emission.getString("middleName").isEmpty()
									? " " + rsCO2Emission.getString("lastName")
									: " " + rsCO2Emission.getString("middleName") + " "
											+ rsCO2Emission.getString("lastName"));
					String energyType = "";
					if (rsCO2Emission.getBoolean("recycle")) {
						energyType += "Recycle ";
					} else if (rsCO2Emission.getBoolean("landfill")) {
						energyType += "Landfilling ";
					}
					rangeDataContent[rowNum][0] = clientName;
					rangeDataContent[rowNum][1] = energyType + rsCO2Emission.getString("energy_typedescription");
					rangeDataContent[rowNum][2] = "" + rsCO2Emission.getString("energy_sourcedescription");
					rangeDataContent[rowNum][3] = "" + rsCO2Emission.getString("unit");
					rangeDataContent[rowNum][4] = "" + rsCO2Emission.getDouble("totalconsumption");
					rangeDataContent[rowNum][5] = "" + rsCO2Emission.getDouble("conversionfactor");
					rangeDataContent[rowNum][6] = "" + rsCO2Emission.getString("cO2eproduce");
					rowNum++;
				}
			} else {
				rangeDataContent = new String[0][7];
				rangeDataContent[rowNum][0] = " ";
				rangeDataContent[rowNum][1] = " ";
				rangeDataContent[rowNum][2] = " ";
				rangeDataContent[rowNum][3] = " ";
				rangeDataContent[rowNum][4] = " ";
				rangeDataContent[rowNum][5] = " ";
				rangeDataContent[rowNum][6] = " ";
			}

		} catch (Exception eE) {
			System.out.println("Error" + eE);
			eE.printStackTrace();

		}

		JTable NewTable = new JTable(rangeDataContent, ColumnHeaderName) {
			public boolean isCellEditable(int iRows, int iCols) {
				return false;
			}
		};

		NewTable.setRowHeight(20);
		NewTable.setAutoCreateRowSorter(true);

		NewTable.setPreferredScrollableViewportSize(new Dimension(1050, 150));
		NewTable.setBackground(Color.white);

		NewTable.getColumnModel().getColumn(0).setPreferredWidth(150);
		NewTable.getColumnModel().getColumn(1).setPreferredWidth(150);
		NewTable.getColumnModel().getColumn(2).setPreferredWidth(200);
		NewTable.getColumnModel().getColumn(3).setPreferredWidth(50);
		NewTable.getColumnModel().getColumn(4).setPreferredWidth(100);
		NewTable.getColumnModel().getColumn(5).setPreferredWidth(100);
		NewTable.getColumnModel().getColumn(6).setPreferredWidth(100);

		// Disposed variables
		ColumnHeaderName = null;
		rangeDataContent = null;

		return NewTable;
	}

	/************************** Start Custom method ***************************/
	/** reloads the rangeTable **/
	public void reloadRecord() {
		jScrollPane1.getViewport().remove(rangeTable);
		rangeTable = CreateRangeTable();
		jScrollPane1.getViewport().add(rangeTable);
		tablePanel1.repaint();
	}

	/**
	 * generates query for particular date table
	 */
	private String getDateSQL() {

		String rangeSqL = "SELECT c.clientid, " + "       c.firstname, " + "       c.middlename, "
				+ "       c.lastname, " + "       a.entry_date, " + "       a.totalCO2E " + "  FROM ( "
				+ "           SELECT clientid, " + "                  entry_date, " + "                  totalCO2E "
				+ "             FROM energyreadingmeter " + "       ) " + "       a " + "       JOIN " + "       ( "
				+ "           SELECT clientid, " + "                  firstname, " + "                  middlename, "
				+ "                  lastname " + "             FROM clients " + "       ) "
				+ "       c ON a.clientid = c.clientid";

		String filter = "";

		if (clientId2 > 0) {
			filter = filter + "a.clientid = " + clientId2;
		}

		if (selectDate != null) {
			if (filter != "") {
				filter += " AND ";
			}

			filter = filter + "a.entry_date = '" + selectDate + "'";
		}

		if (filter != "") {
			rangeSqL += " where " + filter;
		}

		return rangeSqL;

	}

	/*
	 * creates particular date table which shows the co2emission for particular
	 * date
	 **/
	public JTable CreateDateTable() {

		String ColumnHeaderName[] = { "Client Name", "Date", "Total CO2E" };

		strDateTableQuery = getDateSQL();
		try {
			totalDate = 0;
			rsCO2Emission = stCO2Emission.executeQuery("select count(*) max_rows from energyreadingdetail");
			if (rsCO2Emission.next()) {
				totalDate = rsCO2Emission.getInt("max_rows");
			}

			System.out.println(strDateTableQuery);
			rsCO2Emission = stCO2Emission.executeQuery(strDateTableQuery);
			int rowNum = 0;
			if (totalDate > 0) {
				dateDataContent = new String[totalDate][3];
				while (rsCO2Emission.next()) {

					String clientName = rsCO2Emission.getString("firstName")
							+ (rsCO2Emission.getString("middleName").isEmpty()
									? " " + rsCO2Emission.getString("lastName")
									: " " + rsCO2Emission.getString("middleName") + " "
											+ rsCO2Emission.getString("lastName"));
					dateDataContent[rowNum][0] = clientName;
					dateDataContent[rowNum][1] = "" + rsCO2Emission.getString("entry_date");
					dateDataContent[rowNum][2] = "" + rsCO2Emission.getDouble("totalCO2E");
					rowNum++;
				}
			} else {
				dateDataContent = new String[0][3];
				dateDataContent[rowNum][0] = " ";
				dateDataContent[rowNum][1] = " ";
				dateDataContent[rowNum][2] = " ";
			}

		} catch (Exception eE) {
			System.out.println("Error" + eE);
			eE.printStackTrace();

		}

		JTable NewTable = new JTable(dateDataContent, ColumnHeaderName) {
			public boolean isCellEditable(int iRows, int iCols) {
				return false;
			}
		};

		NewTable.setRowHeight(20);
		NewTable.setAutoCreateRowSorter(true);

		NewTable.setPreferredScrollableViewportSize(new Dimension(500, 100));
		NewTable.setBackground(Color.white);

		NewTable.getColumnModel().getColumn(0).setPreferredWidth(150);
		NewTable.getColumnModel().getColumn(1).setPreferredWidth(150);
		NewTable.getColumnModel().getColumn(2).setPreferredWidth(200);

		// Disposed variables
		ColumnHeaderName = null;
		dateDataContent = null;

		return NewTable;
	}

	/************************** Start Custom method ***************************/
	/** reloads the rangeTable **/
	public void reloadRecordDateTable() {
		jScrollPane2.getViewport().remove(dateTable);
		dateTable = CreateDateTable();
		jScrollPane2.getViewport().add(dateTable);
		tablePanel2.repaint();
	}

	ActionListener actionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			switch (e.getActionCommand()) {
			case "CLIENT1":
				clientId1 = JCBClient1.getSelectedIndex();

				System.out.println("ClientID:" + clientId1);

				clientId1 = clientId1 > 0 ? GlobalValues.clientIDMap.get(clientId1 - 1) : 0;

				reloadRecord();
				break;

			case "ENERGYTYPE":
				energyType = JCBEnergyType.getSelectedIndex();
				String sql;
				if (energyType > 0) {
					sql = "Select description from energysource where energytypeid = " + energyType
							+ " order by sourceid asc";
				} else {
					sql = "Select description from energysource order by sourceid asc";
				}

				JCBEnergySource.setModel(new javax.swing.DefaultComboBoxModel<>(
						PublicMethods.getComboEntry(sql, cnCO2Emission, "description")));

				JCBEnergySource.repaint();
				energySource = 0;
				reloadRecord();
				break;

			case "ENERGYSOURCE":
				energySource = JCBEnergySource.getSelectedIndex();
				reloadRecord();
				break;

			case "CLIENT2":
				clientId2 = JCBClient2.getSelectedIndex();

				clientId2 = clientId2 > 0 ? GlobalValues.clientIDMap.get(clientId2 - 1) : 0;

				reloadRecordDateTable();
				break;
			}
		}
	};

	ActionListener selectDateActionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub

			selectDate = (java.sql.Date) selectDatePicker.getModel().getValue();
			if (selectDate != null) {
				reloadRecordDateTable();
			}
		}
	};

	ActionListener fromDateActionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub

			fromDate = (java.sql.Date) fromDatePicker.getModel().getValue();
			if (fromDate != null) {
				reloadRecord();
			}
		}
	};

	ActionListener toDateActionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub

			toDate = (java.sql.Date) toDatePicker.getModel().getValue();
			if (toDate != null) {
				reloadRecord();
			}
		}
	};
}