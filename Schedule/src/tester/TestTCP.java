package tester;


import java.util.ArrayList;

public class TestTCP {
	private final int REPEATS = 1;
	
	public TestTCP(String address) throws InterruptedException {
		ArrayList<Connection> connections = new ArrayList<Connection>();
		for(int i=0; i<REPEATS; i++){
			connections.add(new Connection(i, address));
			connections.get(i).start();
			Thread.sleep(500);
		}
		for(int i = 0; i<REPEATS; i++){
			connections.get(i).join();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		String address = args.length > 0 ? args[0] : "192.168.1.17";
		new TestTCP(address);
	}
}
