package es.prodevelop.pui9.model.dto;

import es.prodevelop.pui9.model.dto.interfaces.INullTablePk;
import es.prodevelop.pui9.utils.PuiObjectUtils;

/**
 * A Null VDto used when no View is associated with a Table
 */
public class NullTablePk extends AbstractTableDto implements INullTablePk {

	private static final long serialVersionUID = 1L;

	@Override
	@SuppressWarnings("unchecked")
	public NullTablePk createPk() {
		NullTablePk pk = new NullTablePk();
		PuiObjectUtils.copyProperties(pk, this);
		return pk;
	}

}
