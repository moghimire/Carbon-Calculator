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

public class LandFillingDataEntry extends JPanel {

	public static JFrame JFParentFrame;
	private Connection cnOtherMaterialsDataEntry;
	public static Statement stOtherMaterialsDataEntry;

	public static ResultSet rsOtherMaterialsDataEntry;

	public LandFillingDataEntry(JFrame mFrame, Connection SRCN) {
		this.JFParentFrame = mFrame;
		this.cnOtherMaterialsDataEntry = SRCN;
		initiaLizeRequirements();
		initComponents();

	}

	JLabel JLImage;

	JLabel JLHeader = new JLabel("Landfilling carbon footprint calculator");
	JLabel JLBanner = new JLabel("Please fill-up all the required fields.");

	JLabel JLDateRange = new JLabel("Select the Date Range:");
	JLabel JLFromDate = new JLabel("Start Date:");
	JLabel JLToDate = new JLabel("End Date:");

	JLabel JLSelectClient = new JLabel("Select Client:");
	JLabel JLUnit1 = new JLabel("Units:");
	JLabel JLUnit2 = new JLabel("Units:");

	JComboBox JCBClient;

	JDatePickerImpl fromDatePicker;
	JDatePickerImpl toDatePicker;

	Date fromDate;
	Date toDate;

	JLabel JLSmallWEEE = new JLabel("Small WEEE:");
	JLabel JLLargeWEE = new JLabel("Large WEEE:");
	JLabel JLTxtNFootW = new JLabel("Textiles and footwear:");
	JLabel JLAluminium = new JLabel("Aluminium:");
	JLabel JLSteel = new JLabel("Steel:");
	JLabel JLMixCans = new JLabel("Mixed cans:");
	JLabel JLMetal = new JLabel("Scrap metal:");
	JLabel JLPlasticBottles = new JLabel("Plastic bottles:");
	JLabel JLPlasticBags = new JLabel("Plastic bags:");
	JLabel JLWood = new JLabel("Wood:");
	JLabel JLPaper = new JLabel("Paper:");
	JLabel JLCard = new JLabel("Card:");
	JLabel JLBooks = new JLabel("Books:");
	JLabel JLRubble = new JLabel("Rubble:");
	JLabel JLGlassColourS = new JLabel("Glass (colour separated):");
	JLabel JLGlassColourM = new JLabel("Glass (Mixed):");

	JTextField JTFSmallWEEE = new JTextField(5);
	JTextField JTFLargeWEE = new JTextField(5);
	JTextField JTFTxtNFootW = new JTextField(5);
	JTextField JTFAluminium = new JTextField(5);
	JTextField JTFSteel = new JTextField(5);
	JTextField JTFMixCans = new JTextField(5);
	JTextField JTFMetal = new JTextField(5);
	JTextField JTFPlasticBottles = new JTextField(5);
	JTextField JTFPlasticBags = new JTextField(5);
	JTextField JTFWood = new JTextField(5);
	JTextField JTFPaper = new JTextField(5);
	JTextField JTFCard = new JTextField(5);
	JTextField JTFBooks = new JTextField(5);
	JTextField JTFRubble = new JTextField(5);
	JTextField JTFGlassColourS = new JTextField(5);
	JTextField JTFGlassColourM = new JTextField(5);

	JComboBox JCBSmallWEEE = new JComboBox();
	JComboBox JCBLargeWEE = new JComboBox();
	JComboBox JCBTxtNFootW = new JComboBox();
	JComboBox JCBAluminium = new JComboBox();
	JComboBox JCBSteel = new JComboBox();
	JComboBox JCBMixCans = new JComboBox();
	JComboBox JCBMetal = new JComboBox();
	JComboBox JCBPlasticBottles = new JComboBox();
	JComboBox JCBPlasticBags = new JComboBox();
	JComboBox JCBWood = new JComboBox();
	JComboBox JCBPaper = new JComboBox();
	JComboBox JCBCard = new JComboBox();
	JComboBox JCBBooks = new JComboBox();
	JComboBox JCBRubble = new JComboBox();
	JComboBox JCBGlassColourS = new JComboBox();
	JComboBox JCBGlassColourM = new JComboBox();

	JButton calculateButton = new JButton("Calculate Landfilling Footprint");
	JTextField JTFResult = new JTextField("Total GHG emissions from Landfilling = 0.00 kgs of CO2e");
	JButton saveButton = new JButton("Save");

	// This map contains energysourceid with unit and conversionfactor
	private Map<Integer, Map<String, Double>> energySourceDetail;

	// Hardcoded energysourceIDs
	private int OTHERMATERIALS = 6;

	private int SMALLWEE = 37;
	private int LARGEWEE = 38;
	private int TXTNFOOTW = 39;
	private int ALUMINIUM = 40;
	private int STEEL = 41;
	private int MIXCANS = 42;
	private int METAL = 43;
	private int PLASTICBOTTLES = 44;
	private int PLASTICBAGS = 45;
	private int WOOD = 46;
	private int PAPER = 47;
	private int CARD = 48;
	private int BOOKS = 49;
	private int RUBBLE = 50;
	private int GLASSCOLORS = 51;
	private int GLASSCOLORM = 52;

	public static String clientName;

	/*
	 * fill up the energySourceDetail map
	 **/
	private void initiaLizeRequirements() {

		try {
			stOtherMaterialsDataEntry = cnOtherMaterialsDataEntry.createStatement();

			String sql = "select energysourceid, unit, conversionfactor from energysourcelandfillingdetail where energysourceid in (select sourceid from energysource where energytypeid ="
					+ OTHERMATERIALS + ") order by energysourceid asc";
			rsOtherMaterialsDataEntry = stOtherMaterialsDataEntry.executeQuery(sql);

			Map<String, Double> unitConversionF = null;
			// List<Map<String, Double>> listUnitConversionF;
			energySourceDetail = new LinkedHashMap<Integer, Map<String, Double>>();

			int energysourceId;
			String unit;
			Double coversionFactor;

			while (rsOtherMaterialsDataEntry.next()) {
				energysourceId = rsOtherMaterialsDataEntry.getInt("energysourceid");
				unit = rsOtherMaterialsDataEntry.getString("unit");
				coversionFactor = rsOtherMaterialsDataEntry.getDouble("conversionfactor");

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
				this.cnOtherMaterialsDataEntry);

		JCBSmallWEEE
				.setModel(new javax.swing.DefaultComboBoxModel<>(energySourceDetail.get(SMALLWEE).keySet().toArray()));

		JCBLargeWEE
				.setModel(new javax.swing.DefaultComboBoxModel<>(energySourceDetail.get(LARGEWEE).keySet().toArray()));

		JCBTxtNFootW
				.setModel(new javax.swing.DefaultComboBoxModel<>(energySourceDetail.get(TXTNFOOTW).keySet().toArray()));

		JCBAluminium
				.setModel(new javax.swing.DefaultComboBoxModel<>(energySourceDetail.get(ALUMINIUM).keySet().toArray()));

		JCBSteel.setModel(new javax.swing.DefaultComboBoxModel<>(energySourceDetail.get(STEEL).keySet().toArray()));

		JCBMixCans.setModel(new javax.swing.DefaultComboBoxModel<>(energySourceDetail.get(MIXCANS).keySet().toArray()));

		JCBMetal.setModel(new javax.swing.DefaultComboBoxModel<>(energySourceDetail.get(METAL).keySet().toArray()));

		JCBPlasticBottles.setModel(
				new javax.swing.DefaultComboBoxModel<>(energySourceDetail.get(PLASTICBOTTLES).keySet().toArray()));

		JCBPlasticBags.setModel(
				new javax.swing.DefaultComboBoxModel<>(energySourceDetail.get(PLASTICBAGS).keySet().toArray()));

		JCBWood.setModel(new javax.swing.DefaultComboBoxModel<>(energySourceDetail.get(WOOD).keySet().toArray()));

		JCBPaper.setModel(new javax.swing.DefaultComboBoxModel<>(energySourceDetail.get(PAPER).keySet().toArray()));

		JCBCard.setModel(new javax.swing.DefaultComboBoxModel<>(energySourceDetail.get(CARD).keySet().toArray()));

		JCBBooks.setModel(new javax.swing.DefaultComboBoxModel<>(energySourceDetail.get(BOOKS).keySet().toArray()));

		JCBRubble.setModel(new javax.swing.DefaultComboBoxModel<>(energySourceDetail.get(RUBBLE).keySet().toArray()));

		JCBGlassColourS.setModel(
				new javax.swing.DefaultComboBoxModel<>(energySourceDetail.get(GLASSCOLORS).keySet().toArray()));

		JCBGlassColourM.setModel(
				new javax.swing.DefaultComboBoxModel<>(energySourceDetail.get(GLASSCOLORM).keySet().toArray()));

		setLayout(new java.awt.GridBagLayout());

		ImageIcon image = new ImageIcon("images/landfillingcalculator.jpg");
		JLImage = new JLabel("", image, JLabel.CENTER);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		// gridBagConstraints.gridwidth = 1;
		gridBagConstraints.gridheight = 17;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		add(JLImage, gridBagConstraints);

		JPanel headerPanel = new JPanel();

		headerPanel.setLayout(new java.awt.GridBagLayout());

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		JLHeader.setFont(new java.awt.Font("SansSerif", Font.BOLD, 30));
		gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 0);
		headerPanel.add(JLHeader, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 0;
		headerPanel.setBackground(Color.WHITE);
		add(headerPanel, gridBagConstraints);

		JPanel selectClientNDate = new JPanel();

		selectClientNDate.setLayout(new java.awt.GridBagLayout());

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
		JLDateRange.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
		gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 0);
		selectClientNDate.add(JLDateRange, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
		JLFromDate.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
		gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 0);
		selectClientNDate.add(JLFromDate, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
		fromDatePicker.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
		gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 0);
		selectClientNDate.add(fromDatePicker, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
		JLToDate.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
		gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 0);
		selectClientNDate.add(JLToDate, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
		toDatePicker.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
		gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 0);
		selectClientNDate.add(toDatePicker, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
		JLSelectClient.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
		gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 0);
		selectClientNDate.add(JLSelectClient, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
		JCBClient.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
		gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 0);
		selectClientNDate.add(JCBClient, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.gridheight = 5;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
		selectClientNDate.setBackground(Color.WHITE);
		add(selectClientNDate, gridBagConstraints);

		JPanel bodyPanel = new JPanel();

		bodyPanel.setLayout(new java.awt.GridBagLayout());

		int bodyPanelX1C = 0;
		int bodyPanelY1C = 1;

		int bodyPanelX2C = bodyPanelX1C + 1;
		int bodyPanelY2C = 1;

		int bodyPanelX3C = bodyPanelX2C + 1;
		int bodyPanelY3C = 1;

		int bodyPanelX4C = bodyPanelX3C + 1;
		int bodyPanelY4C = 1;

		int bodyPanelX5C = bodyPanelX4C + 1;
		int bodyPanelY5C = 1;

		int bodyPanelX6C = bodyPanelX5C + 1;
		int bodyPanelY6C = 1;

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = bodyPanelX1C;
		gridBagConstraints.gridy = bodyPanelY1C;
		JLSmallWEEE.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
		gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
		gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 0);
		bodyPanel.add(JLSmallWEEE, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = bodyPanelX1C;
		gridBagConstraints.gridy = ++bodyPanelY1C;
		gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 0);
		JLLargeWEE.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
		gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
		bodyPanel.add(JLLargeWEE, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = bodyPanelX1C;
		gridBagConstraints.gridy = ++bodyPanelY1C;
		gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 0);
		JLTxtNFootW.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
		gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
		bodyPanel.add(JLTxtNFootW, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = bodyPanelX1C;
		gridBagConstraints.gridy = ++bodyPanelY1C;
		gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 0);
		JLAluminium.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
		gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
		bodyPanel.add(JLAluminium, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = bodyPanelX1C;
		gridBagConstraints.gridy = ++bodyPanelY1C;
		gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 0);
		JLSteel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
		gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
		bodyPanel.add(JLSteel, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = bodyPanelX1C;
		gridBagConstraints.gridy = ++bodyPanelY1C;
		gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 0);
		JLMixCans.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
		gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
		bodyPanel.add(JLMixCans, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = bodyPanelX1C;
		gridBagConstraints.gridy = ++bodyPanelY1C;
		gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 0);
		JLMetal.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
		gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
		bodyPanel.add(JLMetal, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = bodyPanelX1C;
		gridBagConstraints.gridy = ++bodyPanelY1C;
		gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 0);
		JLPlasticBottles.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
		gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
		bodyPanel.add(JLPlasticBottles, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = bodyPanelX4C;
		gridBagConstraints.gridy = bodyPanelY4C;
		gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 0);
		JLPlasticBags.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
		gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
		bodyPanel.add(JLPlasticBags, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = bodyPanelX4C;
		gridBagConstraints.gridy = ++bodyPanelY4C;
		gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 0);
		JLWood.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
		gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
		bodyPanel.add(JLWood, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = bodyPanelX4C;
		gridBagConstraints.gridy = ++bodyPanelY4C;
		gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 0);
		JLPaper.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
		gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
		bodyPanel.add(JLPaper, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = bodyPanelX4C;
		gridBagConstraints.gridy = ++bodyPanelY4C;
		gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 0);
		JLCard.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
		gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
		bodyPanel.add(JLCard, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = bodyPanelX4C;
		gridBagConstraints.gridy = ++bodyPanelY4C;
		gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 0);
		JLBooks.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
		gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
		bodyPanel.add(JLBooks, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = bodyPanelX4C;
		gridBagConstraints.gridy = ++bodyPanelY4C;
		gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 0);
		JLRubble.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
		gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
		bodyPanel.add(JLRubble, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = bodyPanelX4C;
		gridBagConstraints.gridy = ++bodyPanelY4C;
		gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 0);
		JLGlassColourS.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
		gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
		bodyPanel.add(JLGlassColourS, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = bodyPanelX4C;
		gridBagConstraints.gridy = ++bodyPanelY4C;
		gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 0);
		JLGlassColourM.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
		gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
		bodyPanel.add(JLGlassColourM, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = bodyPanelX2C;
		gridBagConstraints.gridy = bodyPanelY2C;
		gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 0);
		// gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		// gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
		bodyPanel.add(JTFSmallWEEE, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = bodyPanelX2C;
		gridBagConstraints.gridy = ++bodyPanelY2C;
		gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 0);
		bodyPanel.add(JTFLargeWEE, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = bodyPanelX2C;
		gridBagConstraints.gridy = ++bodyPanelY2C;
		gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 0);
		bodyPanel.add(JTFTxtNFootW, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = bodyPanelX2C;
		gridBagConstraints.gridy = ++bodyPanelY2C;
		gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 0);
		bodyPanel.add(JTFAluminium, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = bodyPanelX2C;
		gridBagConstraints.gridy = ++bodyPanelY2C;
		gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 0);
		bodyPanel.add(JTFSteel, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = bodyPanelX2C;
		gridBagConstraints.gridy = ++bodyPanelY2C;
		gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 0);
		bodyPanel.add(JTFMixCans, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = bodyPanelX2C;
		gridBagConstraints.gridy = ++bodyPanelY2C;
		gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 0);
		bodyPanel.add(JTFMetal, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = bodyPanelX2C;
		gridBagConstraints.gridy = ++bodyPanelY2C;
		gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 0);
		bodyPanel.add(JTFPlasticBottles, gridBagConstraints);
		//

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = bodyPanelX5C;
		gridBagConstraints.gridy = bodyPanelY5C;
		gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 0);
		// gridBagConstraints.weightx = 1;
		bodyPanel.add(JTFPlasticBags, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = bodyPanelX5C;
		gridBagConstraints.gridy = ++bodyPanelY5C;
		gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 0);
		bodyPanel.add(JTFWood, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = bodyPanelX5C;
		gridBagConstraints.gridy = ++bodyPanelY5C;
		gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 0);
		bodyPanel.add(JTFPaper, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = bodyPanelX5C;
		gridBagConstraints.gridy = ++bodyPanelY5C;
		gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 0);
		bodyPanel.add(JTFCard, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = bodyPanelX5C;
		gridBagConstraints.gridy = ++bodyPanelY5C;
		gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 0);
		bodyPanel.add(JTFBooks, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = bodyPanelX5C;
		gridBagConstraints.gridy = ++bodyPanelY5C;
		gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 0);
		bodyPanel.add(JTFRubble, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = bodyPanelX5C;
		gridBagConstraints.gridy = ++bodyPanelY5C;
		gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 0);
		bodyPanel.add(JTFGlassColourS, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = bodyPanelX5C;
		gridBagConstraints.gridy = ++bodyPanelY5C;
		gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 0);
		bodyPanel.add(JTFGlassColourM, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = bodyPanelX3C;
		gridBagConstraints.gridy = bodyPanelY3C - 1;
		JLUnit1.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
		bodyPanel.add(JLUnit1, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = bodyPanelX3C;
		gridBagConstraints.gridy = bodyPanelY3C;
		gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 0);
		bodyPanel.add(JCBSmallWEEE, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = bodyPanelX3C;
		gridBagConstraints.gridy = ++bodyPanelY3C;
		gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 0);
		bodyPanel.add(JCBLargeWEE, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = bodyPanelX3C;
		gridBagConstraints.gridy = ++bodyPanelY3C;
		gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 0);
		bodyPanel.add(JCBTxtNFootW, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = bodyPanelX3C;
		gridBagConstraints.gridy = ++bodyPanelY3C;
		gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 0);
		bodyPanel.add(JCBAluminium, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = bodyPanelX3C;
		gridBagConstraints.gridy = ++bodyPanelY3C;
		gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 0);
		bodyPanel.add(JCBSteel, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = bodyPanelX3C;
		gridBagConstraints.gridy = ++bodyPanelY3C;
		gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 0);
		bodyPanel.add(JCBMixCans, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = bodyPanelX3C;
		gridBagConstraints.gridy = ++bodyPanelY3C;
		gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 0);
		bodyPanel.add(JCBMetal, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = bodyPanelX3C;
		gridBagConstraints.gridy = ++bodyPanelY3C;
		gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 0);
		bodyPanel.add(JCBPlasticBottles, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = bodyPanelX6C;
		gridBagConstraints.gridy = bodyPanelY6C - 1;
		JLUnit2.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
		bodyPanel.add(JLUnit2, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = bodyPanelX6C;
		gridBagConstraints.gridy = bodyPanelY6C;
		gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 0);
		bodyPanel.add(JCBPlasticBags, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = bodyPanelX6C;
		gridBagConstraints.gridy = ++bodyPanelY6C;
		gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 0);
		bodyPanel.add(JCBWood, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = bodyPanelX6C;
		gridBagConstraints.gridy = ++bodyPanelY6C;
		gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 0);
		bodyPanel.add(JCBPaper, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = bodyPanelX6C;
		gridBagConstraints.gridy = ++bodyPanelY6C;
		gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 0);
		bodyPanel.add(JCBCard, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = bodyPanelX6C;
		gridBagConstraints.gridy = ++bodyPanelY6C;
		gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 0);
		bodyPanel.add(JCBBooks, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = bodyPanelX6C;
		gridBagConstraints.gridy = ++bodyPanelY6C;
		gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 0);
		bodyPanel.add(JCBRubble, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = bodyPanelX6C;
		gridBagConstraints.gridy = ++bodyPanelY6C;
		gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 0);
		bodyPanel.add(JCBGlassColourS, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = bodyPanelX6C;
		gridBagConstraints.gridy = ++bodyPanelY6C;
		gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 0);
		bodyPanel.add(JCBGlassColourM, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 6;
		gridBagConstraints.gridheight = 5;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
		// gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 0);
		bodyPanel.setBackground(Color.WHITE);
		add(bodyPanel, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 22;
		// gridBagConstraints.gridwidth = 2;
		gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 0);
		gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
		calculateButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
		calculateButton.setBackground(new Color(92, 184, 92));
		calculateButton.setForeground(Color.WHITE);
		calculateButton.addActionListener(actionListener);
		calculateButton.setActionCommand("CALCULATE");
		add(calculateButton, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 24;
		gridBagConstraints.gridwidth = 4;
		// gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
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
		gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 0);
		saveButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
		saveButton.addActionListener(actionListener);
		saveButton.setActionCommand("SAVE");
		jpanel.add(saveButton, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 26;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
		// gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		// gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 0);
		jpanel.setBackground(Color.WHITE);
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
		if ((isAllOk && fromDate != null && toDate != null) && (!JTFSmallWEEE.getText().isEmpty()
				|| !JTFLargeWEE.getText().isEmpty() || !JTFTxtNFootW.getText().isEmpty()
				|| !JTFAluminium.getText().isEmpty() || !JTFSteel.getText().isEmpty() || !JTFMixCans.getText().isEmpty()
				|| !JTFMetal.getText().isEmpty() || !JTFPlasticBottles.getText().isEmpty()
				|| !JTFPlasticBags.getText().isEmpty() || !JTFWood.getText().isEmpty() || !JTFPaper.getText().isEmpty()
				|| !JTFCard.getText().isEmpty() || !JTFBooks.getText().isEmpty() || !JTFRubble.getText().isEmpty()
				|| !JTFGlassColourS.getText().isEmpty() || !JTFGlassColourM.getText().isEmpty())) {
			isAllOk = true;
		} else
			isAllOk = false;

		return isAllOk;
	}

	private Double SmallWEEECO2E = 0.0;
	private Double LargeWEECO2E = 0.0;
	private Double TxtNFootWCO2E = 0.0;
	private Double AluminiumCO2E = 0.0;
	private Double SteelCO2E = 0.0;
	private Double MixCansCO2E = 0.0;
	private Double MetalCO2E = 0.0;
	private Double PlasticBottlesCO2E = 0.0;
	private Double PlasticBagsCO2E = 0.0;
	private Double WoodCO2E = 0.0;
	private Double PaperCO2E = 0.0;
	private Double CardCO2E = 0.0;
	private Double BooksCO2E = 0.0;
	private Double RubbleCO2E = 0.0;
	private Double GlassColourSCO2E = 0.0;
	private Double GlassColourMCO2E = 0.0;
	public static Double totalLandfillingCO2E = 0.0;

	/**
	 * calculate total emission and
	 * 
	 * @return true if calculation goes well
	 */
	private Boolean calculateTotalEmission() {
		try {

			SmallWEEECO2E = Double.valueOf(JTFSmallWEEE.getText().isEmpty() ? "0.0" : JTFSmallWEEE.getText())
					* energySourceDetail.get(SMALLWEE).get(JCBSmallWEEE.getSelectedItem().toString());

			LargeWEECO2E = Double.valueOf(JTFLargeWEE.getText().isEmpty() ? "0.0" : JTFLargeWEE.getText())
					* energySourceDetail.get(LARGEWEE).get(JCBLargeWEE.getSelectedItem().toString());

			TxtNFootWCO2E = Double.valueOf(JTFTxtNFootW.getText().isEmpty() ? "0.0" : JTFTxtNFootW.getText())
					* energySourceDetail.get(TXTNFOOTW).get(JCBTxtNFootW.getSelectedItem().toString());

			AluminiumCO2E = Double.valueOf(JTFAluminium.getText().isEmpty() ? "0.0" : JTFAluminium.getText())
					* energySourceDetail.get(ALUMINIUM).get(JCBAluminium.getSelectedItem().toString());

			SteelCO2E = Double.valueOf(JTFSteel.getText().isEmpty() ? "0.0" : JTFSteel.getText())
					* energySourceDetail.get(STEEL).get(JCBSteel.getSelectedItem().toString());

			MixCansCO2E = Double.valueOf(JTFMixCans.getText().isEmpty() ? "0.0" : JTFMixCans.getText())
					* energySourceDetail.get(MIXCANS).get(JCBMixCans.getSelectedItem().toString());

			MetalCO2E = Double.valueOf(JTFMetal.getText().isEmpty() ? "0.0" : JTFMetal.getText())
					* energySourceDetail.get(METAL).get(JCBMetal.getSelectedItem().toString());

			PlasticBottlesCO2E = Double
					.valueOf(JTFPlasticBottles.getText().isEmpty() ? "0.0" : JTFPlasticBottles.getText())
					* energySourceDetail.get(PLASTICBOTTLES).get(JCBPlasticBottles.getSelectedItem().toString());

			PlasticBagsCO2E = Double.valueOf(JTFPlasticBags.getText().isEmpty() ? "0.0" : JTFPlasticBags.getText())
					* energySourceDetail.get(PLASTICBAGS).get(JCBPlasticBags.getSelectedItem().toString());

			WoodCO2E = Double.valueOf(JTFWood.getText().isEmpty() ? "0.0" : JTFWood.getText())
					* energySourceDetail.get(WOOD).get(JCBWood.getSelectedItem().toString());

			PaperCO2E = Double.valueOf(JTFPaper.getText().isEmpty() ? "0.0" : JTFPaper.getText())
					* energySourceDetail.get(PAPER).get(JCBPaper.getSelectedItem().toString());

			CardCO2E = Double.valueOf(JTFCard.getText().isEmpty() ? "0.0" : JTFCard.getText())
					* energySourceDetail.get(CARD).get(JCBCard.getSelectedItem().toString());

			BooksCO2E = Double.valueOf(JTFBooks.getText().isEmpty() ? "0.0" : JTFBooks.getText())
					* energySourceDetail.get(BOOKS).get(JCBBooks.getSelectedItem().toString());

			RubbleCO2E = Double.valueOf(JTFRubble.getText().isEmpty() ? "0.0" : JTFRubble.getText())
					* energySourceDetail.get(RUBBLE).get(JCBRubble.getSelectedItem().toString());

			GlassColourSCO2E = Double.valueOf(JTFGlassColourS.getText().isEmpty() ? "0.0" : JTFGlassColourS.getText())
					* energySourceDetail.get(GLASSCOLORS).get(JCBGlassColourS.getSelectedItem().toString());

			GlassColourMCO2E = Double.valueOf(JTFGlassColourM.getText().isEmpty() ? "0.0" : JTFGlassColourM.getText())
					* energySourceDetail.get(GLASSCOLORM).get(JCBGlassColourM.getSelectedItem().toString());

		} catch (NumberFormatException ne) {
			System.out.println("Number Format Exception while calculating	 OtherMaterials Hold Emission" + ne);
			return false;
		}
		//
		totalLandfillingCO2E = SmallWEEECO2E + LargeWEECO2E + TxtNFootWCO2E + AluminiumCO2E + SteelCO2E + MixCansCO2E
				+ MetalCO2E + PlasticBottlesCO2E + PlasticBagsCO2E + WoodCO2E + PaperCO2E + CardCO2E + BooksCO2E
				+ RubbleCO2E + GlassColourSCO2E + GlassColourMCO2E;
		totalLandfillingCO2E = Math.round(totalLandfillingCO2E * 100D) / 100D;

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
							"Total GHG emissions from Land Filling = " + totalLandfillingCO2E + " kgs of CO2e");
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

						String sql = "INSERT INTO energyreadingdetail (clientid,energysourceid,unit,from_date,to_date,totalconsumption,conversionfactor,CO2eproduce,landfill) VALUES";
						String values = "";
						int cliendId = GlobalValues.clientIDMap.get(JCBClient.getSelectedIndex());
						if (!JTFSmallWEEE.getText().isEmpty()) {
							values += "(" + cliendId + "," + SMALLWEE + ",'" + JCBSmallWEEE.getSelectedItem().toString()
									+ "',date('" + fromDate + "'),date('" + toDate + "'),"
									+ Double.parseDouble(JTFSmallWEEE.getText().toString()) + ","
									+ energySourceDetail.get(SMALLWEE).get(JCBSmallWEEE.getSelectedItem().toString())
									+ "," + SmallWEEECO2E + ",'true'),";

						}
						if (!JTFLargeWEE.getText().isEmpty()) {
							values += "(" + cliendId + "," + LARGEWEE + ",'" + JCBLargeWEE.getSelectedItem().toString()
									+ "',date('" + fromDate + "'),date('" + toDate + "'),"
									+ Double.parseDouble(JTFLargeWEE.getText().toString()) + ","
									+ energySourceDetail.get(LARGEWEE).get(JCBLargeWEE.getSelectedItem().toString())
									+ "," + LargeWEECO2E + ",'true'),";
						}
						if (!JTFTxtNFootW.getText().isEmpty()) {
							values += "(" + cliendId + "," + TXTNFOOTW + ",'"
									+ JCBTxtNFootW.getSelectedItem().toString() + "',date('" + fromDate + "'),date('"
									+ toDate + "')," + Double.parseDouble(JTFTxtNFootW.getText().toString()) + ","
									+ energySourceDetail.get(TXTNFOOTW).get(JCBTxtNFootW.getSelectedItem().toString())
									+ "," + TxtNFootWCO2E + ",'true'),";
						}
						if (!JTFAluminium.getText().isEmpty()) {
							values += "(" + cliendId + "," + ALUMINIUM + ",'"
									+ JCBAluminium.getSelectedItem().toString() + "',date('" + fromDate + "'),date('"
									+ toDate + "')," + Double.parseDouble(JTFAluminium.getText().toString()) + ","
									+ energySourceDetail.get(ALUMINIUM).get(JCBAluminium.getSelectedItem().toString())
									+ "," + AluminiumCO2E + ",'true'),";
						}
						if (!JTFSteel.getText().isEmpty()) {
							values += "(" + cliendId + "," + STEEL + ",'" + JCBSteel.getSelectedItem().toString()
									+ "',date('" + fromDate + "'),date('" + toDate + "'),"
									+ Double.parseDouble(JTFSteel.getText().toString()) + ","
									+ energySourceDetail.get(STEEL).get(JCBSteel.getSelectedItem().toString()) + ","
									+ SteelCO2E + ",'true'),";
						}
						if (!JTFMixCans.getText().isEmpty()) {
							values += "(" + cliendId + "," + MIXCANS + ",'" + JCBMixCans.getSelectedItem().toString()
									+ "',date('" + fromDate + "'),date('" + toDate + "'),"
									+ Double.parseDouble(JTFMixCans.getText().toString()) + ","
									+ energySourceDetail.get(MIXCANS).get(JCBMixCans.getSelectedItem().toString()) + ","
									+ MixCansCO2E + ",'true'),";
						}
						if (!JTFMetal.getText().isEmpty()) {
							values += "(" + cliendId + "," + METAL + ",'" + JCBMetal.getSelectedItem().toString()
									+ "',date('" + fromDate + "'),date('" + toDate + "'),"
									+ Double.parseDouble(JTFMetal.getText().toString()) + ","
									+ energySourceDetail.get(METAL).get(JCBMetal.getSelectedItem().toString()) + ","
									+ MetalCO2E + ",'true'),";
						}
						if (!JTFPlasticBottles.getText().isEmpty()) {
							values += "(" + cliendId + "," + PLASTICBOTTLES + ",'"
									+ JCBPlasticBottles.getSelectedItem().toString() + "',date('" + fromDate
									+ "'),date('" + toDate + "'),"
									+ Double.parseDouble(JTFPlasticBottles.getText().toString()) + ","
									+ energySourceDetail.get(PLASTICBOTTLES)
											.get(JCBPlasticBottles.getSelectedItem().toString())
									+ "," + PlasticBottlesCO2E + ",'true'),";

						}
						if (!JTFPlasticBags.getText().isEmpty()) {
							values += "(" + cliendId + "," + PLASTICBAGS + ",'"
									+ JCBPlasticBags.getSelectedItem().toString() + "',date('" + fromDate + "'),date('"
									+ toDate + "')," + Double.parseDouble(JTFPlasticBags.getText().toString()) + ","
									+ energySourceDetail.get(PLASTICBAGS)
											.get(JCBPlasticBags.getSelectedItem().toString())
									+ "," + PlasticBagsCO2E + ",'true'),";
						}
						if (!JTFWood.getText().isEmpty()) {
							values += "(" + cliendId + "," + WOOD + ",'" + JCBWood.getSelectedItem().toString()
									+ "',date('" + fromDate + "'),date('" + toDate + "'),"
									+ Double.parseDouble(JTFWood.getText().toString()) + ","
									+ energySourceDetail.get(WOOD).get(JCBWood.getSelectedItem().toString()) + ","
									+ WoodCO2E + ",'true'),";
						}
						if (!JTFPaper.getText().isEmpty()) {
							values += "(" + cliendId + "," + PAPER + ",'" + JCBPaper.getSelectedItem().toString()
									+ "',date('" + fromDate + "'),date('" + toDate + "'),"
									+ Double.parseDouble(JTFPaper.getText().toString()) + ","
									+ energySourceDetail.get(PAPER).get(JCBPaper.getSelectedItem().toString()) + ","
									+ PaperCO2E + ",'true'),";
						}
						if (!JTFCard.getText().isEmpty()) {
							values += "(" + cliendId + "," + CARD + ",'" + JCBCard.getSelectedItem().toString()
									+ "',date('" + fromDate + "'),date('" + toDate + "'),"
									+ Double.parseDouble(JTFCard.getText().toString()) + ","
									+ energySourceDetail.get(CARD).get(JCBCard.getSelectedItem().toString()) + ","
									+ CardCO2E + ",'true'),";
						}
						if (!JTFBooks.getText().isEmpty()) {
							values += "(" + cliendId + "," + BOOKS + ",'" + JCBBooks.getSelectedItem().toString()
									+ "',date('" + fromDate + "'),date('" + toDate + "'),"
									+ Double.parseDouble(JTFBooks.getText().toString()) + ","
									+ energySourceDetail.get(BOOKS).get(JCBBooks.getSelectedItem().toString()) + ","
									+ BooksCO2E + ",'true'),";
						}
						if (!JTFRubble.getText().isEmpty()) {
							values += "(" + cliendId + "," + RUBBLE + ",'" + JCBRubble.getSelectedItem().toString()
									+ "',date('" + fromDate + "'),date('" + toDate + "'),"
									+ Double.parseDouble(JTFRubble.getText().toString()) + ","
									+ energySourceDetail.get(RUBBLE).get(JCBRubble.getSelectedItem().toString()) + ","
									+ RubbleCO2E + ",'true'),";
						}
						if (!JTFGlassColourS.getText().isEmpty()) {
							values += "(" + cliendId + "," + GLASSCOLORS + ",'"
									+ JCBGlassColourS.getSelectedItem().toString() + "',date('" + fromDate + "'),date('"
									+ toDate + "')," + Double.parseDouble(JTFGlassColourS.getText().toString()) + ","
									+ energySourceDetail.get(GLASSCOLORS)
											.get(JCBGlassColourS.getSelectedItem().toString())
									+ "," + GlassColourSCO2E + ",'true'),";
						}
						if (!JTFGlassColourM.getText().isEmpty()) {
							values += "(" + cliendId + "," + GLASSCOLORM + ",'"
									+ JCBGlassColourM.getSelectedItem().toString() + "',date('" + fromDate + "'),date('"
									+ toDate + "')," + Double.parseDouble(JTFGlassColourM.getText().toString()) + ","
									+ energySourceDetail.get(GLASSCOLORM)
											.get(JCBGlassColourM.getSelectedItem().toString())
									+ "," + GlassColourMCO2E + ",'true'),";
						}

						if (values != "") {
							try {
								values = values.substring(0, values.lastIndexOf(","));
								sql += values;

								stOtherMaterialsDataEntry = cnOtherMaterialsDataEntry.createStatement();
								System.out.println(sql);
								stOtherMaterialsDataEntry.execute(sql);

								String energyreadingmeterSQL = "Insert into energyreadingmeter(clientid,entry_date,totalCO2E) VALUES(";
								energyreadingmeterSQL += cliendId + "," + "date('now')," + totalLandfillingCO2E + ")";
								System.out.println(energyreadingmeterSQL);
								stOtherMaterialsDataEntry.execute(energyreadingmeterSQL);

								JTFResult.setText("Total GHG emissions from Land Filling = " + totalLandfillingCO2E
										+ " kgs of CO2e");
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
