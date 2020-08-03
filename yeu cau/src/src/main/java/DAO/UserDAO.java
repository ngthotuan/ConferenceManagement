package DAO;

import DTO.User;
import Utils.HibernateUtils;
import Utils.Password;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class UserDAO extends BasicDAO{

    public static User getUser(String username){
        return (User) get(username, User.class);
    }
    public static List<User> getUsers(){
        return (List<User>) getAll("User");
    }

    public static boolean createUser(User user) {
        if(getUser(user.getUsername()) != null){
            return false;
        }
        else {
            user.setPassword(Password.genPassword(user.getPassword()));
            return create(user);
        }
    }
    public static boolean updateUser(User user) {
        if(getUser(user.getUsername()) == null){
            return false;
        }
        else {
//            user.setPassword(Password.genPassword(user.getPassword()));
            return update(user);
        }
    }
    public static User login(String username, String password) {
        User user = getUser(username);
        if(user != null){
            boolean check = Password.checkPassword(password, user.getPassword());
            if(!check) user = null;
        }
        return user;
    }

    public static List<User> findUserByKey(String key){
        List<User> ds = new ArrayList<>();
        Session session = HibernateUtils.getSessionFactory().openSession();
        try {
            String hql = "from User where username like :key or name like :key or email like :key";
            Query query = session.createQuery(hql);
            query.setParameter("key", "%" + key + "%");
            ds = query.list();
        } catch (HibernateException ex) {
            //Log the exception
            System.err.println(ex);
        } finally {
            session.close();
        }
        return ds;
    }

    public static List<User> findUserByKey(String key, int role, int status){
        List<User> ds = new ArrayList<>();
        String r="";
        if(role == 1){
            r = " and isAdmin = true";
        } else if(role == 2){
            r = " and isAdmin = false";
        }
        String s="";
        if(status == 1){
            s = " and isBlocked = true";
        } else if(status == 2){
            s = " and isBlocked = false";
        }
        Session session = HibernateUtils.getSessionFactory().openSession();
        try {
            String hql = "from User where (username like :key or name like :key or email like :key)";
            if(!r.isEmpty()){
                hql += r;
            }
            if(!s.isEmpty()){
                hql += s;
            }
            Query query = session.createQuery(hql);
            query.setParameter("key", "%" + key + "%");
            ds = query.list();
        } catch (HibernateException ex) {
            //Log the exception
            System.err.println(ex);
        } finally {
            session.close();
        }
        return ds;
    }

}

