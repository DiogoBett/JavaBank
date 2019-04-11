package org.academiadecodigo.javabank.persistence.dao.jpa;

import org.academiadecodigo.javabank.model.Recipient;
import org.academiadecodigo.javabank.persistence.dao.RecipientDao;
import org.academiadecodigo.javabank.persistence.jpa.JpaSessionManager;

/**
 * A JPA {@link RecipientDao} implementation
 */
public class JpaRecipientDao extends GenericJpaDao<Recipient> implements RecipientDao {

    /**
     * @see GenericJpaDao#GenericJpaDao(JpaSessionManager, Class)
     */
    public JpaRecipientDao(JpaSessionManager sm) {
        super(sm, Recipient.class);
    }
}
