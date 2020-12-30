import javax.swing.*;
import java.awt.*;

/**This class defines the JPanel for drawing the simulation
 * It includes methods for painting the graphics
 */
public class SimulationPanel extends JPanel {

  // save global variables
  Simulation parent; // save so parent info can be accessed
  int FRAME_HEIGHT = 1024;
  int FRAME_WIDTH = 1024;
  int[] CIRCLE_CENTER = {FRAME_HEIGHT/2, FRAME_WIDTH/2};
  int CIRCLE_RADIUS = FRAME_HEIGHT/4;

  /**Simple Constructor for the Panel**/
  public SimulationPanel(){}

  public void paint(Graphics g){
    super.paintComponent(g); // clear before repainting

    Graphics2D g2 = ( Graphics2D ) g; // typecast graphics to graphics 2d

    /* ------------------------------------------------- Draw outer circle with dots ------------------------------------------------*/
    // draw center circle
    g2.setPaint(Color.BLACK);
    g2.drawOval(FRAME_WIDTH/4,FRAME_HEIGHT/4,FRAME_HEIGHT/2,FRAME_HEIGHT/2);

    // draw dots
    g2.setPaint(Color.RED);
    double[][] dots = dotArray();                           // get array of points from dotArray method
    for(int i = 0; i < dots.length; i++){                   // draw each point in turn
      g2.fillRect((int)dots[i][0], (int)dots[i][1], 4, 4);
    }

    /* -------------------------------------------------------------- inner rotation under this point ----------------------------------*/

    // rotate the rectangle based on conditions in parent
    g2.rotate(Math.toRadians(parent.rotationInDegrees), CIRCLE_CENTER[0], CIRCLE_CENTER[1]);
    // draw center rectangle
    g2.setPaint(Color.BLUE);
    double[][] sq = centerSquare();                          // get array of points from centerSquare method
    Polygon centerSquare = new Polygon();                    // define polygon to hold square points
    for( int i = 0; i < 4; i++){                             // add points from array to polygon
      centerSquare.addPoint((int)sq[i][0], (int)sq[i][1]);
      System.out.println("Square corner " + i + ": <" + sq[i][0] + ", " + sq[i][1] + ">");
    }
    g2.draw(centerSquare);

    // draw antenna loop inside of rectangle
    int antennaLoopDiameter = (int)((centerSquare.xpoints[0] - centerSquare.xpoints[1]) * 0.9);
    g2.drawOval(CIRCLE_CENTER[0] - (antennaLoopDiameter/2), CIRCLE_CENTER[1] - (antennaLoopDiameter/2), antennaLoopDiameter, antennaLoopDiameter);

    // draw antenna extension from rectangle
    double antennaAngle = Math.toRadians(50);                           // angle that antenna extends from antenna loop
    int antennaLength = 400;                                            // length of antenna extending from antenna loop
    int startPointX = (int) ( CIRCLE_CENTER[0] + (antennaLoopDiameter / 2) * Math.cos(Math.toRadians(45)));    // start point for antenna (right edge of antenna loop)
    int startPointY = (int) ( CIRCLE_CENTER[1] + (antennaLoopDiameter / 2) * Math.sin(Math.toRadians(45)));
    int endPointX = (int) (startPointX + ( antennaLength * Math.cos(antennaAngle))); // end point of antenna (start + length * cos/sin angle)
    int endPointY = (int) (startPointY - ( antennaLength * Math.sin(antennaAngle)));
    g2.drawLine(startPointX, startPointY, endPointX, endPointY);



  }


  /**This method saves the parent so that fields in simulation can be accessed from Simulation Panel
   *
   * @param p Parent object
   */
  public void saveParent(Simulation p){
    parent = p;
  }

  /**This method returns a 8x2 array of coordinates for dots on the circle
   */
  public double[][] dotArray(){
    // init empty array of double for coordinates
    double[][] dots = new double[8][2];

    // fill array
    for(int i = 0; i < 8; i++) {
      // find radians for the current loop around the circle
      double deg = i * 45.0;
      double rad = Math.toRadians(deg);
      dots[i][0] = CIRCLE_CENTER[0] + (CIRCLE_RADIUS * Math.cos(rad));
      dots[i][1] = CIRCLE_CENTER[0] + (CIRCLE_RADIUS * Math.sin(rad));
    }

    return dots;
  }

  /**This method returns a 4x2 array of coordinates for the corners of the center square
   * This makes a box that is 80% of the size of the outer circle
   */
  public double[][] centerSquare(){
    double[][] square = new double[4][2];

    for(int i = 0; i < 4; i++){
      double deg = (i + 0.5) * 90.0; // 0.5 added to one makes corners of square at 45/135/225/315
      double rad = Math.toRadians(deg);
      square[i][0] = CIRCLE_CENTER[0] + ((CIRCLE_RADIUS * 0.9) * Math.cos(rad));
      square[i][1] = CIRCLE_CENTER[0] + ((CIRCLE_RADIUS * 0.9) * Math.sin(rad));
    }

    return square;
  }
}

