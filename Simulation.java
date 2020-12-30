import javax.swing.*;
import java.awt.*;

public class Simulation extends JFrame {

  // these are made public and static so that panel can access them
  public static SimulationPanel panel;
  public static double payloadRotation;
  public static double bodyTubeRotation;

  int FRAME_HEIGHT = 1024;
  int FRAME_WIDTH = 1024;

  // constructor for the Simulation JFrame
  public Simulation() {
    panel = new SimulationPanel();                    // create panel
    setContentPane(panel);                            // add panel to the frame
    panel.saveParent(this);                        // save parent as this object


    // set visual attributes for the JFrame
    this.setForeground(Color.WHITE);
    this.setBackground(Color.LIGHT_GRAY);
    this.setTitle("Orientation Simulation");
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setSize(FRAME_WIDTH, FRAME_HEIGHT);
    this.setVisible(true);

  }

  public static void main(String args[]) {


    Simulation frame = new Simulation();

    // loop operation 10 times
    for (int i = 0; i < 10; i++) {
      System.out.println("Iteration: " + i);
      // set body tube for random rotation
      bodyTubeRotation = Math.toRadians(Math.random() * 360);
      payloadRotation = 0;
      double maxHeight = 0;
      int maxHeightAngle = 0;

      panel.repaint();            // repaint panel to show the random rotation
      try {
        Thread.sleep(1500); // sleep before beginning next step
      } catch (Exception e) {
        e.printStackTrace();
      }


      for (int j = 0; j <= 360; j++) {  // rotate payload one degree at a time
        payloadRotation = Math.toRadians(j);
        panel.repaint();              // repaint panel to show
        double currentHeight = panel.rotatedAntMaxHeight;
        if(panel.stop) break;   //break out of loop if we are intersecting the closest dot
        try {
          Thread.sleep(5);     // sleep 20 millis so we don't rotate too fast
        } catch (Exception e) {
          e.printStackTrace();
        }
        panel.repaint();

        }

      // final pause
      try{
        Thread.sleep(2000);
      }catch(Exception e){
        e.printStackTrace();
      }

      System.out.println("Max Height: " + (panel.rotatedAntMaxHeight - (panel.FRAME_HEIGHT / 2)));
      System.out.println("Possible Height: " + (panel.FRAME_HEIGHT / 2));
      System.out.println("Efficiency: " + (panel.rotatedAntMaxHeight - (panel.FRAME_HEIGHT / 2)) / (panel.FRAME_HEIGHT /2));
      System.out.println('\n');

      panel.stop = false;


      /*---------------------------------------- Sweet spot finder code -------------------------------

      payloadRotation = Math.toRadians(304);
      panel.repaint();
      for( int k = 0; k <= 360; k++) {
        bodyTubeRotation = Math.toRadians(k);
        panel.repaint();
        try{
          Thread.sleep(10);
        }catch(Exception e){
          e.printStackTrace();
        }

       */

      }
    }

    /*public static void printStats(){
      displayStats.setText("Max Height: " + panel.rotatedAntMaxHeight + "\n" +
                            "Possible Height: " + panel.FRAME_HEIGHT + "\n" +
                            "Efficiency: " + panel.rotatedAntMaxHeight / panel.FRAME_HEIGHT);

    }

     */
  }

