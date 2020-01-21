package es.prodevelop.pui9.utils.csv;

import java.util.ArrayList;
import java.util.List;

public class CsvRow {

	private List<String> cells;

	public CsvRow() {
		cells = new ArrayList<>();
	}

	public void addCell(String cell) {
		cells.add(cell);
	}

	public String[] getCells() {
		return cells.toArray(new String[0]);
	}
}