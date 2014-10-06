package br.ufmg.database;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;


public class Connection {
	private static final String DB4O_FILENAME = "recsys.yap";
	private ObjectContainer db4o;
	private static Connection instance;

	private Connection() {
		db4o = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), DB4O_FILENAME);
	}

	public static Connection getInstance() {
		if (instance == null) {
			instance = new Connection();
		}

		return instance;
	}

	public ObjectContainer getConnection() {
		return this.db4o;
	}
}
