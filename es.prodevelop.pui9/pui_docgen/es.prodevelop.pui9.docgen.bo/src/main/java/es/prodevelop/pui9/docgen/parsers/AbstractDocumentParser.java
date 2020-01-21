package es.prodevelop.pui9.docgen.parsers;

import java.lang.reflect.Field;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.util.StringUtils;

import es.prodevelop.pui9.docgen.dto.MappingDto;
import es.prodevelop.pui9.docgen.enums.MappingOriginEnum;
import es.prodevelop.pui9.docgen.pdf.converters.IPdfConverter;
import es.prodevelop.pui9.docgen.pdf.converters.PdfConverterRegistry;
import es.prodevelop.pui9.model.dto.DtoRegistry;
import es.prodevelop.pui9.model.dto.interfaces.IDto;
import es.prodevelop.pui9.utils.PuiDateUtil;

/**
 * This is an Abstract Document Parser with useful methods. All the parsers may
 * inherit this class
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public abstract class AbstractDocumentParser implements IDocumentParser {

	protected DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/Mm/dd HH:mm");

	/**
	 * Get the PDF converter for this document parser
	 * 
	 * @return The PDF converter
	 */
	protected IPdfConverter getPdfConverter() {
		return PdfConverterRegistry.getSingleton().getPdfConverter(getFileExtension());
	}

	/**
	 * This method analyzes the list of DTO looking for details, and returns a
	 * modified list of elements with the list of details that belongs to each DTO
	 */
	protected List<DtoDetailsElement> parseDtos(List<IDto> list, List<String> pkFieldNames,
			boolean documentWithDetails) {
		List<DtoDetailsElement> parsedList = new ArrayList<>();

		if (!documentWithDetails) {
			for (IDto dto : list) {
				parsedList.add(new DtoDetailsElement(dto));
			}
		} else {
			if (!StringUtils.isEmpty(pkFieldNames)) {
				Class<? extends IDto> dtoClass = list.get(0).getClass();
				List<Field> pkFields = new ArrayList<>();
				for (String pkFieldName : pkFieldNames) {
					Field pkField = DtoRegistry.getJavaFieldFromFieldName(dtoClass, pkFieldName);
					pkFields.add(pkField);
				}

				// map pk hash to index in list
				Map<Integer, Integer> parents = new HashMap<>();
				for (IDto dto : list) {
					HashCodeBuilder hcb = new HashCodeBuilder();
					for (Field pkField : pkFields) {
						try {
							Object value = FieldUtils.readField(pkField, dto, true);
							hcb.append(value);
						} catch (IllegalAccessException e) {
							// do nothing
						}
					}
					int hash = hcb.toHashCode();
					if (!parents.containsKey(hash)) {
						parents.put(hash, parsedList.size());
						parsedList.add(new DtoDetailsElement(dto));
					}
					parsedList.get(parents.get(hash)).getDetails().add(dto);
				}
			}
		}

		return parsedList;
	}

	/**
	 * This method return the value that will be applied to the given mapping and
	 * DTO
	 */
	protected String getValue(MappingDto mapping, IDto dto) {
		String value = "";
		if (mapping.getOrigin().equals(MappingOriginEnum.VIEW.getCode())) {
			Field field = DtoRegistry.getJavaFieldFromColumnName(dto.getClass(), mapping.getField());
			if (field == null) {
				field = DtoRegistry.getJavaFieldFromFieldName(dto.getClass(), mapping.getField());
			}
			try {
				Object val = FieldUtils.readField(field, dto, true);
				if (val instanceof Instant) {
					// use the formatter, and remove the " character
					value = PuiDateUtil.temporalAccessorToString((Instant) val, formatter);
				} else {
					value = val.toString();
				}
			} catch (Exception e) {
				// do nothing
			}
		} else {
			value = mapping.getField();
		}

		return value;
	}

	/**
	 * This is a supporting class for representing a Master-Detail in the list of
	 * elements that are involved in the template generation
	 * 
	 * @author Marc Gil - mgil@prodevelop.es
	 */
	protected class DtoDetailsElement {
		private IDto dto;
		private List<IDto> details;

		// without details
		private DtoDetailsElement(IDto dto) {
			this(dto, null);
		}

		// with details
		private DtoDetailsElement(IDto dto, List<IDto> details) {
			this.dto = dto;
			this.details = details != null ? details : new ArrayList<>();
		}

		public IDto getDto() {
			return dto;
		}

		public List<IDto> getDetails() {
			return details;
		}
	}

}
