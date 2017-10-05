package com.carboncalculator.forms.dataentry;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
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

import com.carboncalculator.forms.MainFrame;
import com.carboncalculator.utility.DateLabelFormatter;
import com.carboncalculator.utility.GlobalValues;
import com.carboncalculator.utility.PublicMethods;

public class FoodDataEntry extends JPanel {

	public static JFrame JFParentFrame;
	private Connection cnFoodDataEntry;
	public static Statement stFoodDataEntry;

	public static ResultSet rsFoodDataEntry;

	public FoodDataEntry(JFrame mFrame, Connection SRCN) {
		this.JFParentFrame = mFrame;
		this.cnFoodDataEntry = SRCN;
		initiaLizeRequirements();
		initComponents();

	}

	JLabel JLImage;

	JLabel JLHeader = new JLabel("Food carbon footprint calculator");
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

	JLabel JLFood = new JLabel("Food:");
	JLabel JLLandfillingFoodWaste = new JLabel("Landfilling Food Waste:");
	JLabel JLCompostingFoodWaste = new JLabel("Composting Food Waste:");

	JTextField JTFFood = new JTextField();
	JTextField JTFLandfillingFoodWaste = new JTextField();
	JTextField JTFCompostingFoodWaste = new JTextField();

	JComboBox JCBFoodUnit;
	JComboBox JCBLandfillingFoodWasteUnit;
	JComboBox JCBCompostingFoodWasteUnit;

	JButton calculateButton = new JButton("Calculate Food Footprint");
	JTextField JTFResult = new JTextField("Total GHG emissions from Food = 0.00 kgs of CO2e");
	JButton saveButton = new JButton("Save");

	// This map contains energysourceid with unit and conversionfactor
	private Map<Integer, Map<String, Double>> energySourceDetail;

	private Double foodCO2E = 0.0;
	private Double landfillingFoodWasteCO2E = 0.0;
	private Double compostingFoodWasteCO2E = 0.0;
	public static Double totalFoodCO2E = 0.0;

	// Hardcoded energysourceIDs
	private int FOOD = 2;
	private int FoodIntake = 8;
	private int LandfillingFoodWaste = 9;
	private int CompostingFoodWaste = 10;

	public static String clientName;

	/*
	 * fill up the energySourceDetail map
	 **/
	private void initiaLizeRequirements() {

		try {
			stFoodDataEntry = cnFoodDataEntry.createStatement();

			String sql = "select energysourceid, unit, conversionfactor from energysourcedetail where energysourceid in (select sourceid from energysource where energytypeid ="
					+ FOOD + ") order by energysourceid asc";
			rsFoodDataEntry = stFoodDataEntry.executeQuery(sql);

			Map<String, Double> unitConversionF = null;
			// List<Map<String, Double>> listUnitConversionF;
			energySourceDetail = new LinkedHashMap<Integer, Map<String, Double>>();

			int energysourceId;
			String unit;
			Double coversionFactor;

			while (rsFoodDataEntry.next()) {
				energysourceId = rsFoodDataEntry.getInt("energysourceid");
				unit = rsFoodDataEntry.getString("unit");
				coversionFactor = rsFoodDataEntry.getDouble("conversionfactor");

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
				this.cnFoodDataEntry);

		JCBFoodUnit = new JComboBox<>();
		JCBFoodUnit.setModel(
				new javax.swing.DefaultComboBoxModel<>(energySourceDetail.get(FoodIntake).keySet().toArray()));

		JCBLandfillingFoodWasteUnit = new JComboBox<>();
		JCBLandfillingFoodWasteUnit.setModel(new javax.swing.DefaultComboBoxModel<>(
				energySourceDetail.get(LandfillingFoodWaste).keySet().toArray()));

		JCBCompostingFoodWasteUnit = new JComboBox<>();
		JCBCompostingFoodWasteUnit.setModel(
				new javax.swing.DefaultComboBoxModel<>(energySourceDetail.get(CompostingFoodWaste).keySet().toArray()));

		setLayout(new java.awt.GridBagLayout());

		ImageIcon image = new ImageIcon("images/foodcalculator.jpg");
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
		JLFood.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
		add(JLFood, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 4;
		gridBagConstraints.gridy = 7;
		JLLandfillingFoodWaste.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
		add(JLLandfillingFoodWaste, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 4;
		gridBagConstraints.gridy = 8;
		gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
		JLCompostingFoodWaste.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
		add(JLCompostingFoodWaste, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 5;
		gridBagConstraints.gridy = 6;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 10);
		add(JTFFood, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 5;
		gridBagConstraints.gridy = 7;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 10);
		add(JTFLandfillingFoodWaste, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 5;
		gridBagConstraints.gridy = 8;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 10);
		add(JTFCompostingFoodWaste, gridBagConstraints);

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
		add(JCBFoodUnit, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 7;
		gridBagConstraints.gridy = 7;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(1, 4, 4, 10);
		add(JCBLandfillingFoodWasteUnit, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 7;
		gridBagConstraints.gridy = 8;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(1, 4, 4, 10);
		add(JCBCompostingFoodWasteUnit, gridBagConstraints);

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
		if ((isAllOk && fromDate != null && toDate != null) && (!JTFFood.getText().isEmpty()
				|| !JTFLandfillingFoodWaste.getText().isEmpty() || !JTFCompostingFoodWaste.getText().isEmpty())) {
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

			foodCO2E = Double.valueOf(JTFFood.getText().isEmpty() ? "0.0" : JTFFood.getText())
					* energySourceDetail.get(FoodIntake).get(JCBFoodUnit.getSelectedItem().toString());

			landfillingFoodWasteCO2E = Double
					.valueOf(JTFLandfillingFoodWaste.getText().isEmpty() ? "0.0" : JTFLandfillingFoodWaste.getText())
					* energySourceDetail.get(LandfillingFoodWaste)
							.get(JCBLandfillingFoodWasteUnit.getSelectedItem().toString());

			compostingFoodWasteCO2E = Double
					.valueOf(JTFCompostingFoodWaste.getText().isEmpty() ? "0.0" : JTFCompostingFoodWaste.getText())
					* energySourceDetail.get(CompostingFoodWaste)
							.get(JCBCompostingFoodWasteUnit.getSelectedItem().toString());

		} catch (NumberFormatException ne) {
			System.out.println("Number Format Exception while calculating House Hold Emission" + ne);
			return false;
		}
		//
		totalFoodCO2E = foodCO2E + landfillingFoodWasteCO2E + compostingFoodWasteCO2E;
		totalFoodCO2E = Math.round(totalFoodCO2E * 100D) / 100D;
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
					JTFResult.setText("Total GHG emissions from Food = " + totalFoodCO2E + " kgs of CO2e");
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
						if (!JTFFood.getText().isEmpty()) {
							values += "(" + cliendId + "," + FoodIntake + ",'"
									+ JCBFoodUnit.getSelectedItem().toString() + "',date('" + fromDate + "'),date('"
									+ toDate + "')," + Double.parseDouble(JTFFood.getText().toString()) + ","
									+ energySourceDetail.get(FoodIntake).get(JCBFoodUnit.getSelectedItem().toString())
									+ "," + foodCO2E + "),";

						}
						if (!JTFLandfillingFoodWaste.getText().isEmpty()) {
							values += "(" + cliendId + "," + LandfillingFoodWaste + ",'"
									+ JCBLandfillingFoodWasteUnit.getSelectedItem().toString() + "',date('" + fromDate
									+ "'),date('" + toDate + "'),"
									+ Double.parseDouble(JTFLandfillingFoodWaste.getText().toString()) + ","
									+ energySourceDetail.get(LandfillingFoodWaste)
											.get(JCBLandfillingFoodWasteUnit.getSelectedItem().toString())
									+ "," + landfillingFoodWasteCO2E + "),";
						}
						if (!JTFCompostingFoodWaste.getText().isEmpty()) {
							values += "(" + cliendId + "," + CompostingFoodWaste + ",'"
									+ JCBCompostingFoodWasteUnit.getSelectedItem().toString() + "',date('" + fromDate
									+ "'),date('" + toDate + "'),"
									+ Double.parseDouble(JTFCompostingFoodWaste.getText().toString()) + ","
									+ energySourceDetail.get(CompostingFoodWaste)
											.get(JCBCompostingFoodWasteUnit.getSelectedItem().toString())
									+ "," + compostingFoodWasteCO2E + "),";
						}

						if (values != "") {
							try {
								values = values.substring(0, values.lastIndexOf(","));
								sql += values;

								stFoodDataEntry = cnFoodDataEntry.createStatement();
								System.out.println(sql);
								stFoodDataEntry.execute(sql);

								String energyreadingmeterSQL = "Insert into energyreadingmeter(clientid,entry_date,totalCO2E) VALUES(";
								energyreadingmeterSQL += cliendId + "," + "date('now')," + totalFoodCO2E + ")";
								System.out.println(energyreadingmeterSQL);
								stFoodDataEntry.execute(energyreadingmeterSQL);

								JTFResult.setText("Total GHG emissions from Food = " + totalFoodCO2E + " kgs of CO2e");
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
