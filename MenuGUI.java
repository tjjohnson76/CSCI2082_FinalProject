package edu.century.lifProject;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MenuGUI {

	public MenuGUI() {
		
		JFrame frame = new JFrame("Default Menu");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(640, 120);
		JButton buttonBlank = new JButton("Blank");
		JButton buttonBlinker = new JButton("Blinker");
		JButton buttonToad = new JButton("Toad");
		JButton buttonPulsar = new JButton("Pulsar");
		JButton buttonPenta = new JButton("Pentadecthlon");
		JButton buttonGlider = new JButton("Glider Gun");
		JButton buttonLWSS = new JButton("Light Weight Space Ship");
		JButton buttonBeacon = new JButton("Beacon");
		JButton buttonRandom = new JButton("Random");
		
		JPanel panel = new JPanel();
		
		panel.add(buttonBlank);
		panel.add(buttonBlinker);
		panel.add(buttonToad);
		panel.add(buttonPulsar);
		panel.add(buttonPenta);
		panel.add(buttonGlider);
		panel.add(buttonLWSS);
		panel.add(buttonBeacon);
		panel.add(buttonRandom);
		
		frame.getContentPane().add(panel);
		
		frame.setVisible(true);
	}
	
	
}
