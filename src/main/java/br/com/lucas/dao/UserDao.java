package br.com.lucas.dao;

import br.com.lucas.entity.User;

import javax.persistence.EntityManager;
import javax.persistence.RollbackException;

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
            manager.merge(user);
            manager.getTransaction().commit();
        } catch (RollbackException e) {
            manager.getTransaction().rollback();
            throw e;
        }
    }

    public void delete(Long id) {
        User user = buscar(id);
        try{
            manager.remove(user);
            manager.getTransaction().begin();
            manager.getTransaction().commit();
        }catch (Exception e){
            throw e;
        }

    }
}
