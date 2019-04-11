package org.academiadecodigo.javabank.persistence.jpa.dao;

import org.academiadecodigo.javabank.model.Customer;
import org.academiadecodigo.javabank.persistence.TransactionException;
import org.academiadecodigo.javabank.persistence.dao.jpa.JpaCustomerDao;
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
import static org.mockito.Mockito.*;

public class JpaCustomerDaoTest {

    private JpaSessionManager sm;
    private JpaCustomerDao customerDao;
    private EntityManager em;

    @Before
    public void setup() {

        sm = mock(JpaSessionManager.class);
        em = mock(EntityManager.class);
        customerDao = new JpaCustomerDao(sm);

        when(sm.getCurrentSession()).thenReturn(em);

    }

    @Test
    public void testFindAll() {

        // setup
        List<Customer> mockCustomers = new ArrayList<>();
        CriteriaQuery criteriaQuery = mock(CriteriaQuery.class);
        CriteriaBuilder criteriaBuilder = mock(CriteriaBuilder.class);
        TypedQuery typedQuery = mock(TypedQuery.class);
        when(em.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(Customer.class)).thenReturn(criteriaQuery);
        when(em.createQuery(criteriaQuery)).thenReturn(typedQuery);
        when(em.createQuery(any(CriteriaQuery.class))).thenReturn(typedQuery);
        when(em.createQuery(anyString(), any(Class.class))).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(mockCustomers);

        // exercise
        List<Customer> customers = customerDao.findAll();

        // verify
        verify(sm, times(1)).getCurrentSession();
        verify(sm, never()).stopSession();
        verify(sm, never()).startSession();
        verify(typedQuery, times(1)).getResultList();
        assertEquals(mockCustomers, customers);
    }

    @Test(expected = TransactionException.class)
    public void testFindAllFail() {

        // setup
        List<Customer> mockCustomers = new ArrayList<>();
        CriteriaQuery criteriaQuery = mock(CriteriaQuery.class);
        CriteriaBuilder criteriaBuilder = mock(CriteriaBuilder.class);
        TypedQuery typedQuery = mock(TypedQuery.class);
        when(em.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(Customer.class)).thenReturn(criteriaQuery);
        when(em.createQuery(criteriaQuery)).thenReturn(typedQuery);
        when(em.createQuery(anyString(), any(Class.class))).thenReturn(typedQuery);
        when(em.createQuery(any(CriteriaQuery.class))).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(mockCustomers);
        doThrow(new HibernateException(new RuntimeException())).when(typedQuery).getResultList();

        // exercise
        customerDao.findAll();

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
        Customer fakeCustomer = new Customer();
        fakeCustomer.setId(fakeId);
        when(em.find(Customer.class, fakeId)).thenReturn(fakeCustomer);

        // exercise
        Customer customer = customerDao.findById(fakeId);

        // verify
        verify(sm, times(1)).getCurrentSession();
        verify(sm, never()).stopSession();
        verify(sm, never()).startSession();
        verify(em, times(1)).find(Customer.class, fakeId);
        assertEquals(fakeCustomer, customer);

    }

    @Test(expected = TransactionException.class)
    public void testFindByIdFails() {

        // setup
        int fakeId = 9999;
        doThrow(new HibernateException(new RuntimeException())).when(em).find(eq(Customer.class), anyInt());

        // exercise
        customerDao.findById(fakeId);

        // verify
        verify(sm, times(1)).getCurrentSession();
        verify(sm, never()).stopSession();
        verify(sm, never()).startSession();
        verify(em, times(1)).find(Customer.class, fakeId);

    }

    @Test
    public void testSaveOrUpdate() {

        // setup
        Customer fakeCustomer = new Customer();
        when(em.merge(any(Customer.class))).thenReturn(fakeCustomer);

        // exercise
        Customer customer = customerDao.saveOrUpdate(fakeCustomer);

        // verify
        verify(sm, times(1)).getCurrentSession();
        verify(sm, never()).stopSession();
        verify(sm, never()).startSession();
        verify(em, times(1)).merge(any(Customer.class));
        assertEquals(fakeCustomer, customer);

    }

    @Test(expected = TransactionException.class)
    public void testSaveOrUpdateFail() {

        // setup
        Customer fakeCustomer = new Customer();
        doThrow(new HibernateException(new RuntimeException())).when(em).merge(any(Customer.class));

        // exercise
        customerDao.saveOrUpdate(fakeCustomer);

        // verify
        verify(sm, times(1)).getCurrentSession();
        verify(sm, never()).stopSession();
        verify(sm, never()).startSession();
        verify(em, times(1)).merge(any(Customer.class));
    }

    @Test
    public void testDelete() {

        // setup
        int fakeId = 9999;
        Customer fakeCustomer = new Customer();
        fakeCustomer.setId(fakeId);
        when(em.find(Customer.class, fakeId)).thenReturn(fakeCustomer);

        // exercise
        customerDao.delete(fakeId);

        // verify
        verify(sm, times(1)).getCurrentSession();
        verify(sm, never()).stopSession();
        verify(sm, never()).startSession();
        verify(em, times(1)).remove(fakeCustomer);

    }

    @Test(expected = TransactionException.class)
    public void testDeleteFail() {

        // setup
        int fakeId = 9999;
        doThrow(new HibernateException(new RuntimeException())).when(em).find(eq(Customer.class), anyInt());

        // exercise
        customerDao.delete(fakeId);

        // verify
        verify(sm, times(1)).getCurrentSession();
        verify(sm, never()).stopSession();
        verify(sm, never()).startSession();
        verify(em, times(1)).remove(any(Customer.class));

    }

    @Test
    public void testGetCustomerIds() {

        // setup
        List<Integer> fakeCustomerIds = new ArrayList<>();
        TypedQuery typedQuery = mock(TypedQuery.class);
        when(em.createQuery(anyString(), eq(Integer.class))).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(fakeCustomerIds);

        // exercise
        List<Integer> customerIds = customerDao.getCustomerIds();

        // verify
        verify(sm, times(1)).getCurrentSession();
        verify(sm, never()).stopSession();
        verify(sm, never()).startSession();
        verify(em, times(1)).createQuery("select id from Customer", Integer.class);
        verify(typedQuery, times(1)).getResultList();
        assertEquals(fakeCustomerIds, customerIds);

    }

    @Test(expected = TransactionException.class)
    public void testGetCustomerIdsFail() {

        // setup
        List<Integer> fakeCustomerIds = new ArrayList<>();
        TypedQuery typedQuery = mock(TypedQuery.class);
        when(em.createQuery(anyString(), eq(Integer.class))).thenReturn(typedQuery);
        doThrow(new HibernateException(new RuntimeException())).when(typedQuery).getResultList();

        // exercise
        customerDao.getCustomerIds();

        // verify
        verify(sm, times(1)).getCurrentSession();
        verify(sm, never()).stopSession();
        verify(sm, never()).startSession();
        verify(em, times(1)).createQuery("select id from Customer", Integer.class);
        verify(typedQuery, times(1)).getResultList();
    }
}
