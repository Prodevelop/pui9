package es.prodevelop.pui9.model.configuration;

import es.prodevelop.pui9.utils.IPuiObject;

public class FilterCombo implements IPuiObject {

	private static final long serialVersionUID = 1L;

	private String id;
	private FilterComboSearch search;
	private FilterComboLocal local;
	private FilterComboRelatedCombo relatedCombo;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public FilterComboSearch getSearch() {
		return search;
	}

	public void setSearch(FilterComboSearch search) {
		this.search = search;
	}

	public FilterComboLocal getLocal() {
		return local;
	}

	public void setLocal(FilterComboLocal local) {
		this.local = local;
	}

	public FilterComboRelatedCombo getRelatedCombo() {
		return relatedCombo;
	}

	public void setRelatedCombo(FilterComboRelatedCombo relatedCombo) {
		this.relatedCombo = relatedCombo;
	}

}
