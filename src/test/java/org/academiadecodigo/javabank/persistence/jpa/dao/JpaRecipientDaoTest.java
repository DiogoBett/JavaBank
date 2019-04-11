package org.academiadecodigo.javabank.persistence.jpa.dao;

import org.academiadecodigo.javabank.model.Recipient;
import org.academiadecodigo.javabank.persistence.TransactionException;
import org.academiadecodigo.javabank.persistence.dao.jpa.JpaRecipientDao;
import org.academiadecodigo.javabank.persistence.jpa.JpaSessionManager;
import org.hibernate.HibernateException;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class JpaRecipientDaoTest {

    private JpaSessionManager sm;
    private JpaRecipientDao recipientDao;
    private EntityManager em;

    @Before
    public void setup() {
        sm = mock(JpaSessionManager.class);
        em = mock(EntityManager.class);
        recipientDao = new JpaRecipientDao(sm);

        when(sm.getCurrentSession()).thenReturn(em);
    }

    @Test
    public void testFindAll() {

        // setup
        List<Recipient> mockRecipients = new ArrayList<>();
        CriteriaQuery criteriaQuery = mock(CriteriaQuery.class);
        CriteriaBuilder criteriaBuilder = mock(CriteriaBuilder.class);
        TypedQuery typedQuery = mock(TypedQuery.class);
        when(em.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(Recipient.class)).thenReturn(criteriaQuery);
        when(em.createQuery(criteriaQuery)).thenReturn(typedQuery);
        when(em.createQuery(any(CriteriaQuery.class))).thenReturn(typedQuery);
        when(em.createQuery(anyString(), any(Class.class))).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(mockRecipients);

        // exercise
        List<Recipient> recipients = recipientDao.findAll();

        // verify
        verify(sm, times(1)).getCurrentSession();
        verify(sm, never()).stopSession();
        verify(sm, never()).startSession();
        verify(typedQuery, times(1)).getResultList();
        assertEquals(mockRecipients, recipients);
    }

    @Test(expected = TransactionException.class)
    public void testFindAllFail() {

        // setup
        List<Recipient> mockRecipients = new ArrayList<>();
        CriteriaQuery criteriaQuery = mock(CriteriaQuery.class);
        CriteriaBuilder criteriaBuilder = mock(CriteriaBuilder.class);
        TypedQuery typedQuery = mock(TypedQuery.class);
        when(em.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(Recipient.class)).thenReturn(criteriaQuery);
        when(em.createQuery(criteriaQuery)).thenReturn(typedQuery);
        when(em.createQuery(anyString(), any(Class.class))).thenReturn(typedQuery);
        when(em.createQuery(any(CriteriaQuery.class))).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(mockRecipients);
        doThrow(new HibernateException(new RuntimeException())).when(typedQuery).getResultList();

        // exercise
        recipientDao.findAll();

        // verify
        verify(sm, times(1)).getCurrentSession();
        verify(sm, never()).stopSession();
        verify(sm, never()).startSession();
        verify(typedQuery.getResultList(), times(1));

    }

    @Test
    public void testFindById() {

        // setup
        int fakeId = 9999;
        Recipient fakeRecipient = new Recipient();
        fakeRecipient.setId(fakeId);
        when(em.find(Recipient.class, fakeId)).thenReturn(fakeRecipient);

        // exercise
        Recipient recipient = recipientDao.findById(fakeId);

        // verify
        verify(sm, times(1)).getCurrentSession();
        verify(sm, never()).stopSession();
        verify(sm, never()).startSession();
        verify(em, times(1)).find(Recipient.class, fakeId);
        assertEquals(fakeRecipient, recipient);

    }

    @Test(expected = TransactionException.class)
    public void testFindByIdFails() {

        // setup
        int fakeId = 9999;
        doThrow(new HibernateException(new RuntimeException())).when(em).find(eq(Recipient.class), anyInt());

        // exercise
        recipientDao.findById(fakeId);

        // verify
        verify(sm, times(1)).getCurrentSession();
        verify(sm, never()).stopSession();
        verify(sm, never()).startSession();
        verify(em, times(1)).find(Recipient.class, fakeId);

    }

    @Test
    public void testSaveOrUpdate() {

        // setup
        Recipient fakeRecipient = new Recipient();
        when(em.merge(any(Recipient.class))).thenReturn(fakeRecipient);

        // exercise
        Recipient recipient = recipientDao.saveOrUpdate(fakeRecipient);

        // verify
        verify(sm, times(1)).getCurrentSession();
        verify(sm, never()).stopSession();
        verify(sm, never()).startSession();
        verify(em, times(1)).merge(any(Recipient.class));
        assertEquals(fakeRecipient, recipient);

    }

    @Test(expected = TransactionException.class)
    public void testSaveOrUpdateFail() {

        // setup
        Recipient fakeRecipient = new Recipient();
        doThrow(new HibernateException(new RuntimeException())).when(em).merge(any(Recipient.class));

        // exercise
        recipientDao.saveOrUpdate(fakeRecipient);

        // verify
        verify(sm, times(1)).getCurrentSession();
        verify(sm, never()).stopSession();
        verify(sm, never()).startSession();
        verify(em, times(1)).merge(any(Recipient.class));
    }

    @Test
    public void testDelete() {

        // setup
        int fakeId = 9999;
        Recipient fakeRecipient = new Recipient();
        fakeRecipient.setId(fakeId);
        when(em.find(Recipient.class, fakeId)).thenReturn(fakeRecipient);

        // exercise
        recipientDao.delete(fakeId);

        // verify
        verify(sm, times(1)).getCurrentSession();
        verify(sm, never()).stopSession();
        verify(sm, never()).startSession();
        verify(em, times(1)).remove(fakeRecipient);

    }

    @Test(expected = TransactionException.class)
    public void testDeleteFail() {

        // setup
        int fakeId = 9999;
        doThrow(new HibernateException(new RuntimeException())).when(em).find(eq(Recipient.class), anyInt());

        // exercise
        recipientDao.delete(fakeId);

        // verify
        verify(sm, times(1)).getCurrentSession();
        verify(sm, never()).stopSession();
        verify(sm, never()).startSession();
        verify(em, times(1)).remove(any(Recipient.class));

    }
}
