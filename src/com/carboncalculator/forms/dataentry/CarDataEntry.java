package com.carboncalculator.forms.dataentry;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

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

public class CarDataEntry extends javax.swing.JPanel {

	public static JFrame JFParentFrame;
	private Connection cnCarDataEntry;
	public static Statement stCarDataEntry;
	public static ResultSet rsCarDataEntry;

	/**
	 * Creates new form CarDataEntry
	 */
	public CarDataEntry(JFrame mFrame, Connection SRCN) {
		this.JFParentFrame = mFrame;
		this.cnCarDataEntry = SRCN;
		initiaLizeRequirements(CAR, energySourceCarDetail, true);
		initiaLizeRequirements(TRANSPORT, energySourceTransportDetail, false);
		initComponents();

	}

	JLabel JLImage;
	JLabel JLHeader = new JLabel("Car footprint calculator");
	JLabel JLBanner = new JLabel("Please fill-up all the required fields.");

	JLabel JLDateRange = new JLabel("Select the Date Range:");
	JLabel JLFromDate = new JLabel("Start Date:");
	JLabel JLToDate = new JLabel("End Date:");

	JLabel JLSelectClient = new JLabel("Select Client:");

	JComboBox JCBClient;

	JDatePickerImpl fromDatePicker;
	JDatePickerImpl toDatePicker;

	Date fromDate;
	Date toDate;

	JLabel JLChooseCar = new JLabel("Choose your Car:");
	JLabel JLMileage = new JLabel("Mileage:");
	JLabel JLEfficiency = new JLabel("Or Enter Efficiency:");

	JTextField JTFMileage = new JTextField();
	JTextField JTFEfficiency = new JTextField();

	JComboBox JCBCar;
	JComboBox JCBCarUnit;
	JComboBox JCBCarType;
	JComboBox JCBCarTypeUnit;

	JButton calculateButton = new JButton("Calculate Car Footprint");
	JTextField JTFResult = new JTextField("Total GHG emissions from Car = 0.00 kgs of CO2e");
	JButton saveButton = new JButton("Save");

	// This map contains energysourceid with unit and conversionfactor
	private Map<Integer, Map<String, Double>> energySourceCarDetail;
	// This map contains energysourceid with unit and conversionfactor
	private Map<Integer, Map<String, Double>> energySourceTransportDetail;

	private Set<String> carList;
	private Set<String> transportList;
	private Set<String> carUnitList;
	private Set<String> transportUnitList;

	private Double TransportCO2E = 0.0;
	private Double CarCO2E = 0.0;

	public static Double totalCarCO2E = 0.0;

	// Hardcoded energysourceIDs
	private int TRANSPORT = 3;
	private int TRANSPORTOFFSET = 11;
	private int PETROL = 11;
	private int DIESEL = 12;
	private int NATURALGAS = 13;
	private int LPG = 14;

	// Hardcoded energysourceIDs
	private int CAR = 3;
	private int CAROFFSET = 15;
	private int SMALLPETROLCAR = 15;
	private int MEDIUMPETROLCAR = 16;
	private int LARGEPETROLCAR = 17;
	private int AVERAGEPETROLCAR = 18;
	private int SMALLDIESELCAR = 19;
	private int MEDIUMDIESELCAR = 20;
	private int LARGEDIESELCAR = 21;
	private int AVERAGEDIESELCAR = 22;
	private int AVERAGESMALLCARU = 23;
	private int AVERAGEMEDIUMCARU = 24;
	private int AVERAGELARGECARU = 25;
	private int AVERAGECARU = 26;

	public static String clientName;

	// End of variables declaration

	/*
	 * fill up the energySourceDetail map
	 **/
	private void initiaLizeRequirements(int id, Map<Integer, Map<String, Double>> energySourceDetail, Boolean isCar) {

		try {
			stCarDataEntry = cnCarDataEntry.createStatement();

			String filter = "and sourceid";
			if (isCar) {
				filter += ">=";
			} else {
				filter += "<";
			}

			filter += CAROFFSET;

			String sql = "select a.energysourceid energysourceid,b.description description, a.unit unit,a.conversionfactor conversionfactor from((select * from energysourcedetail) a join  (select  * from energysource where energytypeid ="
					+ id + ") b on a.energysourceid=b.sourceid) order by a.energysourceid asc";
			rsCarDataEntry = stCarDataEntry.executeQuery(sql);

			Map<String, Double> unitConversionF = null;
			// List<Map<String, Double>> listUnitConversionF;
			energySourceDetail = new LinkedHashMap<Integer, Map<String, Double>>();
			Set<String> descriptions = new LinkedHashSet<>();

			int energysourceId;
			String unit;
			Double coversionFactor;

			while (rsCarDataEntry.next()) {
				energysourceId = rsCarDataEntry.getInt("energysourceid");
				unit = rsCarDataEntry.getString("unit");
				coversionFactor = rsCarDataEntry.getDouble("conversionfactor");

				if (energySourceDetail.containsKey(energysourceId)) {
					energySourceDetail.remove(energysourceId);
				} else {
					unitConversionF = new LinkedHashMap<String, Double>();
				}
				unitConversionF.put(unit, coversionFactor);
				energySourceDetail.put(energysourceId, unitConversionF);

				descriptions.add(rsCarDataEntry.getString("description"));

			}

			if (isCar) {
				carList = descriptions;
				energySourceCarDetail = energySourceDetail;
			} else {
				transportList = descriptions;
				energySourceTransportDetail = energySourceDetail;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/* initializes UI components */
	private void initComponents() {

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

		JCBClient = PublicMethods.fillComboClient(
				"Select clientId, firstName, middleName, lastName from clients order by firstName asc, middlename asc, lastname asc",
				this.cnCarDataEntry);

		JCBCar = new JComboBox<>();
		JCBCar.setModel(new javax.swing.DefaultComboBoxModel<>(carList.toArray()));
		JCBCar.addActionListener(actionListener);
		JCBCar.setActionCommand("CAR");

		JCBCarType = new JComboBox<>();
		JCBCarType.setModel(new javax.swing.DefaultComboBoxModel<>(transportList.toArray()));
		JCBCarType.addActionListener(actionListener);
		JCBCarType.setActionCommand("CARTYPE");

		GridBagConstraints gridBagConstraints;

		setLayout(new java.awt.GridBagLayout());

		ImageIcon image = new ImageIcon("images/carcalculator.jpg");
		JLImage = new JLabel("", image, JLabel.CENTER);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridwidth = 4;
		gridBagConstraints.gridheight = 17;
		add(JLImage, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 4;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridwidth = 4;
		gridBagConstraints.insets = new java.awt.Insets(5, 1, 5, 1);
		JLHeader.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 35));
		add(JLHeader, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 4;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.insets = new java.awt.Insets(5, 1, 5, 1);
		JLDateRange.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
		add(JLDateRange, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 7;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
		gridBagConstraints.insets = new java.awt.Insets(5, 1, 5, 1);
		JLSelectClient.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
		add(JLSelectClient, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 7;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
		gridBagConstraints.insets = new java.awt.Insets(5, 1, 5, 1);
		JCBClient.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
		add(JCBClient, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 4;
		gridBagConstraints.gridy = 2;
		JLFromDate.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
		gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
		add(JLFromDate, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 5;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(5, 4, 5, 4);
		add(fromDatePicker, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 4;
		gridBagConstraints.gridy = 3;
		JLToDate.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
		gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
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
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
		gridBagConstraints.insets = new java.awt.Insets(5, 1, 5, 1);
		JLChooseCar.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
		add(JLChooseCar, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 4;
		gridBagConstraints.gridy = 7;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
		gridBagConstraints.insets = new java.awt.Insets(5, 1, 5, 1);
		JLMileage.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
		add(JLMileage, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 4;
		gridBagConstraints.gridy = 8;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
		gridBagConstraints.insets = new java.awt.Insets(5, 1, 5, 1);
		JLEfficiency.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
		add(JLEfficiency, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 5;
		gridBagConstraints.gridy = 6;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(5, 4, 5, 4);
		JCBCar.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
		add(JCBCar, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 5;
		gridBagConstraints.gridy = 7;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(5, 4, 5, 4);
		JTFMileage.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
		add(JTFMileage, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 5;
		gridBagConstraints.gridy = 8;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(5, 4, 5, 4);
		JTFEfficiency.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
		add(JTFEfficiency, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 7;
		gridBagConstraints.gridy = 7;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
		gridBagConstraints.insets = new java.awt.Insets(5, 4, 5, 4);
		JCBCarUnit = new JComboBox<>();
		JCBCarUnit.setModel(new javax.swing.DefaultComboBoxModel<>(
				energySourceCarDetail.get(JCBCar.getSelectedIndex() + CAROFFSET).keySet().toArray()));
		add(JCBCarUnit, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 7;
		gridBagConstraints.gridy = 8;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
		gridBagConstraints.insets = new java.awt.Insets(5, 4, 5, 4);
		add(JCBCarType, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 8;
		gridBagConstraints.gridy = 8;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
		gridBagConstraints.insets = new java.awt.Insets(5, 4, 5, 4);
		JCBCarTypeUnit = new JComboBox<>();
		JCBCarTypeUnit.setModel(new javax.swing.DefaultComboBoxModel<>(
				energySourceTransportDetail.get(JCBCarType.getSelectedIndex() + TRANSPORTOFFSET).keySet().toArray()));
		add(JCBCarTypeUnit, gridBagConstraints);

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
		if ((isAllOk && fromDate != null && toDate != null)
				&& (!JTFEfficiency.getText().isEmpty() || !JTFMileage.getText().isEmpty())) {
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

			if (!JTFEfficiency.getText().isEmpty()) {
				double transportFactor = energySourceTransportDetail
						.get(JCBCarType.getSelectedIndex() + TRANSPORTOFFSET)
						.get(JCBCarTypeUnit.getSelectedItem().toString());
				TransportCO2E = Double.valueOf(JTFEfficiency.getText().isEmpty() ? "0.0" : JTFEfficiency.getText())
						* transportFactor;
				totalCarCO2E = TransportCO2E;
			} else {
				double carFactor = energySourceCarDetail.get(JCBCar.getSelectedIndex() + CAROFFSET)
						.get(JCBCarUnit.getSelectedItem().toString());
				CarCO2E = Double.valueOf(JTFMileage.getText().isEmpty() ? "0.0" : JTFMileage.getText()) * carFactor;
				totalCarCO2E = CarCO2E;
			}

		} catch (NumberFormatException ne) {
			System.out.println("Number Format Exception while calculating Car Emission" + ne);
			return false;
		}

		totalCarCO2E = Math.round(totalCarCO2E * 100D) / 100D;

		return true;
	}

	/*
	 * listens the calculate and save button
	 **/
	ActionListener actionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			switch (e.getActionCommand()) {
			case "CAR":
				JCBCarUnit.setModel(new javax.swing.DefaultComboBoxModel<>(
						energySourceCarDetail.get(JCBCar.getSelectedIndex() + CAROFFSET).keySet().toArray()));
				JCBCarUnit.repaint();
				break;

			case "CARTYPE":
				JCBCarTypeUnit.setModel(new javax.swing.DefaultComboBoxModel<>(energySourceTransportDetail
						.get(JCBCarType.getSelectedIndex() + TRANSPORTOFFSET).keySet().toArray()));
				JCBCarTypeUnit.repaint();
				break;

			case "CALCULATE":
				if (calculateTotalEmission()) {
					JTFResult.setText("Total GHG emissions from Car = " + totalCarCO2E + " kgs of CO2e");
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
						if (!JTFEfficiency.getText().isEmpty()) {

							System.out.println("Date: " + fromDate);
							values += "(" + cliendId + "," + (JCBCarType.getSelectedIndex() + TRANSPORTOFFSET) + ",'"
									+ JCBCarTypeUnit.getSelectedItem().toString() + "',date('" + fromDate + "'),date('"
									+ toDate + "')," + Double.parseDouble(JTFEfficiency.getText().toString()) + ","
									+ energySourceTransportDetail.get(JCBCarType.getSelectedIndex() + TRANSPORTOFFSET)
											.get(JCBCarTypeUnit.getSelectedItem().toString())
									+ "," + TransportCO2E + "),";

						} else if (!JTFMileage.getText().isEmpty()) {
							System.out.println("Date: " + fromDate);
							values += "(" + cliendId + "," + (JCBCar.getSelectedIndex() + CAROFFSET) + ",'"
									+ JCBCarUnit.getSelectedItem().toString() + "',date('" + fromDate + "'),date('"
									+ toDate + "')," + Double.parseDouble(JTFMileage.getText().toString()) + ","
									+ energySourceCarDetail.get(JCBCar.getSelectedIndex() + CAROFFSET)
											.get(JCBCarUnit.getSelectedItem().toString())
									+ "," + CarCO2E + "),";
						}

						if (values != "") {
							try {
								values = values.substring(0, values.lastIndexOf(","));
								sql += values;

								stCarDataEntry = cnCarDataEntry.createStatement();
								System.out.println(sql);
								stCarDataEntry.execute(sql);

								String energyreadingmeterSQL = "Insert into energyreadingmeter(clientid,entry_date,totalCO2E) VALUES(";
								energyreadingmeterSQL += cliendId + "," + "date('now')," + totalCarCO2E + ")";
								System.out.println(energyreadingmeterSQL);
								stCarDataEntry.execute(energyreadingmeterSQL);

								JTFResult.setText("Total GHG emissions from Car = " + totalCarCO2E + " kgs of CO2e");
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
