package org.academiadecodigo.javabank.persistence.jpa;

import org.academiadecodigo.javabank.Config;
import org.junit.After;
import org.junit.Before;
import org.springframework.context.support.GenericXmlApplicationContext;

import javax.persistence.EntityManagerFactory;

public class JpaIntegrationTestHelper {

    protected EntityManagerFactory emf;
    protected JpaSessionManager sm;
    protected JpaTransactionManager tx;
    GenericXmlApplicationContext ctx;

    @Before
    public void init() {

        ctx = new GenericXmlApplicationContext();
        ctx.getEnvironment().setActiveProfiles("test");
        ctx.load(Config.SPRING_CONFIG);
        ctx.refresh();

        emf = ctx.getBean(EntityManagerFactory.class);

        sm = new JpaSessionManager();
        tx = new JpaTransactionManager();

        sm.setEmf(emf);
        tx.setSm(sm);

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

        ctx.destroy();
    }
}
