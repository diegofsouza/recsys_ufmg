package br.ufmg.database;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Connection {
	private static final String DATABASE_FILE = "recsys.odb";
	private static Connection instance;
	private EntityManagerFactory factory;
	private EntityManager entityManager;

	private Connection() {
		this.factory = Persistence.createEntityManagerFactory(DATABASE_FILE);
		this.entityManager = this.factory.createEntityManager();
	}

	public static Connection getInstance() {
		if (instance == null) {
			instance = new Connection();
		}

		return instance;
	}

	public EntityManager getConnection() {
		if (!factory.isOpen()) {
			this.factory = Persistence.createEntityManagerFactory(DATABASE_FILE);
		}

		if (!this.entityManager.isOpen()) {
			this.entityManager = this.factory.createEntityManager();
		}

		return this.entityManager;
	}

	public void close() {
		entityManager.close();
		factory.close();
	}
}
