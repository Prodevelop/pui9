package es.prodevelop.pui9.service;

import org.springframework.stereotype.Service;

import es.prodevelop.pui9.model.dao.interfaces.INullTableDao;
import es.prodevelop.pui9.model.dao.interfaces.INullViewDao;
import es.prodevelop.pui9.model.dto.interfaces.INullTable;
import es.prodevelop.pui9.model.dto.interfaces.INullTablePk;
import es.prodevelop.pui9.model.dto.interfaces.INullView;
import es.prodevelop.pui9.service.interfaces.INullService;

@Service
public class NullService
		extends AbstractService<INullTablePk, INullTable, INullView, INullTableDao, INullViewDao>
		implements INullService {

}
