package com.carboncalculator.forms.dataentry;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.Date;
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

public class LocalTransportDataEntry extends JPanel {

	public static JFrame JFParentFrame;
	private Connection cnLocalTransportDataEntry;
	public static Statement stLocalTransportDataEntry;
	public static ResultSet rsLocalTransportDataEntry;

	public LocalTransportDataEntry(JFrame mFrame, Connection SRCN) {
		this.JFParentFrame = mFrame;
		this.cnLocalTransportDataEntry = SRCN;
		initiaLizeRequirements();
		initComponents();

	}

	JLabel JLImage;

	JLabel JLHeader = new JLabel("Local Transport Carbon footprint calculator");
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

	JLabel JLLocalBus = new JLabel("Local bus (not London):");
	JLabel JLCoach = new JLabel("Coach:");
	JLabel JLNationalRail = new JLabel("National rail:");
	JLabel JLFerryFoot = new JLabel("Ferry Foot passengers:");
	JLabel JLFerryCar = new JLabel("Ferry Car passengers:");
	JLabel JLRegularTaxi = new JLabel("Regular	taxi:");
	JLabel JLBlackCab = new JLabel("Black cab:");

	JTextField JTFLocalBus = new JTextField();
	JTextField JTFCoach = new JTextField();
	JTextField JTFNationalRail = new JTextField();
	JTextField JTFFerryFoot = new JTextField();
	JTextField JTFFerryCar = new JTextField();
	JTextField JTFRegularTaxi = new JTextField();
	JTextField JTFBlackCab = new JTextField();

	JComboBox JCBLocalBusUnit;
	JComboBox JCBCoachUnit;
	JComboBox JCBNationalRailUnit;
	JComboBox JCBFerryFootUnit;
	JComboBox JCBFerryCarUnit;
	JComboBox JCBRegularTaxiUnit;
	JComboBox JCBBlackCabUnit;

	JButton calculateButton = new JButton("Calculate Local Transport Footprint");
	JTextField JTFResult = new JTextField("Total GHG emissions from Local Transport = 0.00 kgs of CO2e");
	JButton saveButton = new JButton("Save");

	// This map contains energysourceid with unit and conversionfactor
	private Map<Integer, Map<String, Double>> energySourceDetail;

	private Double LocalBusCO2E = 0.0;
	private Double CoachCO2E = 0.0;
	private Double NationalRailCO2E = 0.0;
	private Double FerryFootCO2E = 0.0;
	private Double FerryCarCO2E = 0.0;
	private Double RegularTaxiCO2E = 0.0;
	private Double BlackCabCO2E = 0.0;

	public static Double totalLocalTransportCO2E = 0.0;

	// Hardcoded energysourceIDs
	private int LOCALTRANSPORT = 4;
	private int LOCALBUS = 27;
	private int COACH = 28;
	private int NATIONALRAIL = 29;
	private int FERRYFOOT = 30;
	private int FERRYCAB = 31;
	private int REGULARTAXI = 32;
	private int BLACKCAB = 33;

	public static String clientName;

	/*
	 * fill up the energySourceDetail map
	 **/
	private void initiaLizeRequirements() {

		try {
			stLocalTransportDataEntry = cnLocalTransportDataEntry.createStatement();

			String sql = "select energysourceid, unit, conversionfactor from energysourcedetail where energysourceid in (select sourceid from energysource where energytypeid ="
					+ LOCALTRANSPORT + ") order by energysourceid asc";
			rsLocalTransportDataEntry = stLocalTransportDataEntry.executeQuery(sql);

			Map<String, Double> unitConversionF = null;
			// List<Map<String, Double>> listUnitConversionF;
			energySourceDetail = new LinkedHashMap<Integer, Map<String, Double>>();

			int energysourceId;
			String unit;
			Double coversionFactor;

			while (rsLocalTransportDataEntry.next()) {
				energysourceId = rsLocalTransportDataEntry.getInt("energysourceid");
				unit = rsLocalTransportDataEntry.getString("unit");
				coversionFactor = rsLocalTransportDataEntry.getDouble("conversionfactor");

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

		// Don't know about the formatter, but there it is...
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
				"Select clientId, firstName, middleName, lastName from clients order by firstName asc, middlename asc, lastname asc",
				this.cnLocalTransportDataEntry);

		JCBLocalBusUnit = new JComboBox<>();
		JCBLocalBusUnit
				.setModel(new javax.swing.DefaultComboBoxModel<>(energySourceDetail.get(LOCALBUS).keySet().toArray()));

		JCBCoachUnit = new JComboBox<>();
		JCBCoachUnit.setModel(new javax.swing.DefaultComboBoxModel<>(energySourceDetail.get(COACH).keySet().toArray()));

		JCBNationalRailUnit = new JComboBox<>();
		JCBNationalRailUnit.setModel(
				new javax.swing.DefaultComboBoxModel<>(energySourceDetail.get(NATIONALRAIL).keySet().toArray()));

		JCBFerryFootUnit = new JComboBox<>();
		JCBFerryFootUnit
				.setModel(new javax.swing.DefaultComboBoxModel<>(energySourceDetail.get(FERRYFOOT).keySet().toArray()));

		JCBFerryCarUnit = new JComboBox<>();
		JCBFerryCarUnit
				.setModel(new javax.swing.DefaultComboBoxModel<>(energySourceDetail.get(FERRYCAB).keySet().toArray()));

		JCBRegularTaxiUnit = new JComboBox<>();
		JCBRegularTaxiUnit.setModel(
				new javax.swing.DefaultComboBoxModel<>(energySourceDetail.get(REGULARTAXI).keySet().toArray()));

		JCBBlackCabUnit = new JComboBox<>();
		JCBBlackCabUnit
				.setModel(new javax.swing.DefaultComboBoxModel<>(energySourceDetail.get(BLACKCAB).keySet().toArray()));

		setLayout(new java.awt.GridBagLayout());

		ImageIcon image = new ImageIcon("images/localtransportcalculator.jpg");
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
		JLHeader.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 30));
		gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 0);
		add(JLHeader, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 7;
		gridBagConstraints.gridy = 1;
		JLSelectClient.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
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
		JLDateRange.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
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
		gridBagConstraints.insets = new java.awt.Insets(5, 4, 5, 4);
		add(toDatePicker, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 4;
		gridBagConstraints.gridy = 6;
		gridBagConstraints.insets = new java.awt.Insets(5, 4, 5, 4);
		gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
		JLLocalBus.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
		add(JLLocalBus, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 4;
		gridBagConstraints.gridy = 7;
		gridBagConstraints.insets = new java.awt.Insets(5, 4, 5, 4);
		gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
		JLCoach.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
		add(JLCoach, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 4;
		gridBagConstraints.gridy = 8;
		gridBagConstraints.insets = new java.awt.Insets(5, 4, 5, 4);
		gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
		JLNationalRail.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
		add(JLNationalRail, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 4;
		gridBagConstraints.gridy = 9;
		gridBagConstraints.insets = new java.awt.Insets(5, 4, 5, 4);
		gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
		JLFerryFoot.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
		add(JLFerryFoot, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 4;
		gridBagConstraints.gridy = 10;
		gridBagConstraints.insets = new java.awt.Insets(5, 4, 5, 4);
		gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
		JLFerryCar.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
		add(JLFerryCar, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 4;
		gridBagConstraints.gridy = 11;
		gridBagConstraints.insets = new java.awt.Insets(5, 4, 5, 4);
		gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
		JLRegularTaxi.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
		add(JLRegularTaxi, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 4;
		gridBagConstraints.gridy = 12;
		gridBagConstraints.insets = new java.awt.Insets(5, 4, 5, 4);
		gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
		JLBlackCab.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
		add(JLBlackCab, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 5;
		gridBagConstraints.gridy = 6;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(5, 4, 5, 10);
		// JTFLocalBus.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
		add(JTFLocalBus, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 5;
		gridBagConstraints.gridy = 7;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(5, 4, 5, 10);
		// JTFCoach.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
		add(JTFCoach, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 5;
		gridBagConstraints.gridy = 8;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(5, 4, 5, 10);
		// JTFNationalRail.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
		add(JTFNationalRail, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 5;
		gridBagConstraints.gridy = 9;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(5, 4, 5, 10);
		// JTFFerryFoot.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
		add(JTFFerryFoot, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 5;
		gridBagConstraints.gridy = 10;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(5, 4, 5, 10);
		// JTFFerryCar.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
		add(JTFFerryCar, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 5;
		gridBagConstraints.gridy = 11;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(5, 4, 5, 10);
		// JTFRegularTaxi.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
		add(JTFRegularTaxi, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 5;
		gridBagConstraints.gridy = 12;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(5, 4, 5, 10);
		// JTFBlackCab.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
		add(JTFBlackCab, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 7;
		gridBagConstraints.gridy = 5;
		JLUnit.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
		add(JLUnit, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 7;
		gridBagConstraints.gridy = 6;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(5, 4, 5, 10);
		add(JCBLocalBusUnit, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 7;
		gridBagConstraints.gridy = 7;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(5, 4, 5, 10);
		add(JCBCoachUnit, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 7;
		gridBagConstraints.gridy = 8;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(5, 4, 5, 10);
		add(JCBNationalRailUnit, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 7;
		gridBagConstraints.gridy = 9;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(5, 4, 5, 10);
		add(JCBFerryFootUnit, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 7;
		gridBagConstraints.gridy = 10;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(5, 4, 5, 10);
		add(JCBFerryCarUnit, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 7;
		gridBagConstraints.gridy = 11;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(5, 4, 5, 10);
		add(JCBRegularTaxiUnit, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 7;
		gridBagConstraints.gridy = 12;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(5, 4, 5, 10);
		add(JCBBlackCabUnit, gridBagConstraints);

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
		saveButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
		saveButton.addActionListener(actionListener);
		saveButton.setActionCommand("SAVE");
		jpanel.add(saveButton, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 4;
		gridBagConstraints.gridy = 18;
		gridBagConstraints.gridwidth = 1;
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
		if ((isAllOk && fromDate != null && toDate != null) && (!JTFLocalBus.getText().isEmpty()
				|| !JTFCoach.getText().isEmpty() || !JTFNationalRail.getText().isEmpty()
				|| !JTFFerryFoot.getText().isEmpty() || !JTFFerryCar.getText().isEmpty()
				|| !JTFRegularTaxi.getText().isEmpty() || !JTFBlackCab.getText().isEmpty())) {
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

			LocalBusCO2E = Double.valueOf(JTFLocalBus.getText().isEmpty() ? "0.0" : JTFLocalBus.getText())
					* energySourceDetail.get(LOCALBUS).get(JCBLocalBusUnit.getSelectedItem().toString());

			CoachCO2E = Double.valueOf(JTFCoach.getText().isEmpty() ? "0.0" : JTFCoach.getText())
					* energySourceDetail.get(COACH).get(JCBCoachUnit.getSelectedItem().toString());

			NationalRailCO2E = Double.valueOf(JTFNationalRail.getText().isEmpty() ? "0.0" : JTFNationalRail.getText())
					* energySourceDetail.get(NATIONALRAIL).get(JCBNationalRailUnit.getSelectedItem().toString());

			FerryFootCO2E = Double.valueOf(JTFFerryFoot.getText().isEmpty() ? "0.0" : JTFFerryFoot.getText())
					* energySourceDetail.get(FERRYFOOT).get(JCBFerryFootUnit.getSelectedItem().toString());

			FerryCarCO2E = Double.valueOf(JTFFerryCar.getText().isEmpty() ? "0.0" : JTFFerryCar.getText())
					* energySourceDetail.get(FERRYCAB).get(JCBFerryCarUnit.getSelectedItem().toString());

			RegularTaxiCO2E = Double.valueOf(JTFRegularTaxi.getText().isEmpty() ? "0.0" : JTFRegularTaxi.getText())
					* energySourceDetail.get(REGULARTAXI).get(JCBRegularTaxiUnit.getSelectedItem().toString());

			BlackCabCO2E = Double.valueOf(JTFBlackCab.getText().isEmpty() ? "0.0" : JTFBlackCab.getText())
					* energySourceDetail.get(BLACKCAB).get(JCBBlackCabUnit.getSelectedItem().toString());

		} catch (NumberFormatException ne) {
			System.out.println("Number Format Exception while calculating House Hold Emission" + ne);
			return false;
		}
		//
		totalLocalTransportCO2E = LocalBusCO2E + CoachCO2E + NationalRailCO2E + FerryFootCO2E + FerryCarCO2E
				+ RegularTaxiCO2E + BlackCabCO2E;

		totalLocalTransportCO2E = Math.round(totalLocalTransportCO2E * 100D) / 100D;

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
					JTFResult.setText(
							"Total GHG emissions from Local Transport = " + totalLocalTransportCO2E + " kgs of CO2e");
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
						if (!JTFLocalBus.getText().isEmpty()) {
							values += "(" + cliendId + "," + LOCALBUS + ",'"
									+ JCBLocalBusUnit.getSelectedItem().toString() + "',date('" + fromDate + "'),date('"
									+ toDate + "')," + Double.parseDouble(JTFLocalBus.getText().toString()) + ","
									+ energySourceDetail.get(LOCALBUS).get(JCBLocalBusUnit.getSelectedItem().toString())
									+ "," + LocalBusCO2E + "),";

						}
						if (!JTFCoach.getText().isEmpty()) {
							values += "(" + cliendId + "," + COACH + ",'" + JCBCoachUnit.getSelectedItem().toString()
									+ "',date('" + fromDate + "'),date('" + toDate + "'),"
									+ Double.parseDouble(JTFCoach.getText().toString()) + ","
									+ energySourceDetail.get(COACH).get(JCBCoachUnit.getSelectedItem().toString()) + ","
									+ CoachCO2E + "),";
						}
						if (!JTFNationalRail.getText().isEmpty()) {
							values += "(" + cliendId + "," + NATIONALRAIL + ",'"
									+ JCBNationalRailUnit.getSelectedItem().toString() + "',date('" + fromDate
									+ "'),date('" + toDate + "'),"
									+ Double.parseDouble(JTFNationalRail.getText().toString()) + ","
									+ energySourceDetail.get(NATIONALRAIL)
											.get(JCBNationalRailUnit.getSelectedItem().toString())
									+ "," + NationalRailCO2E + "),";
						}
						if (!JTFFerryFoot.getText().isEmpty()) {
							values += "(" + cliendId + "," + FERRYFOOT + ",'"
									+ JCBFerryFootUnit.getSelectedItem().toString() + "',date('" + fromDate
									+ "'),date('" + toDate + "'),"
									+ Double.parseDouble(JTFFerryFoot.getText().toString()) + "," + energySourceDetail
											.get(FERRYFOOT).get(JCBFerryFootUnit.getSelectedItem().toString())
									+ "," + FerryFootCO2E + "),";
						}
						if (!JTFFerryCar.getText().isEmpty()) {
							values += "(" + cliendId + "," + FERRYCAB + ",'"
									+ JCBFerryCarUnit.getSelectedItem().toString() + "',date('" + fromDate + "'),date('"
									+ toDate + "')," + Double.parseDouble(JTFFerryCar.getText().toString()) + ","
									+ energySourceDetail.get(FERRYCAB).get(JCBFerryCarUnit.getSelectedItem().toString())
									+ "," + FerryCarCO2E + "),";
						}
						if (!JTFRegularTaxi.getText().isEmpty()) {
							values += "(" + cliendId + "," + REGULARTAXI + ",'"
									+ JCBRegularTaxiUnit.getSelectedItem().toString() + "',date('" + fromDate
									+ "'),date('" + toDate + "'),"
									+ Double.parseDouble(JTFRegularTaxi.getText().toString()) + ","
									+ energySourceDetail.get(REGULARTAXI)
											.get(JCBRegularTaxiUnit.getSelectedItem().toString())
									+ "," + RegularTaxiCO2E + "),";
						}
						if (!JTFBlackCab.getText().isEmpty()) {
							values += "(" + cliendId + "," + BLACKCAB + ",'"
									+ JCBBlackCabUnit.getSelectedItem().toString() + "',date('" + fromDate + "'),date('"
									+ toDate + "')," + Double.parseDouble(JTFBlackCab.getText().toString()) + ","
									+ energySourceDetail.get(BLACKCAB).get(JCBBlackCabUnit.getSelectedItem().toString())
									+ "," + BlackCabCO2E + "),";
						}

						if (values != "") {
							try {
								values = values.substring(0, values.lastIndexOf(","));
								sql += values;

								stLocalTransportDataEntry = cnLocalTransportDataEntry.createStatement();
								System.out.println(sql);
								stLocalTransportDataEntry.execute(sql);

								String energyreadingmeterSQL = "Insert into energyreadingmeter(clientid,entry_date,totalCO2E) VALUES(";
								energyreadingmeterSQL += cliendId + "," + "date('now')," + totalLocalTransportCO2E
										+ ")";
								System.out.println(energyreadingmeterSQL);
								stLocalTransportDataEntry.execute(energyreadingmeterSQL);

								JTFResult.setText("Total GHG emissions from Local Transport = "
										+ totalLocalTransportCO2E + " kgs of CO2e");
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
