package com.carboncalculator.buttoncell;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.JTable;

import com.carboncalculator.forms.CityViewPage;
import com.carboncalculator.forms.ClientViewPage;
import com.carboncalculator.forms.FuelTypeViewPage;
import com.carboncalculator.forms.HouseTypeViewPage;
import com.carboncalculator.forms.VehicleViewPage;
import com.carboncalculator.utility.PublicMethods;

class DeleteAction extends AbstractAction {
	private final JTable table;

	protected DeleteAction(JTable table) {
		super("delete");
		this.table = table;
	}

	/** Action command will distinguish from which table the action is called */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == "ClientViewPage") {
			try {
				if (table.getValueAt(table.getSelectedRow(), table.getSelectedColumn()) != null) {
					String ObjButtons[] = { "Yes", "No" };
					int PromptResult = JOptionPane.showOptionDialog(null,
							"Are you sure you want to delete the selected record?", "Delete Record",
							JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, ObjButtons, ObjButtons[1]);
					if (PromptResult == 0) {
						String sql = "DELETE FROM clients WHERE clientID = "
								+ table.getValueAt(table.getSelectedRow(), 0);
						System.out.println(sql);
						ClientViewPage.stClient.execute(sql);
						ClientViewPage.reloadRecord();

						// Update Global Client Id Map
						PublicMethods.fillClientIDMap(
								"select * from clients order by firstName asc, middlename asc, lastname asc",
								ClientViewPage.cnClient);

						JOptionPane.showMessageDialog(null, "Record has been successfully deleted.", "Comfirm Delete",
								JOptionPane.INFORMATION_MESSAGE);
					}
				}
			} catch (Exception sqlE) {
				sqlE.printStackTrace();
				JOptionPane.showMessageDialog(null, "Please select a record in the list to deleted.",
						"No Record Selected", JOptionPane.INFORMATION_MESSAGE);
			}
		} else if (e.getActionCommand() == "CityViewPage") {
			try {
				if (table.getValueAt(table.getSelectedRow(), table.getSelectedColumn()) != null) {
					String ObjButtons[] = { "Yes", "No" };
					int PromptResult = JOptionPane.showOptionDialog(null,
							"Are you sure you want to delete the selected record?", "Delete Record",
							JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, ObjButtons, ObjButtons[1]);
					if (PromptResult == 0) {
						String sql = "DELETE FROM CITY WHERE cityID = " + table.getValueAt(table.getSelectedRow(), 0);
						System.out.println(sql);
						CityViewPage.stCity.execute(sql);
						CityViewPage.reloadRecord();
						JOptionPane.showMessageDialog(null, "Record has been successfully deleted.", "Comfirm Delete",
								JOptionPane.INFORMATION_MESSAGE);
					}
				}
			} catch (Exception sqlE) {
				sqlE.printStackTrace();
				JOptionPane.showMessageDialog(null, "Please select a record in the list to deleted.",
						"No Record Selected", JOptionPane.INFORMATION_MESSAGE);
			}
		} else if (e.getActionCommand() == "VehicleViewPage") {
			try {
				if (table.getValueAt(table.getSelectedRow(), table.getSelectedColumn()) != null) {
					String ObjButtons[] = { "Yes", "No" };
					int PromptResult = JOptionPane.showOptionDialog(null,
							"Are you sure you want to delete the selected record?", "Delete Record",
							JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, ObjButtons, ObjButtons[1]);
					if (PromptResult == 0) {
						String sql = "DELETE FROM Vehicles WHERE vehicleID = "
								+ table.getValueAt(table.getSelectedRow(), 0);
						System.out.println(sql);
						VehicleViewPage.stVehicle.execute(sql);
						VehicleViewPage.reloadRecord();
						JOptionPane.showMessageDialog(null, "Record has been successfully deleted.", "Comfirm Delete",
								JOptionPane.INFORMATION_MESSAGE);
					}
				}
			} catch (Exception sqlE) {
				sqlE.printStackTrace();
				JOptionPane.showMessageDialog(null, "Please select a record in the list to deleted.",
						"No Record Selected", JOptionPane.INFORMATION_MESSAGE);
			}
		} else if (e.getActionCommand() == "FuelTypeViewPage") {
			try {
				if (table.getValueAt(table.getSelectedRow(), table.getSelectedColumn()) != null) {
					String ObjButtons[] = { "Yes", "No" };
					int PromptResult = JOptionPane.showOptionDialog(null,
							"Are you sure you want to delete the selected record?", "Delete Record",
							JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, ObjButtons, ObjButtons[1]);
					if (PromptResult == 0) {
						String sql = "DELETE FROM FUELTYPE WHERE FuelType_ID = "
								+ table.getValueAt(table.getSelectedRow(), 0);
						System.out.println(sql);
						FuelTypeViewPage.stFuelType.execute(sql);
						FuelTypeViewPage.reloadRecord();
						JOptionPane.showMessageDialog(null, "Record has been successfully deleted.", "Comfirm Delete",
								JOptionPane.INFORMATION_MESSAGE);
					}
				}
			} catch (Exception sqlE) {
				sqlE.printStackTrace();
				JOptionPane.showMessageDialog(null, "Please select a record in the list to deleted.",
						"No Record Selected", JOptionPane.INFORMATION_MESSAGE);
			}
		} else if (e.getActionCommand() == "HouseTypeViewPage") {
			try {
				if (table.getValueAt(table.getSelectedRow(), table.getSelectedColumn()) != null) {
					String ObjButtons[] = { "Yes", "No" };
					int PromptResult = JOptionPane.showOptionDialog(null,
							"Are you sure you want to delete the selected record?", "Delete Record",
							JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, ObjButtons, ObjButtons[1]);
					if (PromptResult == 0) {
						String sql = "DELETE FROM HouseTYPE WHERE House_ID = "
								+ table.getValueAt(table.getSelectedRow(), 0);
						System.out.println(sql);
						HouseTypeViewPage.stHouseType.execute(sql);
						HouseTypeViewPage.reloadRecord();
						JOptionPane.showMessageDialog(null, "Record has been successfully deleted.", "Comfirm Delete",
								JOptionPane.INFORMATION_MESSAGE);
					}
				}
			} catch (Exception sqlE) {
				sqlE.printStackTrace();
				JOptionPane.showMessageDialog(null, "Please select a record in the list to deleted.",
						"No Record Selected", JOptionPane.INFORMATION_MESSAGE);
			}
		}

	}
}