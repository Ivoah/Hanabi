import java.util.Random;

public class Driver {
	public static Random rand = new Random();
	public static int seed = rand.nextInt();

	public static void main(String[] args) {
		seed = 1494450377;
		Hanabi game = new Hanabi(true);
		game.play();

		// This is the code I will actually use to test your code's behavior.
//		final int games = 1000;
//		int total = 0;
//		for (int i = 0; i < games; i++) {
//			seed = rand.nextInt();
//			Hanabi next = new Hanabi(false);
//			int score = next.play();
//			System.out.println("Game " + i + " score: " + score);
//			total += score;
//		}
//		System.out.println("Final average: " + ((double)total/games));
	}

}
