package es.prodevelop.pui9.json.adapters;

import java.math.BigDecimal;

/**
 * Type adapter for BigDecimal type to be used with GSON
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class BigDecimalTypeAdapter extends AbstractNumberTypeAdapter<BigDecimal> {

	@Override
	protected BigDecimal parse(String result) {
		result = result.replace(',', '.');
		return new BigDecimal(new BigDecimal(result).stripTrailingZeros().toPlainString());
	}
}
