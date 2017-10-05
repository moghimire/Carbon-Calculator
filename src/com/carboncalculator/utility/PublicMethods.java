package com.carboncalculator.utility;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.JComboBox;

import com.carboncalculator.forms.ClientViewPage;

public class PublicMethods {

	/**
	 * Global variable clientIDMap is updated with the new client id and client
	 * name
	 */
	public static void fillClientIDMap(String strSQL, Connection sCN) {
		Statement stFC;
		ResultSet rsFC;
		int index = 0;
		GlobalValues.clientIDMap = new LinkedHashMap<>();
		try {
			stFC = sCN.createStatement();
			rsFC = stFC.executeQuery(strSQL);
			while (rsFC.next()) {
				String clientName = rsFC.getString("firstName")
						+ (rsFC.getString("middleName").isEmpty() ? " " + rsFC.getString("lastName")
								: " " + rsFC.getString("middleName") + " " + rsFC.getString("lastName"));
				GlobalValues.clientIDMap.put(index, rsFC.getInt("clientId"));
				index++;
			}
		} catch (SQLException sqlEx) {
			System.out.println("\nERROR IN PublicMethods(fillCombo):" + sqlEx + "\n");
		}
		rsFC = null;
		stFC = null;
	}

	/*
	 * @return maximumNumber of rows
	 **/
	public static int getMaxNum(String strSQL, Connection sCN, String strFieldName) {
		Statement stMaxRow;
		ResultSet rsMaxRow;
		int Max = 0;
		try {
			stMaxRow = sCN.createStatement();
			rsMaxRow = stMaxRow.executeQuery(strSQL);
			if (rsMaxRow.next()) {
				Max = rsMaxRow.getInt(strFieldName);
			}
		} catch (SQLException sqlEx) {
			System.out.println("\nERROR IN clsPublicMethods(getMaxNum):" + sqlEx + "\n");
		}

		rsMaxRow = null;
		stMaxRow = null;
		return Max;
	}

	/** @return id */
	public static int getId(String strSQL, Connection sCN, String strFieldName) {
		Statement st;
		ResultSet rs;
		int id = 0;
		try {
			st = sCN.createStatement();
			rs = st.executeQuery(strSQL);
			if (rs.next()) {
				id = rs.getInt(strFieldName);
			}
		} catch (SQLException sqlEx) {
			System.out.println("\nERROR IN PublicMethods(getId):" + sqlEx + "\n");
			System.out.println("\nERROR SQL:" + strSQL + "\n");
		}

		rs = null;
		st = null;
		return id;
	}

	/** @return jcombobox by filling it */
	public static JComboBox fillCombo(String strSQL, Connection sCN, String strFieldName) {
		Statement stFC;
		ResultSet rsFC;
		ArrayList<String> cmbList = new ArrayList<>();
		try {
			stFC = sCN.createStatement();
			rsFC = stFC.executeQuery(strSQL);
			while (rsFC.next()) {
				cmbList.add(rsFC.getString(strFieldName));
			}
		} catch (SQLException sqlEx) {
			System.out.println("\nERROR IN PublicMethods(fillCombo):" + sqlEx + "\n");
		}
		rsFC = null;
		stFC = null;
		return new JComboBox(cmbList.toArray());
	}

	/** @return jcombobox by filling it */
	public static JComboBox fillComboEnergy(String strSQL, Connection sCN, String strFieldName) {
		Statement stFC;
		ResultSet rsFC;
		ArrayList<String> cmbList = new ArrayList<>();
		cmbList.add("All");
		try {
			stFC = sCN.createStatement();
			rsFC = stFC.executeQuery(strSQL);
			while (rsFC.next()) {
				cmbList.add(rsFC.getString(strFieldName));
			}
		} catch (SQLException sqlEx) {
			System.out.println("\nERROR IN PublicMethods(fillCombo):" + sqlEx + "\n");
		}
		rsFC = null;
		stFC = null;
		return new JComboBox(cmbList.toArray());
	}

	/** @return entries for jcombobx */
	public static Object[] getComboEntry(String strSQL, Connection sCN, String strFieldName) {
		Statement stFC;
		ResultSet rsFC;
		ArrayList<String> cmbList = new ArrayList<>();
		cmbList.add("All");
		try {
			stFC = sCN.createStatement();
			rsFC = stFC.executeQuery(strSQL);
			while (rsFC.next()) {
				cmbList.add(rsFC.getString(strFieldName));
			}
		} catch (SQLException sqlEx) {
			System.out.println("\nERROR IN PublicMethods(fillCombo):" + sqlEx + "\n");
		}
		rsFC = null;
		stFC = null;
		return cmbList.toArray();
	}

	/** @return jcombobox by filling it and by adding ALL in the first index */
	public static JComboBox fillComboClientAll(String strSQL, Connection sCN) {
		Statement stFC;
		ResultSet rsFC;
		ArrayList<String> cmbList = new ArrayList<>();
		cmbList.add("All");
		try {
			stFC = sCN.createStatement();
			rsFC = stFC.executeQuery(strSQL);
			// GlobalValues.clientIDMap = new LinkedHashMap<>();
			while (rsFC.next()) {
				String clientName = rsFC.getString("firstName")
						+ (rsFC.getString("middleName").isEmpty() ? " " + rsFC.getString("lastName")
								: " " + rsFC.getString("middleName") + " " + rsFC.getString("lastName"));
				cmbList.add(clientName);
			}
		} catch (SQLException sqlEx) {
			System.out.println("\nERROR IN PublicMethods(fillCombo):" + sqlEx + "\n");
		}
		rsFC = null;
		stFC = null;
		return new JComboBox(cmbList.toArray());
	}

	/**
	 * @return jcombobox by filling it and without adding ALL in the first index
	 */
	public static JComboBox fillComboClient(String strSQL, Connection sCN) {
		Statement stFC;
		ResultSet rsFC;
		ArrayList<String> cmbList = new ArrayList<>();

		try {
			stFC = sCN.createStatement();
			rsFC = stFC.executeQuery(strSQL);
			while (rsFC.next()) {
				String clientName = rsFC.getString("firstName")
						+ (rsFC.getString("middleName").isEmpty() ? " " + rsFC.getString("lastName")
								: " " + rsFC.getString("middleName") + " " + rsFC.getString("lastName"));
				cmbList.add(clientName);
			}
		} catch (SQLException sqlEx) {
			System.out.println("\nERROR IN PublicMethods(fillCombo):" + sqlEx + "\n");
		}
		rsFC = null;
		stFC = null;
		return new JComboBox(cmbList.toArray());
	}

	/** @return jcombobox for number of beds */
	public static JComboBox fillComboBeds() {
		ArrayList<Integer> cmbList = new ArrayList<>();

		cmbList.add(0);
		cmbList.add(1);
		cmbList.add(2);
		cmbList.add(3);
		cmbList.add(4);
		cmbList.add(5);
		cmbList.add(6);
		cmbList.add(7);
		cmbList.add(8);
		cmbList.add(9);
		cmbList.add(10);
		cmbList.add(11);
		cmbList.add(12);
		cmbList.add(13);
		cmbList.add(14);
		cmbList.add(15);

		return new JComboBox(cmbList.toArray());
	}

	/** @return id-value map for fueltype-id and cityname-id */
	public static Map<Integer, String> getIdValueMap(String strSQL, Connection sCN, String keyFieldName,
			String valueFieldName) {
		Statement stFC;
		ResultSet rsFC;
		Map idValueMap = new HashMap<Integer, String>();
		try {
			stFC = sCN.createStatement();
			rsFC = stFC.executeQuery(strSQL);
			while (rsFC.next()) {
				idValueMap.put(rsFC.getInt(keyFieldName), rsFC.getString(valueFieldName));
			}
		} catch (SQLException sqlEx) {
			System.out.println("\nERROR IN PublicMethods(fillCombo):" + sqlEx + "\n");
		}
		rsFC = null;
		stFC = null;
		return idValueMap;

	}

}
