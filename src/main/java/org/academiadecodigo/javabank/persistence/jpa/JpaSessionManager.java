package org.academiadecodigo.javabank.persistence.jpa;

import org.academiadecodigo.javabank.persistence.SessionManager;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * A JPA {@link SessionManager} implementation
 */
public class JpaSessionManager implements SessionManager<EntityManager> {

    private EntityManagerFactory emf;
    private EntityManager em;

    /**
     * Initializes a new {@code JPA Session Manager} instance given an entity manager factory
     *
     * @param emf the entity manager factory
     */
    public JpaSessionManager(EntityManagerFactory emf) {
        this.emf = emf;
    }

    /**
     * @see SessionManager#startSession()
     */
    @Override
    public void startSession() {

        if (em == null) {
            em = emf.createEntityManager();
        }
    }

    /**
     * @see SessionManager#stopSession()
     */
    @Override
    public void stopSession() {

        if (em != null) {
            em.close();
        }

        em = null;
    }

    /**
     * @see SessionManager#getCurrentSession()
     */
    @Override
    public EntityManager getCurrentSession() {
        startSession();
        return em;
    }
}
