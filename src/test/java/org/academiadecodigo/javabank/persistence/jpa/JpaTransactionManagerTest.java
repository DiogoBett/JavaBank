package org.academiadecodigo.javabank.persistence.jpa;

import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import static org.mockito.Mockito.*;

public class JpaTransactionManagerTest {

    private JpaSessionManager sm;
    private JpaTransactionManager tx;
    private EntityManager em;
    private EntityTransaction transaction;

    @Before
    public void setup() {

        transaction = mock(EntityTransaction.class);
        sm = mock(JpaSessionManager.class);
        em = mock(EntityManager.class);
        tx = new JpaTransactionManager(sm);

        when(sm.getCurrentSession()).thenReturn(em);
        when(em.getTransaction()).thenReturn(transaction);

    }

    @Test
    public void testBeginRead() {

        // exercise
        tx.beginRead();

        // verify
        verify(sm, times(1)).startSession();

    }

    @Test
    public void testBeginWrite() {

        // exercise
        tx.beginWrite();

        // verify
        verify(transaction, times(1)).begin();

    }

    @Test
    public void testCommit() {

        // setup
        when(transaction.isActive()).thenReturn(true);

        // exercise
        tx.commit();

        // verify
        verify(transaction, times(1)).commit();
        verify(sm, times(1)).stopSession();
    }

    @Test
    public void testCommitNoTransaction() {

        // setup
        when(transaction.isActive()).thenReturn(false);

        // exercise
        tx.commit();

        // verify
        verify(transaction, never()).commit();
        verify(sm, times(1)).stopSession();
    }

    @Test
    public void testRollback() {

        // setup
        when(transaction.isActive()).thenReturn(true);

        // exercise
        tx.rollback();

        // verify
        verify(transaction, times(1)).rollback();
        verify(sm, times(1)).stopSession();
    }

    @Test
    public void testRollbackNoTransaction() {

        // setup
        when(transaction.isActive()).thenReturn(false);

        // exercise
        tx.rollback();

        // verify
        verify(transaction, never()).rollback();
        verify(sm, times(1)).stopSession();
    }

}
