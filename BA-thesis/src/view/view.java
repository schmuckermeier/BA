package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class view {
	public static void main(String[] args) {
		JFrame frame = new JFrame("Bachelor Thesis - Sophie Schmuckermeier - 2017");
		frame.setVisible(true);
		frame.setSize(500, 200);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panel = new JPanel();
		frame.add(panel);
		JLabel header = new JLabel("Choose File for prediction:");
		panel.add(header);
		JButton loadButton = new JButton("Load File");
		panel.add(loadButton);
		loadButton.addActionListener(new LoadAction());

		JButton exitButton = new JButton("Exit");
		panel.add(exitButton);
		exitButton.addActionListener(new ExitAction());

	}

	static class LoadAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JFileChooser chooser = new JFileChooser();
			chooser.showOpenDialog(null);
		}
	}

	static class ExitAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	}
}
