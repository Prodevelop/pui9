package es.prodevelop.pui9.model.dto;

import es.prodevelop.pui9.annotations.PuiEntity;
import es.prodevelop.pui9.model.dto.interfaces.INullView;

/**
 * A Null VDto used when no View is associated with a Table
 */
@PuiEntity(tablename = "")
public class NullView extends AbstractViewDto implements INullView {

	private static final long serialVersionUID = 1L;

}
