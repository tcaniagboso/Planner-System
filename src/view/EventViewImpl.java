package view;

import javax.swing.JFrame;

public class EventViewImpl extends JFrame implements EventView {

  public EventViewImpl() {
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setSize(500, 500);
    this.setVisible(true);

  }
}
