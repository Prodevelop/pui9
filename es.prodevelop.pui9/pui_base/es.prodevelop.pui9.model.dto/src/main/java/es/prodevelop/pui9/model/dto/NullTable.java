package es.prodevelop.pui9.model.dto;

import es.prodevelop.pui9.annotations.PuiEntity;
import es.prodevelop.pui9.model.dto.interfaces.INullTable;

/**
 * A Null VDto used when no View is associated with a Table
 */
@PuiEntity(tablename = "")
public class NullTable extends NullTablePk implements INullTable {

	private static final long serialVersionUID = 1L;

}
