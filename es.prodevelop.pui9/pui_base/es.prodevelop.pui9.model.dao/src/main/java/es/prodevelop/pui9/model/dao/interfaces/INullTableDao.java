package es.prodevelop.pui9.model.dao.interfaces;

import es.prodevelop.pui9.model.dto.interfaces.INullTable;
import es.prodevelop.pui9.model.dto.interfaces.INullTablePk;

/**
 * A Null Table Dao to be used in Services without an specified Table
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public interface INullTableDao extends ITableDao<INullTablePk, INullTable> {

}
