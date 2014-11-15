package br.ufmg.repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;

import org.apache.log4j.Logger;

import br.ufmg.database.Connection;
import br.ufmg.domain.User;

public class UserRepository {
	private static final Logger log = Logger.getLogger(UserRepository.class);
	private final Connection CONNECTION = Connection.getInstance();

	public void storeUser(User user) {
		EntityManager localConnection = CONNECTION.getConnection();
		localConnection.getTransaction().begin();
		localConnection.persist(user);
		localConnection.getTransaction().commit();
		localConnection.flush();
	}

	public User get(long id) {
		User user = null;
		try {
			user = CONNECTION.getConnection().getReference(User.class, id);
		} catch (EntityNotFoundException e) {
			log.debug(e);
		}

		return user;
	}

	public void close() {
		CONNECTION.close();
	}

}
