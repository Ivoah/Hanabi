
public class Driver {
	public static void main(String[] args) {
		Hanabi game = new Hanabi(true);
		game.play();
		
		// This is the code I will actually use to test your code's behavior.
//		int total = 0;
//		for (int i = 0; i < 1000; i++) {
//			Hanabi next = new Hanabi(false);
//			int score = next.play();
//			System.out.println("Game " + i + " score: " + score);
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
