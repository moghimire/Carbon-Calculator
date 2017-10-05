package com.carboncalculator.forms.dataentry;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.SqlDateModel;

import com.carboncalculator.utility.DateLabelFormatter;
import com.carboncalculator.utility.GlobalValues;
import com.carboncalculator.utility.PublicMethods;

public class HouseDataEntry extends JPanel {

	public static JFrame JFParentFrame;
	private Connection cnHouseDataEntry;
	public static Statement stHouseDataEntry;
	public static PreparedStatement pstmt;

	public static ResultSet rsHouseDataEntry;

	public HouseDataEntry(JFrame mFrame, Connection SRCN) {
		this.JFParentFrame = mFrame;
		this.cnHouseDataEntry = SRCN;
		initiaLizeRequirements();
		initComponents();

	}

	JLabel JLImage;

	JLabel JLHeader = new JLabel("Household carbon footprint calculator");
	JLabel JLBanner = new JLabel("Please fill-up all the required fields.");

	JLabel JLDateRange = new JLabel("Select the Date Range:");
	JLabel JLFromDate = new JLabel("Start Date:");
	JLabel JLToDate = new JLabel("End Date:");

	JLabel JLSelectClient = new JLabel("Select Client:");
	JLabel JLUnit = new JLabel("Units:");

	JComboBox JCBClient;

	JDatePickerImpl fromDatePicker;
	JDatePickerImpl toDatePicker;

	Date fromDate;
	Date toDate;

	JLabel JLElectricity = new JLabel("Electricity from Grid:");
	JLabel JLGas = new JLabel("Gas (LNG as used in Grid):");
	JLabel JLHeatingOil = new JLabel("Heating Oil (kerosine/parrafin):");
	JLabel JLCoal = new JLabel("Coal (domestic):");
	JLabel JLWoodLogs = new JLabel("Wood logs:");
	JLabel JLWoodChips = new JLabel("Wood chips:");
	JLabel JLWoodPellets = new JLabel("Wood pellets:");

	JTextField JTFElectricity = new JTextField();
	JTextField JTFGas = new JTextField();
	JTextField JTFHeatingOil = new JTextField();
	JTextField JTFCoal = new JTextField();
	JTextField JTFWoodLogs = new JTextField();
	JTextField JTFWoodChips = new JTextField();
	JTextField JTFWoodPellets = new JTextField();

	JComboBox JCBElectricityUnit;
	JComboBox JCBGasUnit;
	JComboBox JCBHeatingOilUnit;
	JComboBox JCBCoalUnit;
	JComboBox JCBWoodLogsUnit;
	JComboBox JCBWoodChipsUnit;
	JComboBox JCBWoodPelletsUnit;

	JButton calculateButton = new JButton("Calculate Household Footprint");
	JTextField JTFResult = new JTextField("Total GHG emissions from House = 0.00 kgs of CO2e");
	JButton saveButton = new JButton("Save");

	// This map contains energysourceid with unit and conversionfactor
	private Map<Integer, Map<String, Double>> energySourceDetail;

	private Double electricityCO2E = 0.0;
	private Double gasCO2E = 0.0;
	private Double heatingOilCO2E = 0.0;
	private Double coalCO2E = 0.0;
	private Double woodLogsCO2E = 0.0;
	private Double woodChipsCO2E = 0.0;
	private Double woodPelletsCO2E = 0.0;
	public static Double totalHouseCO2E = 0.0;

	// Hardcoded energysourceIDs
	private int HOUSEHOLD = 1;
	private int ELECTRICITY = 1;
	private int GAS = 2;
	private int HEATINGOIL = 3;
	private int COAL = 4;
	private int WOODLOGS = 5;
	private int WOODCHIPS = 6;
	private int WOODPELLETS = 7;

	public static String clientName;

	/*
	 * fill up the energySourceDetail map
	 **/
	private void initiaLizeRequirements() {

		try {
			stHouseDataEntry = cnHouseDataEntry.createStatement();

			String sql = "select energysourceid, unit, conversionfactor from energysourcedetail where energysourceid in (select sourceid from energysource where energytypeid ="
					+ HOUSEHOLD + ") order by energysourceid asc";
			rsHouseDataEntry = stHouseDataEntry.executeQuery(sql);

			Map<String, Double> unitConversionF = null;
			// List<Map<String, Double>> listUnitConversionF;
			energySourceDetail = new LinkedHashMap<Integer, Map<String, Double>>();

			int energysourceId;
			String unit;
			Double coversionFactor;

			while (rsHouseDataEntry.next()) {
				energysourceId = rsHouseDataEntry.getInt("energysourceid");
				unit = rsHouseDataEntry.getString("unit");
				coversionFactor = rsHouseDataEntry.getDouble("conversionfactor");

				if (energySourceDetail.containsKey(energysourceId)) {
					energySourceDetail.remove(energysourceId);
				} else {
					unitConversionF = new LinkedHashMap<String, Double>();
				}
				unitConversionF.put(unit, coversionFactor);
				energySourceDetail.put(energysourceId, unitConversionF);

			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/* initializes UI components */
	private void initComponents() {
		java.awt.GridBagConstraints gridBagConstraints;

		SqlDateModel fromSqlModel = new SqlDateModel();
		Properties pFrom = new Properties();
		pFrom.put("text.today", "Today");
		pFrom.put("text.month", "Month");
		pFrom.put("text.year", "Year");
		JDatePanelImpl fromDatePanel = new JDatePanelImpl(fromSqlModel, pFrom);

		fromDatePicker = new JDatePickerImpl(fromDatePanel, new DateLabelFormatter());

		SqlDateModel toSqlModel = new SqlDateModel();
		Properties pTo = new Properties();
		pTo.put("text.today", "Today");
		pTo.put("text.month", "Month");
		pTo.put("text.year", "Year");
		JDatePanelImpl toDatePanel = new JDatePanelImpl(toSqlModel, pTo);
		toDatePicker = new JDatePickerImpl(toDatePanel, new DateLabelFormatter());

		// JCBClient = new JComboBox<>();

		JCBClient = PublicMethods.fillComboClient(
				"Select clientid, firstName, middleName, lastName from clients order by firstName asc, middlename asc, lastname asc",
				this.cnHouseDataEntry);

		JCBElectricityUnit = new JComboBox<>();
		JCBElectricityUnit.setModel(
				new javax.swing.DefaultComboBoxModel<>(energySourceDetail.get(ELECTRICITY).keySet().toArray()));

		JCBGasUnit = new JComboBox<>();
		JCBGasUnit.setModel(new javax.swing.DefaultComboBoxModel<>(energySourceDetail.get(GAS).keySet().toArray()));

		JCBHeatingOilUnit = new JComboBox<>();
		JCBHeatingOilUnit.setModel(
				new javax.swing.DefaultComboBoxModel<>(energySourceDetail.get(HEATINGOIL).keySet().toArray()));

		JCBCoalUnit = new JComboBox<>();
		JCBCoalUnit.setModel(new javax.swing.DefaultComboBoxModel<>(energySourceDetail.get(COAL).keySet().toArray()));

		JCBWoodLogsUnit = new JComboBox<>();
		JCBWoodLogsUnit
				.setModel(new javax.swing.DefaultComboBoxModel<>(energySourceDetail.get(WOODLOGS).keySet().toArray()));

		JCBWoodChipsUnit = new JComboBox<>();
		JCBWoodChipsUnit
				.setModel(new javax.swing.DefaultComboBoxModel<>(energySourceDetail.get(WOODCHIPS).keySet().toArray()));

		JCBWoodPelletsUnit = new JComboBox<>();
		JCBWoodPelletsUnit.setModel(
				new javax.swing.DefaultComboBoxModel<>(energySourceDetail.get(WOODPELLETS).keySet().toArray()));

		setLayout(new java.awt.GridBagLayout());

		ImageIcon image = new ImageIcon("images/housecalculator.jpg");
		JLImage = new JLabel("", image, JLabel.CENTER);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridwidth = 4;
		gridBagConstraints.gridheight = 17;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		add(JLImage, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 4;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridwidth = 4;
		JLHeader.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 35));
		gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 0);
		add(JLHeader, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 7;
		gridBagConstraints.gridy = 1;
		JLSelectClient.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
		gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 0);
		add(JLSelectClient, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 7;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.gridwidth = 4;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
		add(JCBClient, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 4;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.gridwidth = 2;
		JLDateRange.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
		gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 0);
		add(JLDateRange, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 4;
		gridBagConstraints.gridy = 2;
		JLFromDate.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
		add(JLFromDate, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 5;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 10);
		add(fromDatePicker, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 4;
		gridBagConstraints.gridy = 3;
		JLToDate.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
		add(JLToDate, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 5;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 10);
		add(toDatePicker, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 4;
		gridBagConstraints.gridy = 6;
		JLElectricity.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
		add(JLElectricity, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 4;
		gridBagConstraints.gridy = 7;
		JLGas.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
		add(JLGas, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 4;
		gridBagConstraints.gridy = 8;
		gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
		JLHeatingOil.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
		add(JLHeatingOil, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 4;
		gridBagConstraints.gridy = 9;
		JLCoal.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
		add(JLCoal, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 4;
		gridBagConstraints.gridy = 10;
		JLWoodLogs.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
		add(JLWoodLogs, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 4;
		gridBagConstraints.gridy = 11;
		JLWoodChips.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
		add(JLWoodChips, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 4;
		gridBagConstraints.gridy = 12;
		JLWoodPellets.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
		add(JLWoodPellets, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 5;
		gridBagConstraints.gridy = 6;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 10);
		add(JTFElectricity, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 5;
		gridBagConstraints.gridy = 7;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 10);
		add(JTFGas, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 5;
		gridBagConstraints.gridy = 8;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 10);
		add(JTFHeatingOil, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 5;
		gridBagConstraints.gridy = 9;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 10);
		add(JTFCoal, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 5;
		gridBagConstraints.gridy = 10;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 10);
		add(JTFWoodLogs, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 5;
		gridBagConstraints.gridy = 11;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 10);
		add(JTFWoodChips, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 5;
		gridBagConstraints.gridy = 12;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 10);
		add(JTFWoodPellets, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 7;
		gridBagConstraints.gridy = 5;
		JLUnit.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
		add(JLUnit, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 7;
		gridBagConstraints.gridy = 6;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(1, 4, 4, 10);
		add(JCBElectricityUnit, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 7;
		gridBagConstraints.gridy = 7;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(1, 4, 4, 10);
		add(JCBGasUnit, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 7;
		gridBagConstraints.gridy = 8;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(1, 4, 4, 10);
		add(JCBHeatingOilUnit, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 7;
		gridBagConstraints.gridy = 9;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(1, 4, 4, 10);
		add(JCBCoalUnit, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 7;
		gridBagConstraints.gridy = 10;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(1, 4, 4, 10);
		add(JCBWoodLogsUnit, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 7;
		gridBagConstraints.gridy = 11;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(1, 4, 4, 10);
		add(JCBWoodChipsUnit, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 7;
		gridBagConstraints.gridy = 12;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(1, 4, 4, 10);
		add(JCBWoodPelletsUnit, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 4;
		gridBagConstraints.gridy = 14;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 0);
		calculateButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
		calculateButton.setBackground(new Color(92, 184, 92));
		calculateButton.setForeground(Color.WHITE);
		calculateButton.addActionListener(actionListener);
		calculateButton.setActionCommand("CALCULATE");
		add(calculateButton, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 4;
		gridBagConstraints.gridy = 16;
		gridBagConstraints.gridwidth = 4;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 0);
		JTFResult.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
		JTFResult.setEditable(false);
		add(JTFResult, gridBagConstraints);

		JPanel jpanel = new JPanel();

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridwidth = 1;
		// gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		// gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 0);
		saveButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
		saveButton.addActionListener(actionListener);
		saveButton.setActionCommand("SAVE");
		jpanel.add(saveButton, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 4;
		gridBagConstraints.gridy = 18;
		gridBagConstraints.gridwidth = 1;
		// gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 0);
		add(jpanel, gridBagConstraints);

		setBackground(Color.WHITE);

	}

	private Boolean checkRequiredFields() {
		boolean isAllOk = false;

		if (JCBClient.getSelectedIndex() != -1) {
			clientName = JCBClient.getSelectedItem().toString().trim();
			System.out.println(clientName);
			isAllOk = true;
		} else
			isAllOk = false;

		fromDate = (java.sql.Date) fromDatePicker.getModel().getValue();
		toDate = (java.sql.Date) toDatePicker.getModel().getValue();
		if (isAllOk && fromDate != null && toDate != null) {
			isAllOk = true;
		} else
			isAllOk = false;

		return isAllOk;
	}

	/**
	 * calculate total emission and
	 * 
	 * @return true if calculation goes well
	 */
	private Boolean calculateTotalEmission() {
		try {

			electricityCO2E = Double.valueOf(JTFElectricity.getText().isEmpty() ? "0.0" : JTFElectricity.getText())
					* energySourceDetail.get(ELECTRICITY).get(JCBElectricityUnit.getSelectedItem().toString());

			gasCO2E = Double.valueOf(JTFGas.getText().isEmpty() ? "0.0" : JTFGas.getText())
					* energySourceDetail.get(GAS).get(JCBGasUnit.getSelectedItem().toString());

			heatingOilCO2E = Double.valueOf(JTFHeatingOil.getText().isEmpty() ? "0.0" : JTFHeatingOil.getText())
					* energySourceDetail.get(HEATINGOIL).get(JCBHeatingOilUnit.getSelectedItem().toString());

			coalCO2E = Double.valueOf(JTFCoal.getText().isEmpty() ? "0.0" : JTFCoal.getText())
					* energySourceDetail.get(COAL).get(JCBCoalUnit.getSelectedItem().toString());

			woodLogsCO2E = Double.valueOf(JTFWoodLogs.getText().isEmpty() ? "0.0" : JTFWoodLogs.getText())
					* energySourceDetail.get(WOODLOGS).get(JCBWoodLogsUnit.getSelectedItem().toString());

			woodChipsCO2E = Double.valueOf(JTFWoodChips.getText().isEmpty() ? "0.0" : JTFWoodChips.getText())
					* energySourceDetail.get(WOODCHIPS).get(JCBWoodChipsUnit.getSelectedItem().toString());

			woodPelletsCO2E = Double.valueOf(JTFWoodPellets.getText().isEmpty() ? "0.0" : JTFWoodPellets.getText())
					* energySourceDetail.get(WOODPELLETS).get(JCBWoodPelletsUnit.getSelectedItem().toString());

		} catch (NumberFormatException ne) {
			System.out.println("Number Format Exception while calculating House Hold Emission" + ne);
			return false;
		}
		//
		totalHouseCO2E = electricityCO2E + gasCO2E + heatingOilCO2E + coalCO2E + woodLogsCO2E + woodChipsCO2E
				+ woodPelletsCO2E;

		totalHouseCO2E = Math.round(totalHouseCO2E * 100D) / 100D;

		return true;
	}

	/*
	 * listens the calculate and save button
	 **/
	ActionListener actionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			switch (e.getActionCommand()) {
			case "CALCULATE":
				if (calculateTotalEmission()) {
					JTFResult.setText("Total GHG emissions from House = " + totalHouseCO2E + " kgs of CO2e");
					JTFResult.repaint();
				} else {
					JOptionPane.showMessageDialog(null,
							"Only Number is allowed. Please check the field and correct it.", "Carbon Calculator",
							JOptionPane.WARNING_MESSAGE);
				}
				break;

			case "SAVE":
				if (checkRequiredFields()) {

					if (calculateTotalEmission()) {

						String sql = "INSERT INTO energyreadingdetail (clientid,energysourceid,unit,from_date,to_date,totalconsumption,conversionfactor,CO2eproduce) VALUES";
						String values = "";
						int cliendId = GlobalValues.clientIDMap.get(JCBClient.getSelectedIndex());
						if (!JTFElectricity.getText().isEmpty()) {
							values += "(" + cliendId + "," + ELECTRICITY + ",'"
									+ JCBElectricityUnit.getSelectedItem().toString() + "',date('" + fromDate
									+ "'),date('" + toDate + "'),"
									+ Double.parseDouble(JTFElectricity.getText().toString()) + ","
									+ energySourceDetail.get(ELECTRICITY)
											.get(JCBElectricityUnit.getSelectedItem().toString())
									+ "," + electricityCO2E + "),";

						}
						if (!JTFGas.getText().isEmpty()) {
							values += "(" + cliendId + "," + GAS + ",'" + JCBGasUnit.getSelectedItem().toString()
									+ "',date('" + fromDate + "'),date('" + toDate + "'),"
									+ Double.parseDouble(JTFGas.getText().toString()) + ","
									+ energySourceDetail.get(GAS).get(JCBGasUnit.getSelectedItem().toString()) + ","
									+ gasCO2E + "),";
						}
						if (!JTFHeatingOil.getText().isEmpty()) {
							values += "(" + cliendId + "," + HEATINGOIL + ",'"
									+ JCBHeatingOilUnit.getSelectedItem().toString() + "',date('" + fromDate
									+ "'),date('" + toDate + "'),"
									+ Double.parseDouble(JTFHeatingOil.getText().toString()) + ","
									+ energySourceDetail.get(HEATINGOIL)
											.get(JCBHeatingOilUnit.getSelectedItem().toString())
									+ "," + heatingOilCO2E + "),";
						}
						if (!JTFCoal.getText().isEmpty()) {
							values += "(" + cliendId + "," + COAL + ",'" + JCBCoalUnit.getSelectedItem().toString()
									+ "',date('" + fromDate + "'),date('" + toDate + "'),"
									+ Double.parseDouble(JTFCoal.getText().toString()) + ","
									+ energySourceDetail.get(COAL).get(JCBCoalUnit.getSelectedItem().toString()) + ","
									+ coalCO2E + "),";
						}
						if (!JTFWoodLogs.getText().isEmpty()) {
							values += "(" + cliendId + "," + WOODLOGS + ",'"
									+ JCBWoodLogsUnit.getSelectedItem().toString() + "',date('" + fromDate + "'),date('"
									+ toDate + "')," + Double.parseDouble(JTFWoodLogs.getText().toString()) + ","
									+ energySourceDetail.get(WOODLOGS).get(JCBWoodLogsUnit.getSelectedItem().toString())
									+ "," + woodLogsCO2E + "),";
						}
						if (!JTFWoodChips.getText().isEmpty()) {
							values += "(" + cliendId + "," + WOODCHIPS + ",'"
									+ JCBWoodChipsUnit.getSelectedItem().toString() + "',date('" + fromDate
									+ "'),date('" + toDate + "'),"
									+ Double.parseDouble(JTFWoodChips.getText().toString()) + "," + energySourceDetail
											.get(WOODCHIPS).get(JCBWoodChipsUnit.getSelectedItem().toString())
									+ "," + woodChipsCO2E + "),";
						}
						if (!JTFWoodPellets.getText().isEmpty()) {
							values += "(" + cliendId + "," + WOODPELLETS + ",'"
									+ JCBWoodPelletsUnit.getSelectedItem().toString() + "',date('" + fromDate
									+ "'),date('" + toDate + "'),"
									+ Double.parseDouble(JTFWoodPellets.getText().toString()) + ","
									+ energySourceDetail.get(WOODPELLETS)
											.get(JCBWoodPelletsUnit.getSelectedItem().toString())
									+ "," + woodLogsCO2E + "),";
						}

						if (values != "") {
							try {
								values = values.substring(0, values.lastIndexOf(","));
								sql += values;

								stHouseDataEntry = cnHouseDataEntry.createStatement();
								System.out.println(sql);
								stHouseDataEntry.execute(sql);

								String energyreadingmeterSQL = "Insert into energyreadingmeter(clientid,entry_date,totalCO2E) VALUES(";
								energyreadingmeterSQL += cliendId + "," + "date('now')," + totalHouseCO2E + ")";
								System.out.println(energyreadingmeterSQL);
								stHouseDataEntry.execute(energyreadingmeterSQL);

								JTFResult
										.setText("Total GHG emissions from House = " + totalHouseCO2E + " kgs of CO2e");
								JTFResult.repaint();

								JOptionPane.showMessageDialog(null, "Data Has been succesfully saved.",
										"Carbon Calculator", JOptionPane.INFORMATION_MESSAGE);
							} catch (SQLException e1) {
								e1.printStackTrace();
							}

						}
					} else {
						JOptionPane.showMessageDialog(null,
								"Only Number is allowed. Please check the field and correct it.", "Carbon Calculator",
								JOptionPane.WARNING_MESSAGE);
					}
				} else {
					System.out.println("Not Ready to insert");
					JOptionPane.showMessageDialog(null, "Please fill-up all the required fields.", "Carbon Calculator",
							JOptionPane.WARNING_MESSAGE);
				}

				break;

			default:
				break;
			}

		}
	};
}
