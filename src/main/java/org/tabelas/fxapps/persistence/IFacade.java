package org.tabelas.fxapps.persistence;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.tabelas.fxapps.persistence.model.AbstractPojo;


public interface IFacade {
	
	/**
     * Fetch a specific entity object from the database
     * 
     * @param clazz
     *            The class of the entity
     * @param id
     *            The primary key of the entity object to be fetched
     */
    public <A extends AbstractPojo> A find(Class<A> clazz, Long id);
    
    /**
     * Fetches all entities in the database of the given entity type
     * 
     * @param clazz
     *            Entity class
     * @return List of A entities
     */
    public <A extends AbstractPojo> List<A> list(Class<A> clazz);
    
    /**
     * Fetches all entities in the database of the given entity type
     * 
     * @param clazz
     *            Entity class
     * @param startIndex
     *            Index from which we should start selecting entities
     * @param amount
     *            The maximum amount of entities returned
     * @return List of A entities
     */
    public <A extends AbstractPojo> List<A> list(Class<A> clazz,
            int startIndex, int amount);
    
    /**
     * Fetches all entities in the database for the given query
     * 
     * @param queryStr
     *            Database query string
     * @param parameters
     *            A map of parameters and parameter values used in the query
     * @return List of A entities
     */
    public <A extends AbstractPojo> List<A> list(String queryStr,
            Map<String, Object> parameters);
    
    
    /**
     * Fetches all entities in the database for the given query
     * 
     * @param queryStr
     *            Database query string
     * @param parameters
     *            A map of parameters and parameter values used in the query
     * @param startIndex
     *            Index from which we should start selecting entities
     * @param amount
     *            The maximum amount of entities returned
     * @return List of A entities
     */
    public <A extends AbstractPojo> List<A> list(String queryStr,
            Map<String, Object> parameters, int startIndex, int amount);

    /**
     * Fetch a specific entity object from the database for the given query
     * 
     * @param queryStr
     *            Database query string
     * @param parameters
     *            A map of parameters and parameter values used in the query
     * @return An instance of A
     */
    public <A extends AbstractPojo> A find(String queryStr,
            Map<String, Object> parameters);

    /**
     * Store an entity to the database
     * 
     * @param pojo
     *            An instance of the entity to be stored
     */
    public void store(AbstractPojo pojo);

    /**
     * Store a set of entities within the same transaction
     * 
     * @param pojos
     *            Set of entities which are to be stored as a batch within the
     *            same transaction
     */
    public <A extends AbstractPojo> void storeAll(Collection<A> pojos);

    /**
     * Remove an entity from the database
     * 
     * @param pojo
     *            The entity to be removed
     */
    public void delete(AbstractPojo pojo);

    /**
     * Remove a set of entites from the database within the same transaction
     * 
     * @param pojos
     *            Set of entities which are to be removed as a batch within the
     *            same transaction
     */
    public <A extends AbstractPojo> void deleteAll(Collection<A> pojos);
    
    /**
     * Gives the total number of records for the given entity type.
     * 
     * @param c
     *            Class object of the entity
     * @return Total number of records or -1 if an exception occurred.
     */
    public Long count(Class<? extends AbstractPojo> c);

    /**
     * Gives the total number of records for the given entity type which
     * fulfills the where clause.
     * 
     * @param c
     *            Class object of the entity
     * @param whereClause
     *            The query's WHERE clause with desired restraints.
     * @param parameters
     *            A map of parameters and parameter values used in the query
     * @return Total number of records or -1 if an exception occurred.
     */
    public Long count(Class<? extends AbstractPojo> c, String whereClause,
            Map<String, Object> parameters);

    /**
     * Fetches the values for a specific field for all entities which match the
     * set criteria.
     * 
     * @param c
     *            Entity class
     * @param field
     *            Field whose value is returned
     * @param whereConditions
     *            Where conditions
     * @param parameters
     *            Parameter values for the where conditions
     * @return A list a field values
     */
    public List<?> getFieldValues(Class<? extends AbstractPojo> c,
            String field, String whereConditions, Map<String, Object> parameters);
    
    public Long count(String queryStr, Map<String, Object> parameters);

}
