package org.academiadecodigo.javabank.services.jpa;

import org.academiadecodigo.javabank.model.account.Account;
import org.academiadecodigo.javabank.services.AccountService;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.RollbackException;

/**
 * A JPA {@link AccountService} implementation
 */
public class JpaAccountService extends AbstractJpaService<Account> implements AccountService {

    /**
     * @see AbstractJpaService#AbstractJpaService(EntityManagerFactory, Class)
     */
    public JpaAccountService(EntityManagerFactory emf) {
        super(emf, Account.class);
    }

    /**
     * @see AccountService#deposit(Integer, double)
     */
    @Override
    public void deposit(Integer id, double amount) {

        EntityManager em = emf.createEntityManager();

        try {

            em.getTransaction().begin();

            Account account = em.find(Account.class, id);

            if (account == null) {
                em.getTransaction().rollback();
                throw new IllegalArgumentException("invalid account id");
            }

            account.credit(amount);

            em.getTransaction().commit();

        } catch (RollbackException ex) {

            em.getTransaction().rollback();

        } finally {

            if (em != null) {
                em.close();
            }
        }
    }

    /**
     * @see AccountService#withdraw(Integer, double)
     */
    @Override
    public void withdraw(Integer id, double amount) {

        EntityManager em = emf.createEntityManager();

        try {

            em.getTransaction().begin();

            Account account = em.find(Account.class, id);

            if (account == null) {
                em.getTransaction().rollback();
                throw new IllegalArgumentException("invalid account");
            }

            account.debit(amount);

            em.getTransaction().commit();

        } catch (RollbackException ex) {

            em.getTransaction().rollback();

        } finally {

            if (em != null) {
                em.close();
            }
        }
    }

    /**
     * @see AccountService#transfer(Integer, Integer, double)
     */
    @Override
    public void transfer(Integer srcId, Integer dstId, double amount) {

        EntityManager em = emf.createEntityManager();

        try {

            em.getTransaction().begin();

            Account srcAccount = em.find(Account.class, srcId);
            Account dstAccount = em.find(Account.class, dstId);

            if (srcAccount == null || dstAccount == null) {
                em.getTransaction().rollback();
                throw new IllegalArgumentException("invalid account id");
            }

            // make sure transaction can be performed
            if (srcAccount.canDebit(amount) && dstAccount.canCredit(amount)) {
                srcAccount.debit(amount);
                dstAccount.credit(amount);
            }

            em.getTransaction().commit();

        } catch (RollbackException ex) {

            em.getTransaction().rollback();

        } finally {

            if (em != null) {
                em.close();
            }
        }
    }
}
