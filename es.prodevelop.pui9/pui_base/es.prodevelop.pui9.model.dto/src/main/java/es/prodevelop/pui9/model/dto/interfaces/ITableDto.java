package es.prodevelop.pui9.model.dto.interfaces;

/**
 * More specific {@link IDto} representation for all the Tables of the Database
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public interface ITableDto extends IDto {

	/**
	 * Returns the PK object of this DTO
	 * 
	 * @return The PK of the {@link ITableDto}
	 */
	<TPK extends ITableDto> TPK createPk();
}
