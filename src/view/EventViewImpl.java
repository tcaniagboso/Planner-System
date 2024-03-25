package view;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;


public class EventViewImpl extends JFrame implements EventView {

  public EventViewImpl() {
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setResizable(false);
    this.setSize(1000, 1000);
    this.setTitle("Event");
    this.setLayout(null);
    this.setVisible(true);
    this.getContentPane().setBackground(Color.lightGray);
    this.displayLabel("Event name:", 0, 15, 200, 30);
    this.displayLabel("Location:", 0, 200, 200, 30);
    this.displayLabel("Starting Day:", 0, 350, 200, 30);
    this.displayLabel("Starting Day:", 0, 350, 200, 30);
    this.displayLabel("Starting time:", 0, 450, 200, 30);
    this.displayLabel("Ending Day:", 0, 550, 200, 30);
    this.displayLabel("Ending Time:", 0, 650, 200, 30);
    this.displayLabel("Available users:", 0, 750, 200, 30);
  }

  public void displayLabel(String name, int x, int y, int width, int height) {
    JLabel label = new JLabel();
    label.setText(name);
    label.setForeground(Color.black);
    label.setFont(new Font("Calibri", Font.PLAIN, 30));
    label.setBounds(x, y, width, height);
    this.add(label);
  }
}
