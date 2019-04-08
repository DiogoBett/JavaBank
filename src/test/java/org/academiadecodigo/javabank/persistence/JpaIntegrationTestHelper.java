package org.academiadecodigo.javabank.persistence;

import org.junit.After;
import org.junit.Before;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JpaIntegrationTestHelper {

    protected static EntityManagerFactory emf;
    protected static EntityManager em;

    @Before
    public void init() {
        emf = Persistence.createEntityManagerFactory("test");
        em = emf.createEntityManager();
    }

    @After
    public void tearDown() {
        if (em != null) {
            em.clear();
            em.close();
        }

        if (emf != null) {
            emf.close();
        }
    }
}
