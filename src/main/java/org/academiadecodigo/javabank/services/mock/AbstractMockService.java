package org.academiadecodigo.javabank.services.mock;

import org.academiadecodigo.javabank.model.AbstractModel;
import org.academiadecodigo.javabank.services.CRUDService;

import java.util.*;

/**
 * A generic mock service to be used as a base for concrete mock service implementations
 * @see CRUDService
 * @param <T> the model type
 */
public abstract class AbstractMockService<T extends AbstractModel> implements CRUDService<T> {

    protected Map<Integer, T> modelMap = new HashMap<>();

    /**
     * Gets the next model id
     *
     * @return the model id
     */
    private Integer getNextId() {
        return modelMap.isEmpty() ? 1 : Collections.max(modelMap.keySet()) + 1;
    }

    /**
     * @see CRUDService#list()
     */
    @Override
    public List<T> list() {
        return new ArrayList<>(modelMap.values());
    }

    /**
     * @see CRUDService#get(Integer)
     */
    @Override
    public T get(Integer id) {
        return modelMap.get(id);
    }

    /**
     * @see CRUDService#save(AbstractModel)
     */
    @Override
    public T save(T modelObject) {

        if (modelObject.getId() == null) {
            modelObject.setId(getNextId());
        }

        modelMap.put(modelObject.getId(), modelObject);

        return modelObject;

    }

    /**
     * @see CRUDService#delete(Integer)
     */
    @Override
    public void delete(Integer id) {
        modelMap.remove(id);
    }
}
