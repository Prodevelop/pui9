package es.prodevelop.pui9.common.exceptions;

public class PuiCommonImportExportInvalidModelException extends AbstractPuiCommonException {

	private static final long serialVersionUID = 1L;

	public static final Integer CODE = 218;

	public PuiCommonImportExportInvalidModelException(String model) {
		super(CODE, model);
	}

}
