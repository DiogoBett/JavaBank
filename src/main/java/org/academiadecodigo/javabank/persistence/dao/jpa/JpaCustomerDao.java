package org.academiadecodigo.javabank.persistence.dao.jpa;

import org.academiadecodigo.javabank.persistence.model.Customer;
import org.academiadecodigo.javabank.persistence.dao.CustomerDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * A JPA {@link CustomerDao} implementation
 */
@Repository
public class JpaCustomerDao extends GenericJpaDao<Customer> implements CustomerDao {

    /**
     * @see GenericJpaDao#GenericJpaDao(Class)
     */
    public JpaCustomerDao() {
        super(Customer.class);
    }

    /**
     * @see CustomerDao#getCustomerIds()
     */
    public List<Integer> getCustomerIds() {
        return em.createQuery("select id from Customer", Integer.class)
                .getResultList();
    }
}
