package org.academiadecodigo.javabank.services;

import org.academiadecodigo.javabank.model.AbstractModel;

import java.util.List;

/**
 * Base interface for CRUD services, provides methods do manage models
 * @param <T> the model type to be managed
 */
public interface CRUDService<T extends AbstractModel> {

    /**
     * Gets a list of the model type
     *
     * @return the model list
     */
    List<T> list();

    /**
     * Gets the model
     *
     * @param id the model id
     * @return the model
     */
    T get(Integer id);

    /**
     * Saves the model
     *
     * @param modelObject the model to be saved
     * @return the saved model
     */
    T save(T modelObject);

    /**
     * Deletes a model
     *
     * @param id the id of the model to be deleted
     */
    void delete(Integer id);
}
