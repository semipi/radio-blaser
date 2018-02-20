package de.tkn.ui;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.table.DefaultTableModel;

import de.tkn.core.model.ModelProvider;

public class MonitorTableModel<T extends Comparable<T>> extends DefaultTableModel implements ModelProvider<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final String[] header;
	
	private final Set<T> unique = new HashSet<>();
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
	
	@Override
	public Object getValueAt(final int rowIndex, final int columnIndex) {
		return extractor.extract(model.get(rowIndex), columnIndex);
	}

	@Override
	public MonitorTableModel<T> add(final T t) {
		if(t == null) {
			return this;
		}
		
		if(!unique.contains(t)) {
			unique.add(t);
			model.add(t);
			fireTableRowsInserted(model.size() - 1, model.size() - 1);
		}
		
		return this;
	}

	@Override
	public MonitorTableModel<T> clear() {
		model.clear();
		return this;
	}

	@Override
	public int size() {
		return model.size();
	}
}
