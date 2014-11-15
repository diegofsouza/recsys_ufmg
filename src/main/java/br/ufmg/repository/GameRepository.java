package br.ufmg.repository;

import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;

import br.ufmg.database.Connection;
import br.ufmg.domain.Game;

public class GameRepository {
	private static final Logger log = Logger.getLogger(GameRepository.class);
	private Connection CONNECTION = Connection.getInstance();

	public boolean existsInDatabase(Game game) {
		boolean found = false;
		TypedQuery<Long> count = CONNECTION.getConnection().createQuery("select count(g) from Game g where g.id = :id", Long.class);
		count.setParameter("id", game.getId());
		try {
			found = count.getSingleResult() > 0;
		} catch (PersistenceException e) {
			log.error(e);
		}

		return found;
	}

	public void storeGame(Game game) {
		CONNECTION.getConnection().getTransaction().begin();
		CONNECTION.getConnection().persist(game);
		CONNECTION.getConnection().getTransaction().commit();
		CONNECTION.getConnection().flush();
		log.info("Object stored!");
	}

	public void close() {
		CONNECTION.close();
	}

	public Game get(Long id) {
		Game game = null;
		try {
			game = CONNECTION.getConnection().getReference(Game.class, id);
		} catch (EntityNotFoundException e) {
			log.debug(e);
		}

		return game;

	}
}
