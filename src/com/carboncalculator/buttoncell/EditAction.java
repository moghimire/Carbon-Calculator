package com.carboncalculator.buttoncell;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTable;

import com.carboncalculator.forms.CityViewPage;
import com.carboncalculator.forms.ClientViewPage;
import com.carboncalculator.forms.FuelTypeViewPage;
import com.carboncalculator.forms.HouseTypeViewPage;
import com.carboncalculator.forms.VehicleViewPage;
import com.carboncalculator.utility.AddEditCity;
import com.carboncalculator.utility.AddEditClient;
import com.carboncalculator.utility.AddEditFuelType;
import com.carboncalculator.utility.AddEditHouseType;
import com.carboncalculator.utility.AddEditVehicle;

class EditAction extends AbstractAction {
	private final JTable table;

	protected EditAction(JTable table) {
		super("edit");
		this.table = table;
	}

	/** Action command will distinguish from which table the action is called */
	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getActionCommand() == "ClientViewPage") {
			if (ClientViewPage.total != 0) {
				try {
					if (table.getValueAt(table.getSelectedRow(), table.getSelectedColumn()) != null) {
						String sql = "SELECT * FROM clients WHERE ClientID = "
								+ table.getValueAt(table.getSelectedRow(), 0);
						System.out.println(sql);
						JDialog JDEdit = new AddEditClient(false, ClientViewPage.JFParentFrame, ClientViewPage.cnClient,
								sql);
						JDEdit.show();

					}
				} catch (Exception sqlE) {
					if (sqlE.getMessage() != null) {
						System.out.println(sqlE.getMessage());
					} else {
						JOptionPane.showMessageDialog(null, "Please select a record in the list to modify.",
								"No Record Selected", JOptionPane.INFORMATION_MESSAGE);
					}

				}
			}
		} else if (e.getActionCommand() == "CityViewPage") {
			if (CityViewPage.total != 0) {
				try {
					if (table.getValueAt(table.getSelectedRow(), table.getSelectedColumn()) != null) {
						String sql = "SELECT * FROM city WHERE cityID = " + table.getValueAt(table.getSelectedRow(), 0);
						System.out.println(sql);
						JDialog JDEdit = new AddEditCity(false, CityViewPage.JFParentFrame, CityViewPage.cnCity, sql);
						JDEdit.show();

					}
				} catch (Exception sqlE) {
					if (sqlE.getMessage() != null) {
						System.out.println(sqlE.getMessage());
					} else {
						JOptionPane.showMessageDialog(null, "Please select a record in the list to modify.",
								"No Record Selected", JOptionPane.INFORMATION_MESSAGE);
					}

				}
			}
		} else if (e.getActionCommand() == "VehicleViewPage") {
			if (VehicleViewPage.total != 0) {
				try {
					if (table.getValueAt(table.getSelectedRow(), table.getSelectedColumn()) != null) {
						String sql = "SELECT * FROM vehicles WHERE vehicleID = "
								+ table.getValueAt(table.getSelectedRow(), 0);
						System.out.println(sql);
						JDialog JDEdit = new AddEditVehicle(false, VehicleViewPage.JFParentFrame,
								VehicleViewPage.cnVehicle, sql);
						JDEdit.show();

					}
				} catch (Exception sqlE) {
					if (sqlE.getMessage() != null) {
						System.out.println(sqlE.getMessage());
					} else {
						JOptionPane.showMessageDialog(null, "Please select a record in the list to modify.",
								"No Record Selected", JOptionPane.INFORMATION_MESSAGE);
					}

				}
			}
		} else if (e.getActionCommand() == "FuelTypeViewPage") {
			if (FuelTypeViewPage.total != 0) {
				try {
					if (table.getValueAt(table.getSelectedRow(), table.getSelectedColumn()) != null) {
						String sql = "SELECT * FROM fueltype WHERE fueltype_ID = "
								+ table.getValueAt(table.getSelectedRow(), 0);
						System.out.println(sql);
						JDialog JDEdit = new AddEditFuelType(false, FuelTypeViewPage.JFParentFrame,
								FuelTypeViewPage.cnFuelType, sql);
						JDEdit.show();

					}
				} catch (Exception sqlE) {
					if (sqlE.getMessage() != null) {
						System.out.println(sqlE.getMessage());
					} else {
						JOptionPane.showMessageDialog(null, "Please select a record in the list to modify.",
								"No Record Selected", JOptionPane.INFORMATION_MESSAGE);
					}

				}
			}
		} else if (e.getActionCommand() == "HouseTypeViewPage") {
			if (HouseTypeViewPage.total != 0) {
				try {
					if (table.getValueAt(table.getSelectedRow(), table.getSelectedColumn()) != null) {
						String sql = "SELECT * FROM Housetype WHERE House_ID = "
								+ table.getValueAt(table.getSelectedRow(), 0);
						System.out.println(sql);
						JDialog JDEdit = new AddEditHouseType(false, HouseTypeViewPage.JFParentFrame,
								HouseTypeViewPage.cnHouseType, sql);
						JDEdit.show();

					}
				} catch (Exception sqlE) {
					if (sqlE.getMessage() != null) {
						System.out.println(sqlE.getMessage());
					} else {
						JOptionPane.showMessageDialog(null, "Please select a record in the list to modify.",
								"No Record Selected", JOptionPane.INFORMATION_MESSAGE);
					}

				}
			}
		}
	}
}