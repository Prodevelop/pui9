package es.prodevelop.pui9.docgen.model.dao;

import org.springframework.stereotype.Repository;

import es.prodevelop.pui9.docgen.model.dao.interfaces.IPuiDocgenTemplateDao;
import es.prodevelop.pui9.docgen.model.dto.interfaces.IPuiDocgenTemplate;
import es.prodevelop.pui9.docgen.model.dto.interfaces.IPuiDocgenTemplatePk;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.AbstractTableDao;

/**
 * @generated
 */
@Repository
public class PuiDocgenTemplateDao extends AbstractTableDao<IPuiDocgenTemplatePk, IPuiDocgenTemplate>
		implements IPuiDocgenTemplateDao {
	/**
	 * @generated
	 */
	@Override
	public java.util.List<IPuiDocgenTemplate> findById(Integer id) throws PuiDaoFindException {
		return super.findByColumn(IPuiDocgenTemplatePk.ID_FIELD, id);
	}

	/**
	 * @generated
	 */
	@Override
	public java.util.List<IPuiDocgenTemplate> findByName(String name) throws PuiDaoFindException {
		return super.findByColumn(IPuiDocgenTemplate.NAME_FIELD, name);
	}

	/**
	 * @generated
	 */
	@Override
	public java.util.List<IPuiDocgenTemplate> findByDescription(String description) throws PuiDaoFindException {
		return super.findByColumn(IPuiDocgenTemplate.DESCRIPTION_FIELD, description);
	}

	/**
	 * @generated
	 */
	@Override
	public java.util.List<IPuiDocgenTemplate> findByMainmodel(String mainmodel) throws PuiDaoFindException {
		return super.findByColumn(IPuiDocgenTemplate.MAIN_MODEL_FIELD, mainmodel);
	}

	/**
	 * @generated
	 */
	@Override
	public java.util.List<IPuiDocgenTemplate> findByModels(String models) throws PuiDaoFindException {
		return super.findByColumn(IPuiDocgenTemplate.MODELS_FIELD, models);
	}

	/**
	 * @generated
	 */
	@Override
	public java.util.List<IPuiDocgenTemplate> findByFilename(String filename) throws PuiDaoFindException {
		return super.findByColumn(IPuiDocgenTemplate.FILENAME_FIELD, filename);
	}

	/**
	 * @generated
	 */
	@Override
	public java.util.List<IPuiDocgenTemplate> findByMapping(String mapping) throws PuiDaoFindException {
		return super.findByColumn(IPuiDocgenTemplate.MAPPING_FIELD, mapping);
	}

	/**
	 * @generated
	 */
	@Override
	public java.util.List<IPuiDocgenTemplate> findByFilter(String filter) throws PuiDaoFindException {
		return super.findByColumn(IPuiDocgenTemplate.FILTER_FIELD, filter);
	}

	/**
	 * @generated
	 */
	@Override
	public java.util.List<IPuiDocgenTemplate> findByParameters(String parameters) throws PuiDaoFindException {
		return super.findByColumn(IPuiDocgenTemplate.PARAMETERS_FIELD, parameters);
	}

	/**
	 * @generated
	 */
	@Override
	public java.util.List<IPuiDocgenTemplate> findByColumnfilename(String columnfilename) throws PuiDaoFindException {
		return super.findByColumn(IPuiDocgenTemplate.COLUMN_FILENAME_FIELD, columnfilename);
	}
}
