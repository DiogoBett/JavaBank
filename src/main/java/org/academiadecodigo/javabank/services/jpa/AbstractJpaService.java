package org.academiadecodigo.javabank.services.jpa;

import org.academiadecodigo.javabank.model.AbstractModel;
import org.academiadecodigo.javabank.services.CRUDService;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.RollbackException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * A generic jpa service to be used as a base for concrete jpa service implementations
 * @see CRUDService
 * @param <T> the model type
 */
public abstract class AbstractJpaService<T extends AbstractModel> implements CRUDService<T> {

    protected EntityManagerFactory emf;
    private Class<T> modelType;

    /**
     * Initializes a new {@code JPA Service} instance given an entity manager factory and a model type
     *
     * @param emf the entity manager factory
     * @param modelType the model type
     */
    public AbstractJpaService(EntityManagerFactory emf, Class<T> modelType) {
        this.emf = emf;
        this.modelType = modelType;
    }

    /**
     * @see CRUDService#list()
     */
    @Override
    public List<T> list() {

        EntityManager em = emf.createEntityManager();

        try {

            CriteriaQuery<T> criteriaQuery = em.getCriteriaBuilder().createQuery(modelType);
            Root<T> root = criteriaQuery.from(modelType);
            return em.createQuery(criteriaQuery).getResultList();

        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /**
     * @see CRUDService#get(Integer)
     */
    @Override
    public T get(Integer id) {

        EntityManager em = emf.createEntityManager();

        try {

            return em.find(modelType, id);

        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /**
     * @see CRUDService#save(AbstractModel)
     */
    @Override
    public T save(T modelObject) {

        EntityManager em = emf.createEntityManager();

        try {

            em.getTransaction().begin();
            T savedObject = em.merge(modelObject);
            em.getTransaction().commit();

            return savedObject;

        } catch (RollbackException ex) {

            em.getTransaction().rollback();
            return null;

        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /**
     * @see CRUDService#delete(Integer)
     */
    @Override
    public void delete(Integer id) {

        EntityManager em = emf.createEntityManager();

        try {

            em.getTransaction().begin();
            em.remove(em.find(modelType, id));
            em.getTransaction().commit();

        } catch (RollbackException ex) {

            em.getTransaction().rollback();

        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
}
