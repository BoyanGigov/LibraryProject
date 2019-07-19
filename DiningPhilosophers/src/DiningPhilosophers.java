
public class DiningPhilosophers {

	public volatile boolean cutleryTaken[] = { false, false, false, false, false };
	public volatile static int mealsEaten[] = { 0, 0, 0, 0, 0 };
	private static DiningPhilosophers instance = new DiningPhilosophers();
	private static final int SLEEP_DURATION = 250;
	private static final int MEALS_TO_EAT = 10;

	private DiningPhilosophers() {

	}

	private static DiningPhilosophers getInstance() {
		return instance;
	}

	boolean eat(int id) {
		id--;
		int rightCutleryID = id;
		int leftCutleryID;
		if (id == 0) {
			leftCutleryID = cutleryTaken.length - 1;
		} else {
			leftCutleryID = id - 1;
		}

		synchronized (instance) {
			if (cutleryTaken[rightCutleryID] == false && cutleryTaken[leftCutleryID] == false) {
				cutleryTaken[rightCutleryID] = true;
				cutleryTaken[leftCutleryID] = true;
				System.out.println("Philosoper " + (id + 1) + " just ate meal number " + ++mealsEaten[id]);
				return true;
			} else {
				return false;
			}
		}
	}

	void stopEating(int id) {
		id--;
		int rightCutleryID = id;
		int leftCutleryID;
		if (id == 0) {
			leftCutleryID = cutleryTaken.length - 1;
		} else {
			leftCutleryID = id - 1;
		}

		cutleryTaken[rightCutleryID] = false;
		cutleryTaken[leftCutleryID] = false;
	}

	private static void runBody(int id) {
		while (mealsEaten[id - 1] < MEALS_TO_EAT) {
			try {
				if (getInstance().eat(id)) {
					synchronized (instance) {
						getInstance().notifyAll();
						Thread.sleep(SLEEP_DURATION);
						getInstance().stopEating(id);
					}
					Thread.sleep(SLEEP_DURATION);
				} else {
					synchronized (instance) {
						getInstance().wait();
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	};

	public static void main(String args[]) {

		Thread philosopher1 = new Thread(new Runnable() {
			int id = 1;

			@Override
			public void run() {
				runBody(id);
			}
		});
		Thread philosopher2 = new Thread(new Runnable() {
			int id = 2;

			@Override
			public void run() {
				runBody(id);
			}
		});
		Thread philosopher3 = new Thread(new Runnable() {
			int id = 3;

			@Override
			public void run() {
				runBody(id);
			}
		});
		Thread philosopher4 = new Thread(new Runnable() {
			int id = 4;

			@Override
			public void run() {
				runBody(id);
			}
		});
		Thread philosopher5 = new Thread(new Runnable() {
			int id = 5;

			@Override
			public void run() {
				runBody(id);
			}
		});

		philosopher1.start();
		philosopher2.start();
		philosopher3.start();
		philosopher4.start();
		philosopher5.start();

		try {
			philosopher1.join();
			philosopher2.join();
			philosopher3.join();
			philosopher4.join();
			philosopher5.join();
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
		}

		System.out.println("Philosophers have stopped dinning.");

	}
}