package es.prodevelop.pui9.docgen.list.adapters;

import org.springframework.stereotype.Component;

import es.prodevelop.pui9.docgen.model.views.dto.interfaces.IVPuiDocgenTemplate;
import es.prodevelop.pui9.filter.FilterBuilder;
import es.prodevelop.pui9.list.adapters.IListAdapter;
import es.prodevelop.pui9.login.PuiUserSession;
import es.prodevelop.pui9.search.SearchRequest;

/**
 * @author Marc Gil - mgil@prodevelop.es
 */
@Component
public class PuiDocgenTemplateListAdapter implements IListAdapter<IVPuiDocgenTemplate> {

	private static final String WRITE_PUI_DOCGEN_FUNC = "WRITE_PUI_DOCGEN";

	@Override
	public String getFixedFilterParameters(SearchRequest req) {
		FilterBuilder filter = FilterBuilder.newAndFilter();

		if (!PuiUserSession.getCurrentSession().getFunctionalities().contains(WRITE_PUI_DOCGEN_FUNC)) {
			filter.addIsNull(IVPuiDocgenTemplate.MODELS_COLUMN);
		}

		return filter.asFilterGroup().toJson();
	}

}
