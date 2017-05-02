package view;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class view {
	public static void main(String[] args) {
		JFrame frame = new JFrame("Test");
		frame.setVisible(true);
		frame.setSize(500, 200);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panel = new JPanel();
		frame.add(panel);
		JButton button = new JButton("Load");
		panel.add(button);

		JButton button2 = new JButton("Exit");
		panel.add(button2);
	}

}
