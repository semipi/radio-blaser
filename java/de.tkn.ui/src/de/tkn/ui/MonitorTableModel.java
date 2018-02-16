package de.tkn.ui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.DefaultTableModel;

public class MonitorTableModel<T> extends DefaultTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final String[] header;
	
	private final List<T> model = new ArrayList<>();
	private final Extractor<T> extractor;
	
	public MonitorTableModel(final String[] header, final Extractor<T> extractor) {
		this.header = header;
		this.extractor = extractor;
	}
	
	@Override
	public int getColumnCount() {
		return header.length;
	}
	
	@Override
	public int getRowCount() {
		if(model == null) {
			return super.getRowCount();
		} else {
			return model.size();
		}
	}
	
	@Override
	public String getColumnName(final int column) {
		return header[column];
	}

	public void clear() {
		model.clear();
	}
	
	public void add(final T t) {
		model.add(t);
		fireTableRowsInserted(model.size() - 1, model.size() - 1);
	}
	
	@Override
	public Object getValueAt(final int rowIndex, final int columnIndex) {
		return extractor.extract(model.get(rowIndex), columnIndex);
	}
}
