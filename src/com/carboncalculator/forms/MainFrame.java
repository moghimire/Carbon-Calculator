package com.carboncalculator.forms;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.MetalTheme;

import com.carboncalculator.forms.dataentry.AirPlaneDataEntry;
import com.carboncalculator.forms.dataentry.CO2EmissionDetailsForm;
import com.carboncalculator.forms.dataentry.CarDataEntry;
import com.carboncalculator.forms.dataentry.FoodDataEntry;
import com.carboncalculator.forms.dataentry.HouseDataEntry;
import com.carboncalculator.forms.dataentry.LandFillingDataEntry;
import com.carboncalculator.forms.dataentry.LocalTransportDataEntry;
import com.carboncalculator.forms.dataentry.OtherMaterialsDataEntry;
import com.carboncalculator.forms.dataentry.RecyclingDataEntry;
import com.carboncalculator.theme.StyleTheme;

/*
 * This is the Main Frame which contains mainPanel
 * All the other pages/forms are loaded/replaced in mainPanel
 * **/

public class MainFrame extends JFrame {

	/*
	 * Database Connections
	 **/
	String DBDriver = "org.sqlite.JDBC";
	String URL = "jdbc:sqlite:/Users/ALI/Documents/Onyx Tech/Tasneem/Calculator/latest-new developer/08082017/Carbon Calculator/sqlite_file/CarbonCalculator";

	Connection CN;
	public static Boolean isLogin = false;
	public static String userName;

	/*
	 * name for pages
	 **/
	String WCPAGENAME = "WELCOME_PAGE";
	String SIGNUPPAGENAME = "SIGNUP_PAGE";
	String CLIENTVIEWPAGENAME = "CLIENTVIEW_PAGE";
	String CITYVIEWPAGENAME = "CITYVIEW_PAGE";

	/*
	 * Declare objects for different pages
	 **/
	WelcomePage wcPage;
	SignUpPage signUpPage;
	ClientViewPage clientVPage = null;
	CityViewPage cityVPage = null;
	VehicleViewPage vehicleVPage = null;
	FuelTypeViewPage fuelTypeVPage = null;
	HouseTypeViewPage houseTypeVPage = null;
	HouseDataEntry houseDataEntryVPage = null;
	FoodDataEntry foodDataEntryVPage = null;
	CarDataEntry carDataEntryVPage = null;
	LocalTransportDataEntry localDataEntryVPage = null;
	AirPlaneDataEntry airplaneDataEntryVPage = null;
	OtherMaterialsDataEntry otherMaterialsDataEntryVPage = null;
	LandFillingDataEntry landFillingDataEntryVPage = null;
	RecyclingDataEntry recyclingDataEntryVPage = null;
	CO2EmissionDetailsForm co2eDetailsVPage = null;

	/**
	 * Main Panel
	 **/
	JPanel mainPanel;

	public static Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();

	/*
	 * Main Constructor
	 **/

	public MainFrame() {
		this.setTitle("Carbon Calulator");

		/*
		 * Added a window listener
		 **/

		this.addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				/**
				 * user active status is updated to false before exiting the
				 * program
				 **/

				if (isLogin) {
					String sql = "UPDATE user set active = ? where name = ?";
					System.out.println(sql);
					PreparedStatement pstmt;
					try {
						pstmt = CN.prepareStatement(sql);
						pstmt.setBoolean(1, false);
						pstmt.setString(2, userName);
						pstmt.executeUpdate();
						pstmt.close();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}

				}
				System.exit(0);
			}
		});

		try {

			/*
			 * Database Driver is loaded
			 */
			Class.forName(DBDriver);
			CN = DriverManager.getConnection(URL);
			if (CN == null) {
				JOptionPane.showMessageDialog(null,
						"Sorry, there is some issue with our program. Please contact the support team.",
						"Carbon Calculator", JOptionPane.WARNING_MESSAGE);
			} else {
				wcPage = new WelcomePage(this, CN);
				this.getContentPane().add(wcPage);
			}
		} catch (ClassNotFoundException e) {
			JOptionPane.showMessageDialog(null,
					"Sorry, there is some issue with our program. Please contact the support team.",
					"Carbon Calculator", JOptionPane.WARNING_MESSAGE);
			System.err.println("Failed to load driver");
			e.printStackTrace();
			System.exit(1);
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null,
					"Sorry, there is some issue with our program. Please contact the support team.",
					"Carbon Calculator", JOptionPane.WARNING_MESSAGE);
			System.err.println("Unable to connect");
			e.printStackTrace();
			System.exit(1);
		} finally {
			this.setLocation(screen.width / 4, screen.height / 4);
			this.setSize(screen.width / 2, screen.height / 2);
			setResizable(true);
			this.setVisible(true);
		}
	}

	/**
	 * replace the old page/form from the mainPanel and add new page/form
	 * 
	 * @param oldPageName
	 *            previous page name
	 * @param newPageName
	 *            new page name which should be loaded
	 */

	public void updatePanel(String oldPageName, String newPageName) throws SQLException {
		if (oldPageName == WCPAGENAME && newPageName == SIGNUPPAGENAME) {
			signUpPage = new SignUpPage(this, CN);
			this.getContentPane().remove(wcPage);
			this.getContentPane().add(signUpPage);
			this.getContentPane().invalidate();
			this.getContentPane().validate();
			this.getContentPane().repaint();
		}

		if (oldPageName == SIGNUPPAGENAME && newPageName == WCPAGENAME) {
			wcPage = new WelcomePage(this, CN);
			this.getContentPane().remove(signUpPage);
			this.getContentPane().add(wcPage);
			this.getContentPane().invalidate();
			this.getContentPane().validate();
			this.getContentPane().repaint();
		}

		if (oldPageName == WCPAGENAME && newPageName == CLIENTVIEWPAGENAME) {
			clientVPage = new ClientViewPage(this, CN);
			this.getContentPane().remove(wcPage);
			mainPanel = new JPanel(new BorderLayout());
			mainPanel.setBackground(Color.gray);
			mainPanel.setBorder(BorderFactory.createLoweredBevelBorder());
			mainPanel.add(clientVPage, BorderLayout.CENTER);

			getContentPane().add(CreateJToolBar(), BorderLayout.PAGE_START);
			getContentPane().add(mainPanel, BorderLayout.CENTER);

			this.getContentPane().setBackground(Color.WHITE);

			this.addMenuBar();
			this.getContentPane().invalidate();
			this.getContentPane().validate();
			this.getContentPane().repaint();
			this.setLocation(0, 0);
			this.setExtendedState(JFrame.MAXIMIZED_BOTH);
			this.setResizable(false);

		}
	}

	/*
	 * Create a tool bar for House, Food, Car, Local transport, Airplane, Other
	 * Materials, Landfilling, Recycling Data Entries when user gets
	 * successfully login
	 **/
	private JToolBar CreateJToolBar() {
		JToolBar NewJToolBar = new JToolBar("Toolbar");

		NewJToolBar.setMargin(new Insets(0, 0, 0, 0));

		// Create a toolbar button
		NewJToolBar.add(CreateJToolbarButton("House", "images/house-menu.jpg", "toolHouse"));
		NewJToolBar.addSeparator();
		NewJToolBar.add(CreateJToolbarButton("Food", "images/food-menu.jpg", "toolFood"));
		NewJToolBar.addSeparator();
		NewJToolBar.add(CreateJToolbarButton("Car", "images/car-menu.jpg", "toolCar"));
		NewJToolBar.addSeparator();
		NewJToolBar
				.add(CreateJToolbarButton("Local Transport", "images/localtransport-menu.jpg", "toolLocalTransport"));
		NewJToolBar.addSeparator();
		NewJToolBar.add(CreateJToolbarButton("Airplane", "images/airplane-menu.jpg", "toolAirplane"));
		NewJToolBar.addSeparator();
		NewJToolBar
				.add(CreateJToolbarButton("Other Materials", "images/othermaterials-menu.jpg", "toolOtherMaterials"));
		NewJToolBar.addSeparator();
		NewJToolBar.add(CreateJToolbarButton("LandFilling", "images/landfilling-menu.jpg", "toolLandFilling"));
		NewJToolBar.addSeparator();
		NewJToolBar.add(CreateJToolbarButton("Recycling", "images/recycling-menu.jpg", "toolRecycling"));
		NewJToolBar.addSeparator();
		NewJToolBar.add(CreateJToolbarButton("CO2EMISSION", "images/co2emission-details-menu.jpg", "toolCO2E"));
		return NewJToolBar;
	}

	/** End create a tool bar **/

	/*
	 * Create a tool bar Button
	 **/

	private JButton CreateJToolbarButton(String srcToolTipText, String srcImageLocation, String srcActionCommand) {
		JButton NewJButton = new JButton(new ImageIcon(srcImageLocation));

		NewJButton.setActionCommand(srcActionCommand);
		NewJButton.setToolTipText(srcToolTipText);
		NewJButton.addActionListener(JToolBarActionListener);

		return NewJButton;
	}

	/** Create action Listener for JToolBar Button **/
	ActionListener JToolBarActionListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			String srcObject = e.getActionCommand();
			if (srcObject == "toolClient") {
				try {
					loadClientForm();
				} catch (SQLException sqle) {
				}
			} else if (srcObject == "toolCity") {
				try {
					loadCityForm();
				} catch (SQLException sqle) {
				}
			} else if (srcObject == "toolHouseType") {
				try {
					loadHouseTypeForm();
				} catch (SQLException sqle) {
				}
			} else if (srcObject == "toolVehicle") {
				try {
					loadVehicleForm();
				} catch (SQLException sqle) {
				}
			} else if (srcObject == "toolFuelType") {
				try {
					loadFuelTypeForm();
				} catch (SQLException sqle) {
				}
			} else if (srcObject == "toolHouse") {
				try {
					loadHouseForm();
				} catch (SQLException sqle) {
				}
			} else if (srcObject == "toolFood") {
				try {
					loadFoodForm();
				} catch (SQLException sqle) {
				}
			} else if (srcObject == "toolCar") {
				try {
					loadCarForm();
				} catch (SQLException sqle) {
				}
			} else if (srcObject == "toolLocalTransport") {
				try {
					loadLocalTransportForm();
				} catch (SQLException sqle) {
				}
			} else if (srcObject == "toolAirplane") {
				try {
					loadAirplaneForm();
				} catch (SQLException sqle) {
				}
			} else if (srcObject == "toolOtherMaterials") {
				try {
					loadOtherMaterialsForm();
				} catch (SQLException sqle) {
				}
			} else if (srcObject == "toolLandFilling") {
				try {
					loadLandFillingForm();
				} catch (SQLException sqle) {
				}
			} else if (srcObject == "toolRecycling") {
				try {
					loadRecyclingForm();
				} catch (SQLException sqle) {
				}
			} else if (srcObject == "toolCO2E") {
				try {
					loadCO2EDetailsForm();
				} catch (SQLException sqle) {
				}
			}
		}
	};

	/** End create action Listener for JToolBar Button **/

	/** Added a Menu for Client, City, House, Vehicle, FuelType page/form **/

	public void addMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		JMenu client = new JMenu("Client");
		menuBar.add(client);
		JMenu city = new JMenu("City");
		menuBar.add(city);
		JMenu houseType = new JMenu("House");
		menuBar.add(houseType);
		JMenu vehicle = new JMenu("Vehicle");
		menuBar.add(vehicle);
		JMenu fuelType = new JMenu("FuelType");
		menuBar.add(fuelType);

		this.setJMenuBar(menuBar);

		client.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				try {
					loadClientForm();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});

		city.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				try {
					loadCityForm();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}

			}
		});

		houseType.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				try {
					loadHouseTypeForm();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}

			}
		});

		vehicle.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				try {
					loadVehicleForm();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}

			}
		});

		fuelType.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				try {
					loadFuelTypeForm();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}

			}
		});

	}

	/** refreshes contain of the mainPanel */
	public void refreshPanelView(JPanel newPanel) {
		this.mainPanel.removeAll();
		this.mainPanel.add(newPanel);
		this.mainPanel.invalidate();
		this.mainPanel.validate();
		this.mainPanel.repaint();
	}

	/**
	 * Initializes object of client form if its not already initialized Also
	 * refreshes the mainPanel by removing the previous form from mainPanel and
	 * add client form on mainPanel
	 **/
	private void loadClientForm() throws SQLException {
		if (clientVPage == null) {
			clientVPage = new ClientViewPage(this, CN);
		}
		refreshPanelView(clientVPage);

	}

	/**
	 * Initializes object of city form if its not already initialized Also
	 * refreshes the mainPanel by removing the previous form from mainPanel and
	 * add city form on mainPanel
	 **/
	private void loadCityForm() throws SQLException {
		if (cityVPage == null) {
			cityVPage = new CityViewPage(this, CN);
		}
		refreshPanelView(cityVPage);
	}

	/**
	 * Initializes object of house form if its not already initialized Also
	 * refreshes the mainPanel by removing the previous form from mainPanel and
	 * add house form on mainPanel
	 **/
	private void loadHouseTypeForm() throws SQLException {
		if (houseTypeVPage == null) {
			houseTypeVPage = new HouseTypeViewPage(this, CN);
		}
		refreshPanelView(houseTypeVPage);
	}

	/**
	 * Initializes object of vehicle form if its not already initialized Also
	 * refreshes the mainPanel by removing the previous form from mainPanel and
	 * add vehicle form on mainPanel
	 **/
	private void loadVehicleForm() throws SQLException {
		if (vehicleVPage == null) {
			vehicleVPage = new VehicleViewPage(this, CN);
		}
		refreshPanelView(vehicleVPage);
	}

	/**
	 * Initializes object of fueltype form if its not already initialized Also
	 * refreshes the mainPanel by removing the previous form from mainPanel and
	 * add fuel type form on mainPanel
	 **/
	private void loadFuelTypeForm() throws SQLException {
		if (fuelTypeVPage == null) {
			fuelTypeVPage = new FuelTypeViewPage(this, CN);
		}
		refreshPanelView(fuelTypeVPage);
	}

	/**
	 * Initializes object of house data entry form if its not already
	 * initialized Also refreshes the mainPanel by removing the previous form
	 * from mainPanel and add house data entry form on mainPanel
	 **/
	private void loadHouseForm() throws SQLException {
		if (houseDataEntryVPage == null) {
			houseDataEntryVPage = new HouseDataEntry(this, CN);
		}
		refreshPanelView(houseDataEntryVPage);
	}

	/**
	 * Initializes object of food data entry form if its not already initialized
	 * Also refreshes the mainPanel by removing the previous form from mainPanel
	 * and add food data entry form on mainPanel
	 **/
	private void loadFoodForm() throws SQLException {
		if (foodDataEntryVPage == null) {
			foodDataEntryVPage = new FoodDataEntry(this, CN);
		}
		refreshPanelView(foodDataEntryVPage);
	}

	/**
	 * Initializes object of car data entry form if its not already initialized
	 * Also refreshes the mainPanel by removing the previous form from mainPanel
	 * and add car data entry form on mainPanel
	 **/
	private void loadCarForm() throws SQLException {
		if (carDataEntryVPage == null) {
			carDataEntryVPage = new CarDataEntry(this, CN);
		}
		refreshPanelView(carDataEntryVPage);
	}

	/**
	 * Initializes object of local transport data entry form if its not already
	 * initialized Also refreshes the mainPanel by removing the previous form
	 * from mainPanel and add local transport data entry form on mainPanel
	 **/
	private void loadLocalTransportForm() throws SQLException {
		if (localDataEntryVPage == null) {
			localDataEntryVPage = new LocalTransportDataEntry(this, CN);
		}
		refreshPanelView(localDataEntryVPage);
	}

	/**
	 * Initializes object of airplane data entry form if its not already
	 * initialized Also refreshes the mainPanel by removing the previous form
	 * from mainPanel and add airplane data entry form on mainPanel
	 **/
	private void loadAirplaneForm() throws SQLException {
		if (airplaneDataEntryVPage == null) {
			airplaneDataEntryVPage = new AirPlaneDataEntry(this, CN);
		}
		refreshPanelView(airplaneDataEntryVPage);
	}

	/**
	 * Initializes object of other materials data entry form if its not already
	 * initialized Also refreshes the mainPanel by removing the previous form
	 * from mainPanel and add other materials data entry form on mainPanel
	 **/
	private void loadOtherMaterialsForm() throws SQLException {
		if (otherMaterialsDataEntryVPage == null) {
			otherMaterialsDataEntryVPage = new OtherMaterialsDataEntry(this, CN);
		}
		refreshPanelView(otherMaterialsDataEntryVPage);
	}

	/**
	 * Initializes object of landfilling data entry form if its not already
	 * initialized Also refreshes the mainPanel by removing the previous form
	 * from mainPanel and add landfilling data entry form on mainPanel
	 **/
	private void loadLandFillingForm() throws SQLException {
		if (landFillingDataEntryVPage == null) {
			landFillingDataEntryVPage = new LandFillingDataEntry(this, CN);
		}
		refreshPanelView(landFillingDataEntryVPage);
	}

	/**
	 * Initializes object of recycling data entry form if its not already
	 * initialized Also refreshes the mainPanel by removing the previous form
	 * from mainPanel and add recycling data entry form on mainPanel
	 **/
	private void loadRecyclingForm() throws SQLException {
		if (recyclingDataEntryVPage == null) {
			recyclingDataEntryVPage = new RecyclingDataEntry(this, CN);
		}
		refreshPanelView(recyclingDataEntryVPage);
	}

	/**
	 * Initializes object of Total CO2 Emission Details form if its not already
	 * initialized Also refreshes the mainPanel by removing the previous form
	 * from mainPanel and add Total CO2 Emission Details form on mainPanel
	 **/
	private void loadCO2EDetailsForm() throws SQLException {
		if (co2eDetailsVPage == null) {
			co2eDetailsVPage = new CO2EmissionDetailsForm(this, CN);
		}
		co2eDetailsVPage.reloadRecord();
		co2eDetailsVPage.reloadRecordDateTable();
		refreshPanelView(co2eDetailsVPage);
	}

	/**************************** Main method start ***************************/

	public static void main(String[] args) {
		/**
		 * Load Nimbus Theme
		 */
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {
			System.out.println("Error Loading Theme:" + e.toString());
			// If Failed to load the liquid them then load my own XPStyleTheme
			MetalTheme myXPStyleTheme = new StyleTheme();
			MetalLookAndFeel.setCurrentTheme(myXPStyleTheme);
			try {
				UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
			} catch (Exception err) {
				System.out.println("Error loading myStyleTheme:" + err.toString());
			}
		}
		new MainFrame();
	}

	/************************** End main method start *************************/

}
