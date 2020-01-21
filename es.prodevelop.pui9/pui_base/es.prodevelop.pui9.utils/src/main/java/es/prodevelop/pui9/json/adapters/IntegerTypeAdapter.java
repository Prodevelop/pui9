package es.prodevelop.pui9.json.adapters;

/**
 * Type adapter for Integer type to be used with GSON
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class IntegerTypeAdapter extends AbstractNumberTypeAdapter<Integer> {

	@Override
	protected Integer parse(String result) {
		return Integer.parseInt(result);
	}

}
