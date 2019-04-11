package org.academiadecodigo.javabank.persistence.dao.jpa;

import org.academiadecodigo.javabank.model.account.Account;
import org.academiadecodigo.javabank.persistence.dao.AccountDao;
import org.academiadecodigo.javabank.persistence.jpa.JpaSessionManager;

/**
 * A JPA {@link AccountDao} implementation
 */
public class JpaAccountDao extends GenericJpaDao<Account> implements AccountDao {

    /**
     * @see GenericJpaDao#GenericJpaDao(JpaSessionManager, Class)
     */
    public JpaAccountDao(JpaSessionManager sm) {
        super(sm, Account.class);
    }
}
