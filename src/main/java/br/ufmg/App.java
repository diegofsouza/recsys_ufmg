package br.ufmg;

import br.ufmg.repository.GameRepository;
import br.ufmg.service.UserService;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) {
		if (args.length < 1) {
			throw new RuntimeException("error... verify parameters");
		}

		String appFunction = args[0].toUpperCase();

		if (AppFunction.GAMES.name().equals(appFunction)) {
			importGames();
		} else if (AppFunction.USERS.name().equals(appFunction)) {
			importUsers();
		}

	}

	private static void importUsers() {
		UserService service = new UserService();
		service.importUsers();
	}

	private static void importGames() {
		GameRepository repo = new GameRepository();
		repo.importGames();
	}
}
