package es.prodevelop.pui9.service.interfaces;

import es.prodevelop.pui9.model.dao.interfaces.INullTableDao;
import es.prodevelop.pui9.model.dao.interfaces.INullViewDao;
import es.prodevelop.pui9.model.dto.interfaces.INullTable;
import es.prodevelop.pui9.model.dto.interfaces.INullTablePk;
import es.prodevelop.pui9.model.dto.interfaces.INullView;

public interface INullService
		extends IService<INullTablePk, INullTable, INullView, INullTableDao, INullViewDao> {

}
