import javax.swing.*;
import java.awt.*;

public class Simulation extends JFrame {

  // these are made public and static so that panel can access them
  public static SimulationPanel panel;
  public static int rotationInDegrees;

  int FRAME_HEIGHT = 1024;
  int FRAME_WIDTH = 1024;

  // constructor for the Simulation JFrame
  public Simulation(){
    panel = new SimulationPanel();    // create panel
    setContentPane(panel);                            // add panel to the frame
    panel.saveParent(this);                        // save parent as this object

    // set visual attributes for the JFrame
    this.setForeground(Color.BLACK);
    this.setBackground(Color.WHITE);
    this.setTitle("Orientation Simulation");
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setSize(FRAME_WIDTH, FRAME_HEIGHT);
    this.setVisible(true);
  }

  public static void main(String args[]){


    Simulation frame = new Simulation();

    // rotate inner loop by 5 degrees until full rotation
    for( int i = 0; i <= 360; i += 5){
      rotationInDegrees = i;        // set global rotation amount for panel paint method
      panel.repaint();              // call panel to repaint
      try {
        Thread.sleep(100);    // sleep for 100millis to show effect
      }catch(Exception e){
        e.printStackTrace();
      }

    }
  }
}
