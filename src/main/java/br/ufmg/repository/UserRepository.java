package br.ufmg.repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import br.ufmg.database.Connection;
import br.ufmg.domain.User;

public class UserRepository {
	private static final Connection CONNECTION = Connection.getInstance();

	public void storeUser(User user) {
		EntityManager localConnection = CONNECTION.getConnection();
		localConnection.getTransaction().begin();
		localConnection.persist(user);
		localConnection.getTransaction().commit();
		localConnection.flush();
	}

	public User get(long id) {
		TypedQuery<User> user = CONNECTION.getConnection().createQuery("select count(g) from User u where u.id = :id", User.class);
		user.setParameter("id", id);

		return user.getSingleResult();
	}

}
