package es.prodevelop.pui9.utils.csv;

import java.util.ArrayList;
import java.util.List;

public class CsvHeader {

	private List<String> headers;

	public CsvHeader() {
		headers = new ArrayList<>();
	}

	public void addHeader(String header) {
		headers.add(header);
	}

	public String[] getHeaders() {
		return headers.toArray(new String[0]);
	}
}
