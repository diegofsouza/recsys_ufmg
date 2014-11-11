package br.ufmg.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;

import br.ufmg.database.Connection;
import br.ufmg.domain.User;

public class UserRepository {
	private static final Connection CONNECTION = Connection.getInstance();
	private static final Logger log = Logger.getLogger(UserRepository.class);

	public void storeUser(User user) {
		EntityManager localConnection = CONNECTION.getConnection();
		localConnection.getTransaction().begin();
		localConnection.persist(user);
		localConnection.getTransaction().commit();
		localConnection.flush();
	}

	public User get(long id) {
		User foundUser = null;
		TypedQuery<User> user = CONNECTION.getConnection().createQuery("select count(g) from User u where u.id = :id", User.class);
		user.setParameter("id", id);
		try {
			foundUser = user.getSingleResult();
		} catch (PersistenceException e) {
			log.error(e);
		}

		return foundUser;
	}

}
