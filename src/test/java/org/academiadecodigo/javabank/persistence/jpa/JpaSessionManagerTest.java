package org.academiadecodigo.javabank.persistence.jpa;

import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class JpaSessionManagerTest {

    private JpaSessionManager sm;
    private EntityManagerFactory emf;
    private EntityManager em;

    @Before
    public void setup() {

        emf = mock(EntityManagerFactory.class);
        em = mock(EntityManager.class);
        sm = new JpaSessionManager();
        sm.setEmf(emf);

        when(emf.createEntityManager()).thenReturn(em);

    }

    @Test
    public void testStartSession() {

        // exercise
        sm.startSession();
        sm.startSession();

        // verify
        verify(emf, times(1)).createEntityManager();

    }

    @Test
    public void testStopSession() {

        // setup
        sm.startSession();

        // exercise
        sm.stopSession();
        sm.stopSession();

        // verify
        verify(em, times(1)).close();
    }

    @Test
    public void testStopSessionNoStart() {

        // exercise
        sm.stopSession();

        // verify
        verify(em, never()).close();

    }

    @Test
    public void testGetCurrentSession() {

        // setup
        sm.startSession();

        // exercise
        EntityManager currentEm = sm.getCurrentSession();

        // verify
        verify(emf, times(1)).createEntityManager();
        assertEquals(em, currentEm);

    }

    @Test
    public void testGetCurrentSessionNoPreviousSession() {

        // exercise
        EntityManager currentEm = sm.getCurrentSession();

        // verify
        verify(emf, times(1)).createEntityManager();
        assertEquals(em, currentEm);
    }
}
