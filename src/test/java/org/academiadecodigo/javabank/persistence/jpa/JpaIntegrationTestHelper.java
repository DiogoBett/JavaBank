package org.academiadecodigo.javabank.persistence.jpa;

import org.junit.After;
import org.junit.Before;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JpaIntegrationTestHelper {

    protected static EntityManagerFactory emf;
    protected static JpaSessionManager sm;
    protected static JpaTransactionManager tx;

    @Before
    public void init() {
        emf = Persistence.createEntityManagerFactory("test");
        sm = new JpaSessionManager(emf);
        tx = new JpaTransactionManager(sm);

        tx.beginRead();
    }

    @After
    public void tearDown() {

        if (sm.getCurrentSession().getTransaction().getRollbackOnly()) {
            tx.rollback();
        } else {
            tx.commit();
        }

        if (emf != null) {
            emf.close();
        }
    }
}
