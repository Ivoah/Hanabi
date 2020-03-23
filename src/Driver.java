import java.util.Random;

public class Driver {
	public static Random rand = new Random();
	public static int seed = rand.nextInt();

	public static void main(String[] args) {
		seed = 1681249626;
		Hanabi game = new Hanabi(true);
		game.play();

		// This is the code I will actually use to test your code's behavior.
//		int total = 0;
//		for (int i = 0; i < 1000; i++) {
//			seed = rand.nextInt();
//			Hanabi next = new Hanabi(false);
//			int score = next.play();
//			System.out.println("Game " + i + " score: " + score);
//			if (score == 0) System.out.println(seed);
//			total += score;
//		}
//		System.out.println("Final average: " + (total/1000.0));

//		int total = 0;
//		double blah = 0.0;
//		for (int i = 0; i < 1000; i++) {
//			Hanabi next = new Hanabi(false);
//			int score = next.play();
//			System.out.println("Game " + i + " score: " + score);
//			if (score != 0) {
//				blah += 1.0;
//				total += score;
//			}
//		}
//		System.out.println("Final average: " + (total/blah));
	}

}
