package org.academiadecodigo.javabank.persistence.dao.jpa;

import org.academiadecodigo.javabank.model.Customer;
import org.academiadecodigo.javabank.persistence.TransactionException;
import org.academiadecodigo.javabank.persistence.dao.CustomerDao;
import org.academiadecodigo.javabank.persistence.jpa.JpaSessionManager;
import org.hibernate.HibernateException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import java.util.List;

/**
 * A JPA {@link CustomerDao} implementation
 */
public class JpaCustomerDao extends GenericJpaDao<Customer> implements CustomerDao {

    /**
     * @see GenericJpaDao#GenericJpaDao(JpaSessionManager, Class)
     */
    public JpaCustomerDao(JpaSessionManager sm) {
        super(sm, Customer.class);
    }

    /**
     * @see CustomerDao#getCustomerIds()
     */
    public List<Integer> getCustomerIds() {
        try {

            EntityManager em = sm.getCurrentSession();
            return em.createQuery("select id from Customer", Integer.class)
                    .getResultList();

        } catch (PersistenceException ex) {
            throw new TransactionException(ex);
        }
    }
}
