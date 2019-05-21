/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mssmfactory.KafylElYatim.Utilities.DataHandlers;

import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

/**
 *
 * @author MSSM
 */
public class SessionFactoryHandler {
    
    private String hibernateMappingFilePath = null;
    private SessionFactory sessionFactory = null;
    
    public SessionFactoryHandler(String hibernateMappingFilePath){
        this.hibernateMappingFilePath = hibernateMappingFilePath;
        this.sessionFactory = (new Configuration().configure(hibernateMappingFilePath)).buildSessionFactory();
    }
    public SessionFactoryHandler(){
        this.sessionFactory = (new Configuration().configure()).buildSessionFactory();
    }
        
    public synchronized void insert(Object ... entities){
    	try (Session session = getSessionFactory().openSession()) {
            session.beginTransaction();
            
            for(Object mssmEntity: entities)
                session.save(mssmEntity);
            
            session.getTransaction().commit();
        }
    }
    
    public synchronized void update(Object ... entities){
    	try (Session session = getSessionFactory().openSession()) {
            session.beginTransaction();
            
            for(Object mssmEntity: entities)
                session.update(mssmEntity);
            
            session.getTransaction().commit();
        }        
    }
    
    public synchronized void remove(Object ... entities){
    	try (Session session = getSessionFactory().openSession()) {
            session.beginTransaction();
            
            for(Object mssmEntity: entities)
                session.delete(mssmEntity);
            
            session.getTransaction().commit();
        }
    }
    
    public synchronized List<Object> select(String stringQuery, Object ... params){
        List<Object> result = null;
        try (Session session = getSessionFactory().openSession()) {
            session.beginTransaction();
            
            Query query = session.createQuery(stringQuery);
            
            if(params != null)
                for (int i = 0; i < params.length; i++)
                    if (params[i] != null)
                        query.setParameter(i, params[i]);
            
            result = query.getResultList();
            
            session.getTransaction().commit();
        }
        
        return result;
    }
    
    public synchronized List<Object> select(String stringQuery, Map<String, Object> params){
        List<Object> result = null;
        try (Session session = getSessionFactory().openSession()) {
            session.beginTransaction();
            
            Query query = session.createQuery(stringQuery);
            
            if(params != null)
                for (String key: params.keySet()) {
                	Object value = params.get(key);
                	
                	if (value != null)
                        query.setParameter(key, value);
                }
            
            result = query.getResultList();
            
            session.getTransaction().commit();
        }
        
        return result;
    }
    
    public synchronized List<Object> selectWithLimit(String stringQuery, int limit, Object ... params){
        List<Object> result = null;
        
        try (Session session = getSessionFactory().openSession()) {
            session.beginTransaction();
            
            Query query = session.createQuery(stringQuery);
            
            if(params != null)
                for (int i = 0; i < params.length; i++)
                    if (params[i] != null)
                        query.setParameter(i, params[i]);
            
            query.setMaxResults(limit);
            
            result = query.getResultList();
            
            session.getTransaction().commit();
        }
        
        return result;
    }
    
    public synchronized int executeHql(String stringQuery, Object ... params){
        try (Session session = getSessionFactory().openSession()) {
            session.beginTransaction();
            
            Query query = session.createQuery(stringQuery);
            
            if(params != null)
                for (int i = 0; i < params.length; i++)
                    if (params[i] != null)
                        query.setParameter(i, params[i]);
            
            return query.executeUpdate();
        }
    }

    public String getHibernateMappingFilePath() {
        return hibernateMappingFilePath;
    }

    public void setHibernateMappingFilePath(String hibernateMappingFilePath) {
        this.hibernateMappingFilePath = hibernateMappingFilePath;
    }

    public synchronized SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public synchronized void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
}
