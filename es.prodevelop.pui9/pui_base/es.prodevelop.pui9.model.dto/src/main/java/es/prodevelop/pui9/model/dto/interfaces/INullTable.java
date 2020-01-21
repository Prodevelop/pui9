package es.prodevelop.pui9.model.dto.interfaces;

/**
 * Interface representation for a Null Table. Used by Services and Controllers
 * that don't work againt any concrete Table. This can be used in the situation
 * that you have a model that is readonly, and the only you want to do with it
 * is to list the registries, but never try to edit them
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public interface INullTable extends INullTablePk {

}
