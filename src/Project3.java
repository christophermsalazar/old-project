import java.awt.*;
import java.util.*;

public class Project3 {
  
  public static final int SLEEP_TIME = 50;
  
  // keys
  public static final int RIGHT_ARROW = 39;
  public static final int LEFT_ARROW = 37;
  public static final int UP_ARROW = 38;
  
  // panel size
  public static final int PANEL_WIDTH = 600;
  public static final int PANEL_HEIGHT = 400;
  
  // patrol ship
  public static final int PATROL_Y = PANEL_HEIGHT-50;
  public static final int PATROL_WIDTH = 30;
  public static final int PATROL_HEIGHT = 18;
  public static final int PATROL_MOVE_AMOUNT = 3;
  public static final int PATROL_INITIAL_X = PANEL_WIDTH - PATROL_WIDTH - 5;
  public static int patrolX;
  
  // enemy ship
  public static final int ENEMY_Y = 20;
  public static final int ENEMY_WIDTH = 36;
  public static final int ENEMY_HEIGHT = 21;
  public static final int ENEMY_INITIAL_X = 5;
  public static int enemyX;
  
  // patrol missile
  public static final int PATROL_MISSILE_LENGTH = 10;
  public static final int PATROL_MISSILE_MOVE_AMOUNT = 5;
  public static final int PATROL_MISSILE_AVAILABLE_Y = 0;
  public static int patrolMissileX =0;
  public static int patrolMissileY =0;
  
  // enemy missile
   public static final int ENEMY_MISSILE_LENGTH = 10;
  public static final int ENEMY_MISSILE_MOVE_AMOUNT = 5;
  public static final int ENEMY_MISSILE_AVAILABLE_Y = 1;
  public static int enemyMissileX;
  public static int enemyMissileY =0;
  public static boolean missilestart = false;
  // colors
  public static final Color BACKGROUND_COLOR = Color.WHITE;
  public static final Color ENEMY_COLOR = Color.RED;
  public static final Color ENEMY_DEAD_COLOR = Color.BLACK;
  public static final Color ENEMY_MISSILE_COLOR = Color.RED;
  public static final Color PATROL_COLOR = Color.GREEN;
  public static final Color PATROL_MISSILE_COLOR = Color.GREEN;
  public static final Color HEADING_COLOR = Color.BLACK;
  public static final Color MESSAGE_WIN_COLOR = Color.GREEN;
  public static final Color MESSAGE_LOSE_COLOR = Color.RED;
  public static final Color MESSAGE_SPACE_BAR_COLOR = Color.BLACK;
  
  // messages
  public static final String HEADING_MESSAGE = "Project3 by christopher salazar";
  public static final String START_MESSAGE = "Push Space Bar to Start";
  public static final String ENEMY_HIT_MESSAGE = "Enemy ship hit!";
  public static final String ENEMY_GOT_AWAY_MESSAGE = "Enemy ship got away!";
  public static final String ENEMY_WON_MESSAGE = "Enemy has destroyed your ship";
  
  // message positions
  public static final int HEADING_X = 10;
  public static final int HEADING_Y = 15;
  public static final int MESSAGE_X = 10;
  public static final int MESSAGE_Y = PANEL_HEIGHT - 10;
  
  public static boolean running = false;
  public static boolean hit = false;
  public static boolean enemyhit = false;
  
  //random section
  public static Random rand = new Random();
  public static int moveenemyamount;
  //array variable 
  public static final int PATROL_MISSILE_MAX = 5;
  public static int x = 0;
  public static int y = 0;
  public static int [] patrolMissilesX = new int[PATROL_MISSILE_MAX];
  public static int [] patrolMissilesY = new int[PATROL_MISSILE_MAX];
  
  
  
  // Main method for Project 3
  public static void main(String[] args) {
    DrawingPanel panel = new DrawingPanel(PANEL_WIDTH, PANEL_HEIGHT);
//    int [] patrolMissilesX = new int[PATROL_MISSILE_MAX];
//    int [] patrolMissilesY = new int[PATROL_MISSILE_MAX];
    Arrays.fill(patrolMissilesX,(-1));
    Arrays.fill(patrolMissilesY,(-1));
    for (int i =0; i<PATROL_MISSILE_MAX; i++){
    System.out.println(patrolMissilesX[i]);
    System.out.println(patrolMissilesY[i]) ;
    }
    
    
    Graphics g = panel.getGraphics( );
    restart(g);
    running = false;
    showMessage(g,START_MESSAGE,MESSAGE_SPACE_BAR_COLOR);
    startGame(panel, g);
  }
  
  // show a message at the bottom of the screen
  public static void showMessage(Graphics g, String message, Color c) {
    g.setColor(c);
    g.drawString(message,MESSAGE_X,MESSAGE_Y);
  }
  
  // show a message at the top of the screen
  public static void showHeading(Graphics g, String message, Color c) {
    g.setColor(c);
    g.drawString(message,HEADING_X,HEADING_Y);
  }
  
  // This method contains the main loop for the program
  // The loop runs forever, sleeping after each iteration
  public static void startGame(DrawingPanel panel, Graphics g) {
    drawPatrol(g, Color.green);
   
    while(true) {
//      moveEnemyShipAndDraw(g);
      handleKeys(panel, g);
      movePatrolMissilesAndDraw(g);
//      moveEnemyMissileAndDraw(g);
      if (detectHit())
        hit = true;
      if (hit)
        showMessage(g,ENEMY_HIT_MESSAGE,MESSAGE_WIN_COLOR);
      panel.sleep(SLEEP_TIME);
      if(detectenemyhit())
        enemyhit = true;
      if(enemyhit){
        showMessage(g,ENEMY_WON_MESSAGE,MESSAGE_LOSE_COLOR);
        drawPatrol(g,ENEMY_DEAD_COLOR);
      }
       x = getEmptyPatrolMissileIndex(patrolMissilesX);
       y = getEmptyPatrolMissileIndex(patrolMissilesY);
    } 
    }
  
  
  // redraw the screen and reset the game to the beginning
  public static void restart(Graphics g) {
    running = true;
    hit = false;
    enemyhit= false;
    g.setColor(BACKGROUND_COLOR);
    g.fillRect(0,0,PANEL_WIDTH,PANEL_HEIGHT);
    showHeading(g,HEADING_MESSAGE,HEADING_COLOR);
    patrolMissileY = PATROL_MISSILE_AVAILABLE_Y;
    enemyX = ENEMY_INITIAL_X;
    patrolX = PATROL_INITIAL_X;
    drawPatrol(g,PATROL_COLOR);
    enemyMissileX = enemyX + ENEMY_WIDTH/2;
    enemyMissileY = ENEMY_Y + ENEMY_MISSILE_LENGTH + 1;
    Arrays.fill(patrolMissilesX,(-1));
    Arrays.fill(patrolMissilesY,(-1));
  }
  
  // draw the patrol ship in a given color
  public static void drawPatrol(Graphics g, Color c) {
    g.setColor(c);
    g.fillRect(patrolX, PATROL_Y, PATROL_WIDTH, PATROL_HEIGHT);
  }
  
  // draw the enemy ship in a given color
  public static void drawEnemy(Graphics g, Color c) {
    g.setColor(c);
    g.fillRect(enemyX, ENEMY_Y, ENEMY_WIDTH, ENEMY_HEIGHT);
  }
   
  // move the enemy ship and redraw it, erasing the old position
  public static void moveEnemyShipAndDraw(Graphics g) {
    double num1 = rand.nextDouble();
   
    int num2 = rand.nextInt(2);
    if (hit) {
      drawEnemy(g,ENEMY_DEAD_COLOR);
      return;
    }
    if (running) {
      drawEnemy(g,BACKGROUND_COLOR);
      enemyX+=moveenemyamount;
    }
    if(num1 < .02){
      moveenemyamount = rand.nextInt(3)-1;
    } else if (enemyX <= 0){
      moveenemyamount = num2;
    }else if (enemyX >= PANEL_WIDTH - ENEMY_WIDTH-1){
     moveenemyamount = num2 +1;
    }
    drawEnemy(g,ENEMY_COLOR);
    if (enemyX > PANEL_WIDTH)
      moveenemyamount = num2 -1;
  }
  
  // handle the space bar and arrow keys
  public static void handleKeys(DrawingPanel panel, Graphics g) {
    int keyCode = panel.getKeyCode();
    if (keyCode == 0)
      return; 
    if (keyCode == ' '){
      restart(g);
      missilestart = true;
    }
    if (enemyhit) {
      drawPatrol(g, ENEMY_DEAD_COLOR);
      return;
    }
    if (keyCode == LEFT_ARROW)
      movePatrol(g,-PATROL_MOVE_AMOUNT);
    if (keyCode == RIGHT_ARROW)
      movePatrol(g,PATROL_MOVE_AMOUNT);
    if ((keyCode == UP_ARROW) && getEmptyPatrolMissileIndex(patrolMissilesX) !=-2) {
      x = getEmptyPatrolMissileIndex(patrolMissilesX);
      y = getEmptyPatrolMissileIndex(patrolMissilesY);
      
      patrolMissilesX[x] = patrolX + PATROL_WIDTH/2; 
      System.out.println(patrolX + PATROL_WIDTH/2);
      patrolMissilesY[y]  = PATROL_Y - PATROL_MISSILE_LENGTH - 1;
      System.out.println(PATROL_Y - PATROL_MISSILE_LENGTH - 1);
      
    }
  }
  
  // move the patrol ship by a given amount, erasing the ship at the old position
  public static void movePatrol(Graphics g, int delta) {
    if (enemyhit) {
      drawPatrol(g,ENEMY_DEAD_COLOR);
      return;
    }
    drawPatrol(g, BACKGROUND_COLOR);
    patrolX += delta;
    if (patrolX < -PATROL_WIDTH/2)
      patrolX = -PATROL_WIDTH/2;
    if (patrolX >= PANEL_WIDTH - PATROL_WIDTH/2)
      patrolX = PANEL_WIDTH - PATROL_WIDTH/2 - 1;
    drawPatrol(g, PATROL_COLOR);
  }
  
  // draw the missile with (x,y) at the top of the missile
  public static void drawMissile(Graphics g, int x, int y, Color c) {
    g.setColor(c);
    g.drawLine(x,y,x,y+PATROL_MISSILE_LENGTH);
  }
  
  // move the patrol missile, erasing the old one
  public static void movePatrolMissileAndDraw(Graphics g) {
    if(x > 0){
    if(getEmptyPatrolMissileIndex(patrolMissilesX)!=-2){
    drawMissile(g,patrolMissilesX[x],patrolMissilesY[y],PATROL_MISSILE_COLOR);
    patrolMissilesY[x] -= PATROL_MISSILE_MOVE_AMOUNT;
    g.setColor(PATROL_MISSILE_COLOR);
    if (patrolMissilesY[x] <= 0)
      patrolMissilesY[y] = PATROL_MISSILE_AVAILABLE_Y;
    else{
      drawMissile(g,patrolMissileX,patrolMissileY,PATROL_MISSILE_COLOR);}
    }
    }
  }
   // draw the enemy missile with (x,y) at the top of the missile
  public static void drawEnemyMissile(Graphics g, int x, int y, Color c) {
    g.setColor(c);
    g.drawLine(x,y,x,y+ENEMY_MISSILE_LENGTH);
    }  
      
  
  
  // move the enemy missile, erasing the old one
  public static void moveEnemyMissileAndDraw(Graphics g ) {
    if(hit || enemyhit){
      drawEnemyMissile(g,enemyMissileX, enemyMissileY, BACKGROUND_COLOR);
      enemyMissileY = 200;
      return;}
    if(missilestart){
    if (enemyMissileY >= PANEL_HEIGHT){
         enemyMissileX = enemyX + ENEMY_WIDTH/2;
         enemyMissileY = ENEMY_Y + ENEMY_MISSILE_LENGTH;}
    drawEnemyMissile(g,enemyMissileX,enemyMissileY,BACKGROUND_COLOR);
    enemyMissileY += ENEMY_MISSILE_MOVE_AMOUNT;
    g.setColor(ENEMY_MISSILE_COLOR);
 
      drawEnemyMissile(g,enemyMissileX,enemyMissileY,ENEMY_MISSILE_COLOR);
      
  }
  }
  // return true if the missile intersects the enemy ship
  public static boolean detectHit() {
    for(int i=0; i< 5; i++){
      if(patrolMissilesX[i] >= enemyX && patrolMissilesX[i] <= enemyX + ENEMY_WIDTH &&
         patrolMissilesY[i] >= ENEMY_Y && patrolMissilesY[i] <= ENEMY_Y + ENEMY_HEIGHT)
      return true;
    }
    return false;
  }
  // returns true if the missile intersects the patrol ship
  public static boolean detectenemyhit() {
   // for(int i =0; i<5; i++){
      return enemyMissileX >= patrolX && enemyMissileX <= patrolX + PATROL_WIDTH &&
         enemyMissileY >= PATROL_Y && enemyMissileY <= PATROL_Y + PATROL_HEIGHT;
    //}
  }
  public static int getEmptyPatrolMissileIndex(int[] array){
  int location =0;
  for (int i=0; i<5; i++){
	  System.out.println(array[i] + " "+i+" getempty");
  if(array[i] ==(-1))
    return i;
  }
  return -2;
  }
  public static void movePatrolMissilesAndDraw(Graphics g){
  x = getEmptyPatrolMissileIndex(patrolMissilesX);
  y = getEmptyPatrolMissileIndex(patrolMissilesY);  
  movePatrolMissileAndDraw(g);
if(x > PATROL_MISSILE_MAX || y > PATROL_MISSILE_MAX || x<0 || y<0 )
	return;
else{
 // patrolMissilesX[x] = patrolMissileX;
 // System.out.println(patrolMissileX);
//  patrolMissilesY[y] = patrolMissileY;
 // System.out.println(patrolMissileY);
}
    }
}