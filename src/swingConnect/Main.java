package swingConnect;

import javax.swing.SwingUtilities;

public class Main {

	private static Runnable gui = new Runnable() {
	        @Override
	        public void run() {
	            new GUI();
	        }};
		
	public static void main(String[] args) {
		SwingUtilities.invokeLater(gui);
		
	}

}
