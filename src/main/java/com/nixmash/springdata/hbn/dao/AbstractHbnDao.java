package com.nixmash.springdata.hbn.dao;
/*
 * Copyright (c) 2013 Manning Publications Co.
 *
 * Book: http://manning.com/wheeler/
 * Blog: http://springinpractice.com/
 * Code: https://github.com/springinpractice
 */

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ReflectionUtils;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.Date;
import java.util.List;

// Don't put @Transactional here. It's not that it's inherently wrong--indeed it would allow us to avoid some pass-
// though service bean methods--but using @Transactional causes Spring to create proxies, and recipe 10.3 assumes that
// it has direct access to the DAOs. I don't think we're doing direct DAO injects into controllers anywhere. [WLW]

/**
 * @author Willie Wheeler (willie.wheeler@gmail.com)
 */
public abstract class AbstractHbnDao<T extends Object> implements Dao<T> {
    @Autowired
    private SessionFactory sessionFactory;
    private Class<T> domainClass;

    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    @SuppressWarnings("unchecked")
    private Class<T> getDomainClass() {
        if (domainClass == null) {
            ParameterizedType thisType = (ParameterizedType) getClass().getGenericSuperclass();
            this.domainClass = (Class<T>) thisType.getActualTypeArguments()[0];
        }
        return domainClass;
    }

    private String getDomainClassName() {
        return getDomainClass().getName();
    }

    /* (non-Javadoc)
     * @see com.springinpractice.dao.Dao#create(java.lang.Object)
     */
    @Override
    public void create(T t) {

        // If there's a setDateCreated() method, then set the date.
        Method method = ReflectionUtils.findMethod(
                getDomainClass(), "setDateCreated", Date.class);
        if (method != null) {
            try {
                method.invoke(t, new Date());
            } catch (Exception e) {
                // Ignore any exception here; simply abort the setDate() attempt
            }
        }

        getSession().save(t);
    }

    /* (non-Javadoc)
     * @see com.springinpractice.dao.Dao#get(java.io.Serializable)
     */
    @Override
    @SuppressWarnings("unchecked")
    public T get(Serializable id) {
        return (T) getSession().get(getDomainClass(), id);
    }

    /* (non-Javadoc)
     * @see com.springinpractice.dao.Dao#load(java.io.Serializable)
     */
    @Override
    @SuppressWarnings("unchecked")
    public T load(Serializable id) {
        return (T) getSession().load(getDomainClass(), id);
    }

    @SuppressWarnings("unchecked")
    public List<T> getAll() {
        return getSession()
                .createQuery("from " + getDomainClassName())
                .list();
    }

    /* (non-Javadoc)
     * @see com.springinpractice.dao.Dao#update(java.lang.Object)
     */
    @Override
    public void update(T t) {
        getSession().update(t);
    }

    /* (non-Javadoc)
     * @see com.springinpractice.dao.Dao#delete(java.lang.Object)
     */
    @Override
    public void delete(T t) {
        getSession().delete(t);
    }

    /* (non-Javadoc)
     * @see com.springinpractice.dao.Dao#deleteById(java.io.Serializable)
     */
    @Override
    public void deleteById(Serializable id) {
        delete(load(id));
    }

    /* (non-Javadoc)
     * @see com.springinpractice.dao.Dao#deleteAll()
     */
    @Override
    public void deleteAll() {
        getSession()
                .createQuery("delete " + getDomainClassName())
                .executeUpdate();
    }

    /* (non-Javadoc)
     * @see com.springinpractice.dao.Dao#count()
     */
    @Override
    public long count() {
        return (Long) getSession()
                .createQuery("select count(*) from " + getDomainClassName())
                .uniqueResult();
    }

    /* (non-Javadoc)
     * @see com.springinpractice.dao.Dao#exists(java.io.Serializable)
     */
    @Override
    public boolean exists(Serializable id) {
        return (get(id) != null);
    }
}