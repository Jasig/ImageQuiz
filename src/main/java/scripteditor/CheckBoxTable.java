/**
 * Licensed to Apereo under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Apereo licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a
 * copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package scripteditor;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.math.BigDecimal;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

public class CheckBoxTable extends AbstractTableModel implements KeyListener {
	private ColoumData m_column[] = null;
	private JTable jtable;
	private Vector vrow;
	private Vector delrow;
	private Vector isColumneditable;
	private Vector isCelleditable;
	private Vector constraints;
	private Vector numberconstraints;
	private Vector dependablecomboboxcolumn;
	public Vector dummyconstraints;
	private int serialnumbercolumn;
	private int row;
	private int column;
	private int prevcode;
	private boolean wheatherNextRow;
	private boolean wheatherDeleteRow;
	private Vector decimalscale;

	String currentdummyvalue = "";

	public CheckBoxTable() {

	}

	public void initialiseJTable(JTable jtable, Vector TableColumnNames) {

		vrow = new Vector();
		delrow = new Vector();

		isColumneditable = new Vector();

		isCelleditable = new Vector();

		constraints = new Vector();
		numberconstraints = new Vector();
		dummyconstraints = new Vector();

		decimalscale = new Vector();

		dependablecomboboxcolumn = new Vector();

		prevcode = 0;
		wheatherNextRow = true;
		wheatherDeleteRow = true;

		serialnumbercolumn = -1;
		currentdummyvalue = "";

		m_column = new ColoumData[TableColumnNames.size()];

		for (int i = 0; i < TableColumnNames.size(); i++) {

			m_column[i] = new ColoumData(
					(String) TableColumnNames.elementAt(i), 50, 0);

			isColumneditable.add(new Boolean(false));

			constraints.add(new Boolean(false));
			numberconstraints.add("String");

			dummyconstraints.add(new Integer(-1));

			decimalscale.add(new Integer(2));

			dependablecomboboxcolumn.add(new Boolean(false));

		}

		this.jtable = jtable;

		jtable.setRowSelectionAllowed(true);

		jtable.setColumnSelectionAllowed(false);
		jtable.addKeyListener(this);
		JTableHeader header = jtable.getTableHeader();
		header.setUpdateTableInRealTime(true);
		header.setReorderingAllowed(false);

		refreshTable();

	}

	public void refreshTable() {

		if (serialnumbercolumn > -1) {

			for (int i = 0; i < vrow.size(); i++) {
				Vector eachrow = new Vector();
				eachrow = (Vector) ((Vector) vrow.elementAt(i)).clone();

				String temp = new String();
				try {

					eachrow.set(serialnumbercolumn, String.valueOf(i + 1));
					vrow.set(i, eachrow);

				} catch (Exception e) {

				}

			}

		}

		jtable.tableChanged(new TableModelEvent(this));
		jtable.repaint();
		jtable.setModel(this);

	}

	public void setColumnSerialNumber(int colindex) {
		serialnumbercolumn = colindex;

	}

	public int getRowCount() {
		return vrow == null ? 0 : vrow.size();
	}

	public int getColumnCount() {
		return m_column.length;
	}

	public String getColumnName(int column) {
		return m_column[column].m_title;
	}

	public boolean isCellEditable(int nRow, int nCol) {

		for (int i = 0; i < isCelleditable.size(); i++) {

			Vector single = (Vector) isCelleditable.elementAt(i);

			int rowvalue = ((Integer) single.elementAt(0)).intValue();
			int colvalue = ((Integer) single.elementAt(1)).intValue();
			boolean enableflag = ((Boolean) single.elementAt(2)).booleanValue();

			if (rowvalue == nRow && colvalue == nCol)
				return enableflag;

		}

		if (((Boolean) isColumneditable.elementAt(nCol)).booleanValue())
			return true;
		else
			return false;

	}

	public void setCellEditable(boolean flag, int columnindex) {
		if (columnindex < 0 || columnindex >= getColumnCount())
			return;

		isColumneditable.set(columnindex, new Boolean(flag));

	}

	public void setAllCellEditable(boolean flag) {

		for (int i = 0; i < isColumneditable.size(); i++) {
			isColumneditable.set(i, new Boolean(flag));

		}

	}

	public void setAllCellConstraints(boolean flag) {

		for (int i = 0; i < constraints.size(); i++) {
			constraints.set(i, new Boolean(flag));

		}

	}

	public void setAllCellNumberConstraints(String value) {

		if (value == null) {
			return;
		}

		value = value.trim();

		if (!(value.equalsIgnoreCase("String"))
				&& !(value.equalsIgnoreCase("float"))
				&& !(value.equalsIgnoreCase("int"))) {

			return;

		}

		for (int i = 0; i < numberconstraints.size(); i++) {
			numberconstraints.set(i, value);

		}

	}

	public void setCellNumberConstraints(String value, int columnindex) {
		if (columnindex < 0 || columnindex >= getColumnCount())
			return;

		if (value == null) {
			return;
		}

		value = value.trim();

		if (!(value.equalsIgnoreCase("String"))
				&& !(value.equalsIgnoreCase("float"))
				&& !(value.equalsIgnoreCase("int"))) {

			return;

		}

		numberconstraints.set(columnindex, value);

		if (value.equalsIgnoreCase("float") || value.equalsIgnoreCase("int")) {

			setCellHorizontalAlignment(JLabel.RIGHT, columnindex);

		}

		refreshTable();

	}

	public void setCellConstraints(boolean flag, int columnindex) {
		if (columnindex < 0 || columnindex >= getColumnCount())
			return;

		constraints.set(columnindex, new Boolean(flag));

	}

	public void setDependableComboboxColumn(boolean flag, int columnindex) {

		if (columnindex < 0 || columnindex >= getColumnCount())
			return;

		dependablecomboboxcolumn.set(columnindex, new Boolean(flag));

	}

	public Class getColumnClass(int c) {

		return getValueAt(0, c).getClass();
	}

	public Object getValueAt(int nRow, int nCol) {

		Vector row = (Vector) vrow.elementAt(nRow);
		Object val = row.elementAt(nCol);

		return val;
	}

	public void setValueAt(Object value, int nRow, int nCol) {
		Vector rowdata = (Vector) vrow.elementAt(nRow);
		if (value instanceof Boolean) {
			rowdata.set(nCol, (Boolean) value);
			this.vrow.set(nRow, rowdata);
		}

		else if (value instanceof String) {
			Vector column = (Vector) vrow.elementAt(nRow);
			if (((String) numberconstraints.elementAt(nCol))
					.equalsIgnoreCase("int")) {
				try {
					if (value.equals(""))
						value = "0";
					Integer.parseInt(value.toString());
				} catch (Exception e) {
					Utilities.MessageDialog(this.jtable.getParent(), "Invalid Number");
	                //JOptionPane.showMessageDialog(this.jtable.getParent(), "Invalid Number");
					return;
				}
			} else if (((String) numberconstraints.elementAt(nCol))
					.equalsIgnoreCase("float")) {
				try {
					if (value.equals(""))
						value = "0.0";
					BigDecimal bg = new BigDecimal(value.toString());
					value = (bg.setScale(((Integer) decimalscale
							.elementAt(nCol)).intValue(),
							BigDecimal.ROUND_HALF_UP)).toString();
				} catch (Exception e) {
					Utilities.MessageDialog(this.jtable.getParent(), "Invalid Number");
	                //JOptionPane.showMessageDialog(this.jtable.getParent(), "Invalid Number");
					return;
				}

			}
			column.set(nCol, value);
			vrow.set(nRow, column);
		}
		if (nCol > 1) {

			String prim = (String) getValueAt(nRow, 0);
			if (prim != null) {
				prim = prim.trim();
				if (!(prim.equals(""))) {
					String aed = (String) getValueAt(nRow, 1);
					if (aed == null || aed.equals("")
							|| !((aed.trim()).equals("E"))) {
						setValueAt("E", nRow, 1);
					}
				}
			}
		}
		fireTableChanged(new TableModelEvent(this, nRow, nRow, nCol));
	}

	public void addSingleDataRow() {

		if (vrow != null) {
			vrow.removeAllElements();
		}
		Vector column = new Vector();

		for (int i = 0; i < m_column.length; i++) {

			if (((String) numberconstraints.elementAt(i))
					.equalsIgnoreCase("int")) {
				column.add("0");
			} else if (((String) numberconstraints.elementAt(i))
					.equalsIgnoreCase("float")) {

				String val = "0.0";
				BigDecimal bg = new BigDecimal(val);
				bg = bg.setScale(((Integer) decimalscale.elementAt(i))
						.intValue(), BigDecimal.ROUND_HALF_UP);
				column.add(bg.toString());

			} else {
				column.add("");
			}

		}

		vrow.add(column);
		refreshTable();

	}

	public Vector deleteSelectedRow(int pos) {

		int messageint = pos + 1;
		if (messageint == 0) {
			Utilities.MessageDialog(this.jtable.getParent(), "No rows selected");
			//JOptionPane.showMessageDialog(this.jtable.getParent(), "No rows selected");
			return null;
		}


		int optionstatus = Utilities.ConfirmDialog(this.jtable.getParent(), "Do you want to delete " + messageint + " row", "Delete Row?");
		//int optionstatus = JOptionPane.showConfirmDialog(this.jtable.getParent(), "Do you want to delete " + messageint + " row", "Delete Row?", JOptionPane.YES_NO_OPTION);
		if (optionstatus != 0) {
			return null;
		}

		Vector tempclone = new Vector();
		if (pos > -1 && vrow != null && vrow.size() > 1 && pos < vrow.size()) {

			Vector temp = new Vector();
			temp = (Vector) vrow.elementAt(pos);
			tempclone = (Vector) temp.clone();
			vrow.removeElementAt(pos);

			String prim = (String) tempclone.elementAt(0);

			if (prim != null && !((prim.trim()).equals(""))) {
				tempclone.set(1, "D");
				delrow.add(tempclone);

				refreshTable();

				return tempclone;
			}

		}

		else if (pos > -1 && vrow != null && vrow.size() == 1
				&& pos < vrow.size()) {
			Vector temp = new Vector();
			temp = (Vector) vrow.elementAt(pos);

			tempclone = (Vector) temp.clone();

			removeDataRow();

			String prim = (String) tempclone.elementAt(0);

			if (prim != null && !((prim.trim()).equals(""))) {
				tempclone.set(1, "D");
				delrow.add(tempclone);

				return tempclone;
			}

		}
		refreshTable();

		return null;

	}

	public void removeDataRow()

	{

		if (vrow != null) {
			vrow.removeAllElements();
		}
		refreshTable();

	}

	public void InitiliseDataRow(Vector common) {

		if (common != null && common.size() > 0) {
			vrow.removeAllElements();
			for (int i = 0; i < common.size(); i++) {
				Vector eachrow = new Vector();
				eachrow = (Vector) ((Vector) common.elementAt(i)).clone();
				if (eachrow.size() == m_column.length) {

					for (int j = 0; j < eachrow.size(); j++) {

						if (((String) numberconstraints.elementAt(j))
								.equalsIgnoreCase("float")) {

							String temp = (String) eachrow.elementAt(j);
							if (temp != null)
								temp = temp.trim();
							BigDecimal bg = null;
							if (temp != null && !(temp.equals(""))) {

								try {
									bg = new BigDecimal(temp);
									bg = bg.setScale(((Integer) decimalscale
											.elementAt(j)).intValue(),
											BigDecimal.ROUND_HALF_UP);
									eachrow.set(j, bg.toString());

								} catch (Exception e) {
									String val = "0.0";
									bg = new BigDecimal(val);
									bg = bg.setScale(((Integer) decimalscale
											.elementAt(j)).intValue(),
											BigDecimal.ROUND_HALF_UP);
									eachrow.set(j, bg.toString());
								}

							} else {

								String val = "0.0";
								bg = new BigDecimal(val);
								bg = bg.setScale(((Integer) decimalscale
										.elementAt(j)).intValue(),
										BigDecimal.ROUND_HALF_UP);
								eachrow.set(j, bg.toString());
							}
						}
					}

					vrow.add(eachrow);

				}

			}
			refreshTable();
			return;
		}

		else {
			addSingleDataRow();
			refreshTable();

			return;
		}

	}

	public void addDataRow() {

		boolean flag = true;
		for (int i = 0; i < vrow.size(); i++) {
			Vector temp = new Vector();
			temp = (Vector) vrow.elementAt(i);

			for (int j = 0; j < temp.size(); j++) {

				if (((Boolean) constraints.elementAt(j)).booleanValue()) {
					String tempstr = new String();

					tempstr = (String) temp.elementAt(j);

					if (tempstr != null)
						tempstr = tempstr.trim();

					if (((String) numberconstraints.elementAt(j))
							.equalsIgnoreCase("float")
							|| ((String) numberconstraints.elementAt(j))
									.equalsIgnoreCase("int"))

					{
						try {
							if (Double.parseDouble(tempstr) == 0) {
								flag = false;
								break;
							}

						} catch (Exception e) {

						}

					} else {

						if (tempstr == null || (tempstr.equals(""))) {

							flag = false;
							break;
						}

					}

				}
			}

		}

		if (flag)

		{
			Vector column = new Vector();

			for (int i = 0; i < m_column.length; i++) {

				if (((String) numberconstraints.elementAt(i))
						.equalsIgnoreCase("int")) {
					column.add("0");
				} else if (((String) numberconstraints.elementAt(i))
						.equalsIgnoreCase("float")) {

					String val = "0.0";
					BigDecimal bg = new BigDecimal(val);
					bg = bg.setScale(((Integer) decimalscale.elementAt(i))
							.intValue(), BigDecimal.ROUND_HALF_UP);
					column.add(bg.toString());

				} else {
					column.add("");
				}

			}

			vrow.add(column);

			refreshTable();

		}

	}

	public void addSingleRowToExistingTable(Vector column) {

		if (column.size() == m_column.length) {

			boolean flag = true;

			for (int j = 0; j < column.size(); j++) {

				if (((Boolean) constraints.elementAt(j)).booleanValue()) {

					String tempstr = new String();
					tempstr = (String) column.elementAt(j);

					if (tempstr != null)
						tempstr = tempstr.trim();

					if (((String) numberconstraints.elementAt(j))
							.equalsIgnoreCase("float")
							|| ((String) numberconstraints.elementAt(j))
									.equalsIgnoreCase("int"))

					{
						try {
							if (Double.parseDouble(tempstr) == 0) {
								flag = false;
								break;
							}

						} catch (Exception e) {

						}

					}

					else {
						if (tempstr == null || (tempstr.equals(""))) {

							flag = false;
							break;
						}

					}

				}

			}

			if (flag) {

				Vector eachrow = new Vector();
				eachrow = (Vector) column.clone();

				for (int j = 0; j < eachrow.size(); j++) {

					if (((String) numberconstraints.elementAt(j))
							.equalsIgnoreCase("float")) {

						String temp = (String) eachrow.elementAt(j);
						if (temp != null)
							temp = temp.trim();
						BigDecimal bg = null;
						if (temp != null && !(temp.equals(""))) {

							try {
								bg = new BigDecimal(temp);
								bg = bg.setScale(((Integer) decimalscale
										.elementAt(j)).intValue(),
										BigDecimal.ROUND_HALF_UP);
								eachrow.set(j, bg.toString());
							} catch (Exception e) {
								String val = "0.0";
								bg = new BigDecimal(val);
								bg = bg.setScale(((Integer) decimalscale
										.elementAt(j)).intValue(),
										BigDecimal.ROUND_HALF_UP);
								eachrow.set(j, bg.toString());

							}

						} else {

							String val = "0.0";
							bg = new BigDecimal(val);
							bg = bg.setScale(((Integer) decimalscale
									.elementAt(j)).intValue(),
									BigDecimal.ROUND_HALF_UP);
							eachrow.set(j, bg.toString());

						}

					}
				}

				vrow.add(eachrow);
				refreshTable();

			}

		}

	}

	public void addDataRowsToExistingTable(Vector common) {

		if (common != null && common.size() > 0) {

			for (int i = 0; i < common.size(); i++) {
				Vector eachrow = new Vector();
				eachrow = (Vector) ((Vector) common.elementAt(i)).clone();

				if (eachrow.size() == m_column.length) {

					for (int j = 0; j < eachrow.size(); j++) {

						if (((String) numberconstraints.elementAt(j))
								.equalsIgnoreCase("float")) {

							String temp = (String) eachrow.elementAt(j);
							if (temp != null)
								temp = temp.trim();
							BigDecimal bg = null;
							if (temp != null && !(temp.equals(""))) {

								try {
									bg = new BigDecimal(temp);
									bg = bg.setScale(((Integer) decimalscale
											.elementAt(j)).intValue(),
											BigDecimal.ROUND_HALF_UP);
									eachrow.set(j, bg.toString());
								} catch (Exception e) {
									String val = "0.0";
									bg = new BigDecimal(val);
									bg = bg.setScale(((Integer) decimalscale
											.elementAt(j)).intValue(),
											BigDecimal.ROUND_HALF_UP);
									eachrow.set(j, bg.toString());

								}

							} else {

								String val = "0.0";
								bg = new BigDecimal(val);
								bg = bg.setScale(((Integer) decimalscale
										.elementAt(j)).intValue(),
										BigDecimal.ROUND_HALF_UP);
								eachrow.set(j, bg.toString());

							}

						}
					}

					vrow.add(eachrow);

				}

			}

			refreshTable();

		}

	}

	public Vector getColumn(int colindex) {

		if (colindex < 0 || colindex >= getColumnCount())
			return (new Vector());

		Vector tempvrow = new Vector();

		for (int i = 0; i < getRowCount(); i++) {

			String temp = (String) getValueAt(i, colindex);

			if (temp != null && !((temp.trim()).equals(""))) {
				tempvrow.add(temp);

			}

		}

		return (tempvrow);

	}

	public Vector getAllValuesFromTable() {
		Vector tempvrow = new Vector();

		for (int i = 0; i < vrow.size(); i++) {
			Vector eachrow = new Vector();
			eachrow = (Vector) ((Vector) vrow.elementAt(i)).clone();

			if (eachrow.size() == m_column.length) {

				tempvrow.add(eachrow);

			}

		}

		return (tempvrow);
	}

	public void setBackground(Color color) {

		jtable.setBackground(color);
	}

	public void setForeground(Color color) {

		jtable.setForeground(color);
	}

	public void setPreferredWidth(int width, int columnindex) {
		if (columnindex < 0 || columnindex >= getColumnCount())
			return;

		TableColumn column = jtable.getTableHeader().getColumnModel()
				.getColumn(columnindex);
		column.setPreferredWidth(width);
	}

	public void setCellInvisible(int columnindex) {
		if (columnindex < 0 || columnindex >= getColumnCount())
			return;

		TableColumn column = jtable.getTableHeader().getColumnModel()
				.getColumn(columnindex);
		column.setPreferredWidth(0);
		column.setMinWidth(0);
		column.setMaxWidth(0);
		// fireTableStructureChanged();

	}

	public void setCellVisible(int columnindex, int width) {
		if (columnindex < 0 || columnindex >= getColumnCount())
			return;

		TableColumn column = jtable.getTableHeader().getColumnModel()
				.getColumn(columnindex);
		column.setPreferredWidth(width);
		column.setMinWidth(width);
		column.setMaxWidth(200);
		fireTableStructureChanged();

	}

	public void fireStructureChanged() {
		this.fireTableStructureChanged();
	}

	public final void setColumnName(int columnindex, String columnname) {
		if (columnindex < 0 || columnindex >= m_column.length)
			return;

		m_column[columnindex].m_title = columnname;

		TableColumn column = jtable.getTableHeader().getColumnModel()
				.getColumn(columnindex);

		column.setHeaderValue(columnname);

	}

	public void setAutoResizeMode(int mode) {

		jtable.setAutoResizeMode(mode);
	}

	public void setCellForeground(Color color, int columnindex) {

		if (columnindex < 0 || columnindex >= getColumnCount())
			return;

		DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
		TableColumn column = jtable.getTableHeader().getColumnModel()
				.getColumn(columnindex);
		renderer.setForeground(color);
		column.setCellRenderer(renderer);
	}

	public void setCellFont(Font font, int columnindex) {
		if (columnindex < 0 || columnindex >= getColumnCount())
			return;

		DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
		TableColumn column = jtable.getTableHeader().getColumnModel()
				.getColumn(columnindex);
		renderer.setFont(font);
		column.setCellRenderer(renderer);

	}

	public void setCellBackground(Color color, int columnindex) {
		if (columnindex < 0 || columnindex >= getColumnCount())
			return;

		DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
		TableColumn column = jtable.getTableHeader().getColumnModel()
				.getColumn(columnindex);
		renderer.setBackground(color);
		column.setCellRenderer(renderer);

	}

	public void setCellVerticalAlignment(int align, int columnindex) {
		if (columnindex < 0 || columnindex >= getColumnCount())
			return;

		DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
		TableColumn column = jtable.getTableHeader().getColumnModel()
				.getColumn(columnindex);
		renderer.setVerticalAlignment(align);
		column.setCellRenderer(renderer);

	}

	public void setCellHorizontalAlignment(int align, int columnindex) {
		if (columnindex < 0 || columnindex >= getColumnCount())
			return;

		DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
		TableColumn column = jtable.getTableHeader().getColumnModel()
				.getColumn(columnindex);
		renderer.setHorizontalAlignment(align);
		column.setCellRenderer(renderer);

	}

	public void setEnableOrDisableCellAt(boolean flag, int row, int col) {

		if (col < 0 || col >= getColumnCount())
			return;

		if (row < 0 || row >= getRowCount())
			return;

		boolean tempflag = false;

		for (int i = 0; i < isCelleditable.size(); i++) {
			Vector single = new Vector();
			single = (Vector) ((Vector) isCelleditable.elementAt(i)).clone();

			int rowvalue = ((Integer) single.elementAt(0)).intValue();
			int colvalue = ((Integer) single.elementAt(1)).intValue();

			if (rowvalue == row && colvalue == col) {

				single.set(2, new Boolean(flag));
				isCelleditable.set(i, single);
				tempflag = true;

			}

		}

		if (!tempflag) {

			Vector single = new Vector();

			single.add(new Integer(row));
			single.add(new Integer(col));
			single.add(new Boolean(flag));

			isCelleditable.add(single);

		}

		// refreshTable();

	}

	public void setEnableOrDisableRowAt(boolean flag, int row) {

		if (row < 0 || row >= getRowCount())
			return;

		for (int j = 0; j < getColumnCount(); j++) {

			boolean tempflag = false;

			for (int i = 0; i < isCelleditable.size(); i++) {

				Vector single = new Vector();
				single = (Vector) ((Vector) isCelleditable.elementAt(i))
						.clone();

				int rowvalue = ((Integer) single.elementAt(0)).intValue();
				int colvalue = ((Integer) single.elementAt(1)).intValue();

				if (rowvalue == row && colvalue == j) {

					single.set(2, new Boolean(flag));
					isCelleditable.set(i, single);
					tempflag = true;

				}

			}

			if (!tempflag) {

				Vector single = new Vector();

				single.add(new Integer(row));
				single.add(new Integer(j));
				single.add(new Boolean(flag));

				isCelleditable.add(single);

			}

		}

		refreshTable();

	}

	public void setRefreshEnableOrDisableCells() {
		isCelleditable = new Vector();
		refreshTable();

	}

	public void keyPressed(KeyEvent keyEvent) {

	}

	public void keyReleased(KeyEvent keyEvent) {

	}

	public void keyTyped(KeyEvent keyEvent) {

	}

}

class ColoumData {
	public String m_title;
	public int m_width;
	public int m_alignment;

	public ColoumData(String title, int width, int alignment) {
		m_title = title;
		m_width = width;
		m_alignment = alignment;
	}
}
