package br.ufmg.database;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Connection {
	private static final String DATABASE_FILE = "recsys.odb";
	private EntityManagerFactory factory;
	private static Connection instance;

	private Connection() {
		factory = Persistence.createEntityManagerFactory(DATABASE_FILE);
	}

	public static Connection getInstance() {
		if (instance == null) {
			instance = new Connection();
		}

		return instance;
	}

	public EntityManager getConnection() {
		return this.factory.createEntityManager();
	}
}
