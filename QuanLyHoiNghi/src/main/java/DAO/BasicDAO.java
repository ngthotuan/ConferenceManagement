package DAO;

import DTO.DTO;
import Utils.HibernateUtils;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public abstract class BasicDAO {

    protected static DTO get(String id, Class<? extends DTO> dtoClass){
        DTO DTO = null;
        Session session = HibernateUtils.getSessionFactory().openSession();
        try{
            DTO = session.get(dtoClass, id);
        } catch (Exception e){
            e.printStackTrace();
        }
        return DTO;
    }
    protected static DTO get(int id, Class<? extends DTO> dtoClass){
        DTO DTO = null;
        Session session = HibernateUtils.getSessionFactory().openSession();
        try{
            DTO = session.get(dtoClass, id);
        } catch (Exception e){
            e.printStackTrace();
        }
        return DTO;
    }
    protected static List<? extends DTO> getAll(String table){
        List<? extends DTO> ds = null;
        Session session = HibernateUtils.getSessionFactory().openSession();
        try {
            String hql = String.format("from %s", table);
            Query query = session.createQuery(hql);
            ds = query.list();
        } catch (HibernateException ex) {
            //Log the exception
            System.err.println(ex);
        } finally {
            session.close();
        }
        return ds;
    }
    protected static boolean create(DTO DTO){
        boolean result = true;
        Session session = HibernateUtils.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.save(DTO);
            transaction.commit();
        } catch (HibernateException ex) {
            //Log the exception
            transaction.rollback();
            System.err.println(ex);
            result = false;
        } finally {
            session.close();
        }
        return result;
    }
    protected static boolean update(DTO DTO){
        boolean result = true;
        Session session = HibernateUtils.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.update(DTO);
            transaction.commit();
        } catch (HibernateException ex) {
            //Log the exception
            transaction.rollback();
            System.err.println(ex);
            result = false;
        } finally {
            session.close();
        }
        return result;
    }

}
