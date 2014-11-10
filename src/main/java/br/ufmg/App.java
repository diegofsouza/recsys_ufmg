package br.ufmg;

import br.ufmg.repository.GameRepository;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) {
//		long error = 0;
//		long ok = 0;
//		// long firtUser = 76561197960287930L;
//		long firstUser = 76561100000000000L;
//		long user = 76561198076264300L;
//		long lastUser = 76561198157200427L;// test user created 03-10
//		for (int i = 0; i < 76561198157200427L; i++) {
//			try {
//				SteamId id = SteamId.create(firstUser++);
//				if (id != null) {
//					ok++;
//				}
//			} catch (SteamCondenserException e) {
//				error++;
//			}
//		}
//
//		System.out.println("Errors: " + error + " / Ok: " + ok);

		importGames();

	}

	private static void importGames() {
		GameRepository repo = new GameRepository();
		repo.importGames();
	}
}
