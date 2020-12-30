import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Simulation extends JFrame {

  // these are made public and static so that panel can access them
  public static SimulationPanel panel;
  public static double payloadRotation;
  public static double bodyTubeRotation;

  int FRAME_HEIGHT = 1024;
  int FRAME_WIDTH = 1024;
  private JTextArea textArea1;
  private JTextArea textArea2;

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

    // loop operation 10 times
    for(int i = 0; i < 10; i ++) {
      //System.out.println("Iteration: " + i);
      // set body tube for random rotation
      bodyTubeRotation = Math.toRadians(Math.random() * 360);
      payloadRotation = 0;
      int maxHeight = 0;
      int maxHeightAngle = 0;
      int[] maxHeightCoords = new int[2];

      panel.repaint();            // repaint panel to show the random rotation
      try {
        Thread.sleep(1500); // sleep before beginning next step
      } catch (Exception e) {
        e.printStackTrace();
      }

      for (int j = 0; j <= 360; j++) {  // rotate payload one degree at a time
        payloadRotation = Math.toRadians(j);
        panel.repaint();              // repaint panel to show
        int currentHeight = panel.FRAME_HEIGHT - panel.antEndPointY;
        System.out.println(panel.antEndPointX + ", " + panel.antEndPointY);
        if( currentHeight > maxHeight){
          maxHeight = currentHeight;
          maxHeightAngle = j;
          maxHeightCoords[0] = panel.antStartPointX;
          maxHeightCoords[1] = panel.antStartPointY;
        }
        try {
          Thread.sleep(10);     // sleep 20 millis so we don't rotate too fast
        } catch (Exception e) {
          e.printStackTrace();
        }
      }

      System.out.println("Max Height: " + maxHeight);
      System.out.println("Max Height Angle: " + maxHeightAngle);
      System.out.println("Max Height Coords: <" + maxHeightCoords[0] + ", " + maxHeightCoords[1] + ">");


    }

  }
}
