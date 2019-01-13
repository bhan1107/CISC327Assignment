public class WeeklyScript {
	public static void main(String[] args) {
		for (int i = 0; i < 5; i++) {
			System.out.println("Beginning of day " + (i+1));
			new DailyScript(3);
			System.out.println("End of day " + (i+1));
		}
	}
}
