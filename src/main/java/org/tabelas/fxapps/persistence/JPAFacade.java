package org.tabelas.fxapps.persistence;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.eclipse.persistence.expressions.ExpressionBuilder;
import org.eclipse.persistence.jpa.JpaEntityManager;
import org.eclipse.persistence.jpa.JpaHelper;
import org.tabelas.fxapps.App;
import org.tabelas.fxapps.persistence.model.AbstractPojo;


public class JPAFacade implements IFacade, Serializable{
	
	private static final String PERSISTENCE_UNIT_NAME = "testPU";
	
	private static EntityManagerFactory emf = null;
	
	public JPAFacade(){
		emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME,App.getDatabaseProperties());
	}
	
	public static EntityManagerFactory getEntityManagerFactory(){
		if(emf == null){
			emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME,App.getDatabaseProperties());
		}
        return emf;
	}
	
	public static EntityManager getEntityManager(){
		EntityManagerFactory emf = getEntityManagerFactory();
		if(emf != null){
			try{
				return emf.createEntityManager();	
			}
			catch(Exception e){
				e.printStackTrace();
				return null;
			}
			
		}
		return null;
	}
	
	
	 /**
     * {@inheritDoc}
     */
    public <A extends AbstractPojo> A find(Class<A> clazz, Long id) {
        // Get the EntityManager and use its find() method to fetch the object.
        EntityManager em = getEntityManager();
        try {
            return em.find(clazz, id);
        } finally {
            // Once we've done the query, close the EntityManager
            ////em.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public <A extends AbstractPojo> List<A> list(Class<A> clazz) {
        EntityManager em = getEntityManager();
        try {
            // Initialize the query
            Query query = generateQuery(clazz, em);
            // Execute the query and return the result
            return query.getResultList();
        } finally {
            // Once we've done the query, close the EntityManager
            ////em.close();
        }
    }

    /**
     * This method creates a Query object from the given entity class.
     * 
     * @param entityClass
     *            The class of the entity for which we are creating the query
     * @param em
     *            EntityManager instance
     * @return An instance of the Query object for the given entity class
     */
    private <A extends AbstractPojo> Query generateQuery(Class<A> entityClass,
            EntityManager em) {
        // Use the ExpressionBuilder to create a query which fetches a list
        // of the given objects.
        ExpressionBuilder builder = new ExpressionBuilder();
        JpaEntityManager jpaEm = JpaHelper.getEntityManager(em);
        // Build the query
        Query query = jpaEm.createQuery(builder, entityClass);
        return query;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public <A extends AbstractPojo> List<A> list(String queryStr,Map<String, Object> parameters) {
        EntityManager em = getEntityManager();
        try {
            // Generate a query instance for the given query and parameters
            Query query = generateQuery(queryStr, parameters, em);
            // Execute query and return results
            return query.getResultList();
        } finally {
            // Once we've done the query, close the EntityManager
            ////em.close();
        }
    }

    /**
     * This method creates a Query object from the given query string and the
     * given parameters.
     * 
     * @param queryStr
     *            Database query string
     * @param parameters
     *            A map of parameters and parameter values used in the query
     * @param em
     *            EntityManager instance
     * @return An instance of the Query object for the given entity class
     */
    private Query generateQuery(String queryStr,
            Map<String, Object> parameters, EntityManager em) {
        // Create a query object from the query string given as the
        // parameter
        Query query = em.createQuery(queryStr);
        // Check if we have some parameters defined
        if (parameters != null) {
            for (Entry<String, Object> entry : parameters.entrySet()) {
                // Inject the parameter to the query
                query.setParameter(entry.getKey(), entry.getValue());
            }
        }
        return query;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public <A extends AbstractPojo> A find(String queryStr,Map<String, Object> parameters) {
        EntityManager em = getEntityManager();
        try {
            // Create a query object from the query string given as the
            // parameter
            Query query = em.createQuery(queryStr);
            // Check if we have some parameters defined
            if (parameters != null) {
                for (Entry<String, Object> entry : parameters.entrySet()) {
                    // Inject the parameter to the query
                    query.setParameter(entry.getKey(), entry.getValue());
                }
            }

            // Execute query and return result
            return (A) query.getSingleResult();
        } catch (NoResultException e) {
            // This exception will occur if no results were found with the given
            // query. If this occurs, return null.
            return null;
        } finally {
            // Once we've done the query, close the EntityManager
            ////em.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void store(AbstractPojo pojo) {
        EntityManager em = getEntityManager();
        try {
            // Check if we have an open transaction
            if (!em.getTransaction().isActive()) {
                // If not, open a new transaction
                em.getTransaction().begin();
            }
            // Check if the entity has an id (primary key). If it has a primary
            // key, then there is an existing instance of this object in the
            // database and we only need to update its state.
            if (pojo.getId() != null) {
                em.merge(pojo);
            } else {
                // An id didn't exist, so we have a new entity in our hands,
                // hence we need to persist it and not merge.
                em.persist(pojo);
            }
            // Commit the transaction
            em.getTransaction().commit();

            // The concurrency version id has now been incremented for pojo,
            // hence we need to refresh the pojo at this point to avoid
            // exceptions caused by the optimistic locking. This refresh won't
            // cause an extra database query, since the object is cached in the
            // JPA providers memory.
            
        } finally {
            // Once we've done the query, close the EntityManager
            ////em.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    public <A extends AbstractPojo> void storeAll(Collection<A> pojos) {
        // This method follows the same principles as the store() method. Read
        // store()'s comments for more detailed explanations.
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            // Loop through all entities
            for (AbstractPojo pojo : pojos) {
                // Merge or persist the objects depending on if they already
                // exist in the database.
                if (pojo.getId() != null) {
                    em.merge(pojo);
                } else {
                    em.persist(pojo);
                }
            }
            // Commit the transaction.
            em.getTransaction().commit();
            
        } finally {
            ////em.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void delete(AbstractPojo pojo) {
        if (pojo == null) {
            throw new IllegalArgumentException("Null values are not accepted");
        }

        // If it isn't stored, it can't be removed
        if (pojo.getId() == null) {
            return;
        }

        EntityManager em = getEntityManager();
        try {
            // Begin the transaction
            em.getTransaction().begin();
            // We need to merge this object to the database session, so that it
            // can be deleted. We do this by actually fetching the object first.
            Object entity = em.find(pojo.getClass(), pojo.getId());
            // Now when we have a fresh instance of the entity which is attached
            // to the JPA provider's session, we can remove the entity.
            em.remove(entity);
            // Commit transaction.
            em.getTransaction().commit();
        } finally {
            ////em.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    public <A extends AbstractPojo> void deleteAll(Collection<A> pojos) {
        if (pojos == null) {
            throw new IllegalArgumentException("Null values are not accepted");
        }
        // This method follows the same principles as the delete() method. Read
        // delete()'s comments for more detailed explanations.

        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            for (A pojo : pojos) {
                // If it isn't stored, it can't be removed
                if (pojo.getId() == null) {
                    continue;
                }
                // Fetch the entity merged to the session
                Object entity = em.find(pojo.getClass(), pojo.getId());
                if (entity != null) {
                    // Remove the entity
                    em.remove(entity);
                }
            }
            // Commit transaction
            em.getTransaction().commit();
        } finally {
            ////em.close();
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public <A extends AbstractPojo> List<A> list(Class<A> clazz,
            int startIndex, int amount) {
        EntityManager em = getEntityManager();
        try {
            // Initialize the query
            Query query = generateQuery(clazz, em);
            query.setFirstResult(startIndex).setMaxResults(amount);
            // Execute the query and return the result
            return query.getResultList();
        } finally {
            // Once we've done the query, close the EntityManager
            ////em.close();
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public <A extends AbstractPojo> List<A> list(String queryStr,
            Map<String, Object> parameters, int startIndex, int amount) {
        EntityManager em = getEntityManager();
        try {
            // Generate a query instance for the given query and parameters
            Query query = generateQuery(queryStr, parameters, em);

            // Set the result limit parameters
            query.setFirstResult(startIndex).setMaxResults(amount);

            // Execute query and return results
            return query.getResultList();
        } finally {
            // Once we've done the query, close the EntityManager
            ////em.close();
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public Long count(Class<? extends AbstractPojo> c) {
        if (c == null) {
            throw new IllegalArgumentException("Class may not be null");
        }

        EntityManager em = getEntityManager();
        try {
            String queryStr = "SELECT COUNT(p.id) FROM " + c.getSimpleName()
                    + " p";
            // Create a query object from the query string given as the
            // parameter
            Query query = em.createQuery(queryStr);

            // Execute query and return result
            return (Long) query.getSingleResult();
        } catch (NoResultException e) {
            // This exception will occur if no results were found with the given
            // query. If this occurs, return null.
            return -1L;
        } finally {
            // Once we've done the query, close the EntityManager
            ////em.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    public Long count(Class<? extends AbstractPojo> c, String whereClause,
            Map<String, Object> parameters) {
        if (c == null) {
            throw new IllegalArgumentException("Class may not be null");
        }

        if (whereClause == null) {
            throw new IllegalArgumentException("Where clause may not be null");
        }

        EntityManager em = getEntityManager();
        try {
            String queryStr = "SELECT COUNT(p.id) FROM " + c.getSimpleName()
                    + " p WHERE " + whereClause;

            Query query = generateQuery(queryStr, parameters, em);
            // Execute query and return result
            return (Long) query.getSingleResult();
        } catch (NoResultException e) {
            // This exception will occur if no results were found with the given
            // query. If this occurs, return null.
            return -1L;
        } finally {
            // Once we've done the query, close the EntityManager
            ////em.close();
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public List<?> getFieldValues(Class<? extends AbstractPojo> c,
            String field, String whereConditions, Map<String, Object> parameters) {

        String queryStr = createSelectFieldQuery(c, field, whereConditions);
        EntityManager em = getEntityManager();
        Query query = generateQuery(queryStr, parameters, em);

        try {
            // Execute query and return results
            return query.getResultList();
        } finally {
            // Once we've done the query, close the EntityManager
            ////em.close();
        }
    }
    
    /**
     * Creates the query for selecting a specific field's value from entities.
     * 
     * @param c
     * @param field
     * @param whereConditions
     * @return
     */
    private String createSelectFieldQuery(Class<? extends AbstractPojo> c,
            String field, String whereConditions) {
        String queryStr = "SELECT p." + field + " FROM " + c.getSimpleName()
                + " p";

        if (whereConditions != null) {
            queryStr += " WHERE " + whereConditions;
        }
        return queryStr;
    }

	@Override
	public Long count(String queryStr, Map<String, Object> parameters) {
		// TODO Auto-generated method stub
		EntityManager em = getEntityManager();
        Query query = generateQuery(queryStr, parameters, em);

        try {
            // Execute query and return results
            List list = query.getResultList();
            if(list.size() == 0)
            	return 0L;
            else
            	return (Long)list.get(0);
        } finally {
            // Once we've done the query, close the EntityManager
            ////em.close();
        }

	}

	
}
