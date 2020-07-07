package br.com.lucas.dao;

import br.com.lucas.entity.User;

import javax.persistence.EntityManager;
import javax.persistence.RollbackException;
import javax.persistence.TypedQuery;
import java.util.List;

public class UserDao {

    EntityManager manager = PersistenceManager
            .getInstance().getEntityManager();


    public User buscar(Long id) {
        try {
            return manager.find(User.class, id);
        } catch (Exception e) {
            throw e;
        }
    }

    public void salvar(User user) throws RollbackException {
        try {
            manager.getTransaction().begin();
            salvarSemCommit(user);
            manager.flush();
            manager.getTransaction().commit();
        } catch (RollbackException e) {
            manager.getTransaction().rollback();
            throw e;
        }
    }

    public User buscarPorEmail(String email) {
        String consulta = "select u from User u where userEmail = :email";
        TypedQuery<User> query = manager.createQuery(consulta, User.class);
        query.setParameter("email", email);
        List<User> users = query.getResultList();
        return users.get(0);
    }

    public void salvarSemCommit(User user) {
        if(user.getIdUser() == null) {
            manager.persist(user);
        }
        else {
            manager.merge(user);
        }
    }

    public void delete(Long id) {
        User user = buscar(id);
        try{
            manager.getTransaction().begin();
            manager.remove(user);
            manager.getTransaction().commit();
        }catch (Exception e){
            manager.getTransaction().rollback();
            throw e;
        }

    }
}
