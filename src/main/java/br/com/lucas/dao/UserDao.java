package br.com.lucas.dao;

import br.com.lucas.entity.User;

import javax.persistence.EntityManager;
import javax.persistence.RollbackException;
import javax.persistence.TypedQuery;

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
        String consulta = "select a from User a where userEmail = :email";
        TypedQuery<User> query = manager.createQuery(consulta, User.class);
        query.setParameter("nome", email);
        return query.getSingleResult();
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
            throw e;
        }

    }
}
