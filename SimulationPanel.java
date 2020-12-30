import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

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
  double rotatedAntMaxHeight;
  int SWEET_SPOT_X = 590;   //sweet spot is ideal position for body tube. we want to be closest to this
  int SWEET_SPOT_Y = 265;
  boolean stop = false;     // determines if antenna is in spot closest to sweet spot
  String direction = "cw"; // this string holds the direction the payload needs to turn. cw/ ccw



  /**Simple Constructor for the Panel**/
  public SimulationPanel(){
    this.setBackground(Color.BLACK);
  }

  public void paint(Graphics g){
    super.paintComponent(g); // clear before repainting

    Graphics2D g2 = ( Graphics2D ) g; // typecast graphics to graphics 2d
    /*-------------------------------------------------Put in the sweet spot--------------------------------------------------------*/
    g2.setPaint(Color.YELLOW);
    Rectangle2D sweetSpot = new Rectangle2D.Double(SWEET_SPOT_X, SWEET_SPOT_Y, 6 ,6);
    g2.fill(sweetSpot);


    /*-------------------------------------------------- Update Data Fields ---------------------------------------------------------*/


    AffineTransform bodyTubeTransform = new AffineTransform();
    bodyTubeTransform.rotate(parent.bodyTubeRotation, CIRCLE_CENTER[0], CIRCLE_CENTER[1]);
    /* ------------------------------------------------- Draw body tube with dots ------------------------------------------------*/
    // draw body tube
    Shape bodyTube = new Ellipse2D.Double(FRAME_WIDTH/4, FRAME_HEIGHT/4, FRAME_WIDTH /2, FRAME_HEIGHT /2); // define the body tube ellipse
    Shape bodyTubeRotated = bodyTubeTransform.createTransformedShape(bodyTube);                                                // rotate body tube around origin with transform
    g2.setPaint(Color.WHITE);
    g2.draw(bodyTubeRotated);

    // draw dots
    g2.setPaint(Color.RED);
    double[][] dots = dotArray();                           // get array of points from dotArray method
    Shape[] dotShapeList = new Shape[8];
    for(int i = 0; i < dots.length; i++){                   // draw each point in turn
      Shape dot = new Rectangle2D.Double((int)dots[i][0], (int)dots[i][1], 6, 6);       // define the dot
      Shape dotRotated = bodyTubeTransform.createTransformedShape(dot);                       // rotate the dot about origin with affine transform
      dotShapeList[i] = dotRotated;
      g2.fill(dotRotated);
    }

    /* -------------------------------------------------------------- inner rotation under this point ----------------------------------*/

    // rotate the rectangle based on conditions in parent
    AffineTransform payloadTransform = new AffineTransform();                             // initialize transform
    payloadTransform.rotate(parent.payloadRotation, CIRCLE_CENTER[0], CIRCLE_CENTER[1]);  // define transform based on parent

    // draw center rectangle
    g2.setPaint(Color.BLUE);
    double[][] squareArray = centerSquare();                                      // get array of points from centerSquare method
    Polygon payload = new Polygon();                                              // define polygon shape
    for(int i = 0; i < squareArray.length; i++){                                  // add points from squareArray to payload in turn
      payload.addPoint((int)squareArray[i][0], (int)squareArray[i][1]);
    }
    Shape payloadRotated = payloadTransform.createTransformedShape(payload);      // rotate payload with transform
    g2.draw(payloadRotated);

    // draw antenna loop inside of rectangle
    int antennaLoopDiameter = (int)((payload.getBounds().width ) * 0.9);          // set diameter of antenna loop to 90% of the payload width
    Shape antennaLoop = new Ellipse2D.Double(                                     // define shape of antenna loop
        CIRCLE_CENTER[0] - (antennaLoopDiameter/2),                            // x-coord is one radius less than the center
        CIRCLE_CENTER[1] - (antennaLoopDiameter/2),                            // y-coord is one radius less than the center
          antennaLoopDiameter,
          antennaLoopDiameter
    );
    Shape antennaLoopRotated = payloadTransform.createTransformedShape(antennaLoop);// rotate the antenna loop
    g2.draw(antennaLoopRotated);

    // draw antenna extension from rectangle
    double antennaAngle = Math.toRadians(50);                                                                   // angle that antenna extends from antenna loop
    int antennaLength = 500;                                                                                    // length of antenna extending from antenna loop
    double antStartPointX = ( CIRCLE_CENTER[0] + (antennaLoopDiameter / 2) * Math.cos(Math.toRadians(45)));     // start point for antenna (right edge of antenna loop)
    double antStartPointY = ( CIRCLE_CENTER[1] + (antennaLoopDiameter / 2) * Math.sin(Math.toRadians(45)));
    double antEndPointX = (antStartPointX + ( antennaLength * Math.cos(antennaAngle)));                         // end point of antenna (start + length * cos/sin angle)
    double antEndPointY = (antStartPointY - ( antennaLength * Math.sin(antennaAngle)));
    Shape antenna = new Line2D.Double(                                                                          // define shape of antenna
        antStartPointX,
        antStartPointY,
        antEndPointX,
        antEndPointY
    );
    Shape antennaRotated = payloadTransform.createTransformedShape(antenna);                                    // rotate the antenna
    g2.draw(antennaRotated);

    /*-------------------------------------------------------------Check Intersections ---------------------------------------*/
    Shape closest = dotShapeList[0]; // initiate closest point with first shape
    for(int i = 0; i < dotShapeList.length; i++){

      // print intersections to console
      if( antennaRotated.intersects(
          dotShapeList[i].getBounds2D().getX(),
          dotShapeList[i].getBounds2D().getY(),
          6,
          6)){
        System.out.println("Intersection at <" + dotShapeList[i].getBounds2D().getX()
                            + ", " + dotShapeList[i].getBounds2D().getY() + ">");
      }

      // determine which shape is closest
      if( distance(sweetSpot, dotShapeList[i]) < distance(sweetSpot, closest)){
        closest = dotShapeList[i];
      }
    }

    /*----------------------------------------------------------Set globals to turn shape correct way -------------------------*/
    if( antennaRotated.intersects(closest.getBounds().getX(), closest.getBounds().getY(), 6, 6)){
      stop = true;
      System.out.println("Most efficient spot found");
    }




    // save rotated coords to global for parent
    rotatedAntMaxHeight = FRAME_HEIGHT - antennaRotated.getBounds2D().getMinY();



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

  /** this method accepts two rectangles and returns the distance between them
   *
   */
  public double distance(Shape r1, Shape r2){
    double x1 = r1.getBounds().getX();
    double x2 = r2.getBounds().getX();
    double y1 = r1.getBounds().getY();
    double y2 = r2.getBounds().getY();
    return (Math.sqrt( Math.pow( (x1-x2), 2) + Math.pow( (y1-y2), 2) ));
  }

}

