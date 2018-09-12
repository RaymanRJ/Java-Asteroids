import Resources.Objects.*;
import Resources.States.*;
import java.applet.Applet;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import javax.swing.Timer;

/**
 *
 * @author Rayman Jamal
 */
public class Asteroids extends Applet implements ActionListener, KeyListener{

    GameState GAMESTATE;
    Difficulty DIFFICULTY;
    
    SpaceCraft ship;
    Laser laser;
    int bulletCount;
    int maxBullets;
    
    ArrayList<Asteroid> asteroidList;
    ArrayList<Bullet> bulletList;
    ArrayList<Debris> debrisList;
    ArrayList<PUPs> PUPList;
    ArrayList<Bullet> laserBeam;
    
    Image offscreen;
    Graphics offg;
    
    // --- Timers --- //
    Timer timer; // -- Controls Refresh
    GameTimer gameTimer, PUPRespawnTimer, PUPTimer, bulletTimer;
    int minutes, seconds;
    
    Font titleFont;
    Font instructionFont;
    Font difficultyText;
    
    boolean eKey, mKey, hKey; // Difficulty Selectors
    boolean leftKey, upKey, rightKey; // Ship Direction Controls
    boolean spaceKey, reloadKey, powerUpKey; // Ship Controls
    boolean restartKey; // Restart Game
    
    public void init() {
        this.setSize(900, 600);
        this.addKeyListener(this);
        
        offscreen = createImage(this.getWidth(), this.getHeight());
        offg = offscreen.getGraphics();
        
        GAMESTATE = GameState.INTRODUCTION;
        timer = new Timer(20, this);
        
        titleFont = new Font("Agency FB", Font.BOLD, 30);
        instructionFont = new Font("Agency FB", Font.PLAIN, 20);
        difficultyText = new Font("Agency FB", Font.PLAIN, 25);
        
        eKey = false; mKey = false; hKey = false;
        leftKey = false; upKey = false; rightKey = false;
        spaceKey = false; reloadKey = false; powerUpKey = false;
        restartKey = false;
        
        ship = new SpaceCraft(450, 300);
    }
    
    private void startGame(){
        // --- Set GAMESTATE --- //
        GAMESTATE = GameState.PLAYING;
        
        // --- Create Ship --- //
        ship = new SpaceCraft(DIFFICULTY);
        laser = new Laser(ship.getXPos(), ship.getYPos(), ship.getAngle());
        
        // --- Create Lists --- //
        asteroidList = new ArrayList();
        bulletList = new ArrayList();
        debrisList = new ArrayList();
        PUPList = new ArrayList(); // Only 2 PowerUps allowed at a time.
        
       // --- Bullets & Asteroids --- //
        int numOfAsteroids = 0;
        
        switch (DIFFICULTY){
            case EASY:
                maxBullets = 50;
                numOfAsteroids = getNum(3, 7);
                break;
            case MEDIUM:
                maxBullets = 35;
                numOfAsteroids = getNum(5, 10);
                break;
            case HARD:
                maxBullets = 25;
                numOfAsteroids = getNum(7, 12);
                break;
        }
        
        bulletCount = maxBullets;
        
        for(int i = 0; i < numOfAsteroids; i++){
            asteroidList.add(new Asteroid(DIFFICULTY));
        }
        
         // --- Timers --- //
        gameTimer = new GameTimer();
        PUPRespawnTimer = new GameTimer();
        PUPTimer = new GameTimer();
        bulletTimer = new GameTimer();
        minutes = 0;
        seconds = 0;
        
        // --- Boolean Checks --- //
        eKey = false; mKey = false; hKey = false;
        leftKey = false; upKey = false; rightKey = false;
        spaceKey = false; reloadKey = false; powerUpKey = false;
        restartKey = false;
    }
    private int getNum(int min, int max){
        double toReturn = Math.random()*max+1;
        while(toReturn < min || toReturn > max){
            toReturn = Math.random()*max+1;
        }
        return (int)toReturn;
    }
    public void paint(Graphics g){
        offg.setColor(Color.BLACK);
        offg.fillRect(0, 0, 900, 600);
        
        if(GAMESTATE == GameState.INTRODUCTION){
            // --- INTRO SCREEN --- //
            
            offg.setFont(titleFont);
            offg.setColor(Color.RED);
            offg.drawString("A  S  T  E  R  O  I  D  S", 350, 50);
            
            // Border - (200, 100) to (700, 500)
            offg.drawRect(200, 100, 500, 400);
            
            // Instructions
            offg.setFont(instructionFont);
            offg.drawString("YOU KNOW HOW THIS WORKS. DESTROY THE ASTEROIDS.", 310, 200);
            offg.drawString("      UP                                   ACCELERATE", 330, 400);
            offg.drawString("LEFT/RIGHT                                 ROTATE", 330, 425);
            offg.drawString("    SPACE                                    SHOOT", 330, 450);
            
            // Ship
            ship.paint(offg);  
            
            // Difficulty Selection
            offg.drawString("E - EASY                    M - MEDIUM                    H - HARD", 275, 550);
            
            // --- END OF INTRO --- //
        } else if (GAMESTATE == GameState.PLAYING){
            // --- GAME SCREEN --- //
            
            // ------ Paint Ship:
            if(ship.getState() == SpriteState.ALIVE){
                offg.setColor(Color.RED);
                ship.paint(offg);
            }
            
            // ------ Paint Asteroids:
            paintAsteroids();
            
            // ------ Paint Bullets
            paintBullets();
            
            // ------ Paint Debris
            paintDebris();
            
            // ------ Paint PowerUps
            paintPUPList();
            paintActivePUP();
            
            // ------ Draw Text:
            offg.setColor(Color.RED);
            offg.drawString("Lives Left: " + ship.getLifeCounter(), 25, 30);
            offg.drawString("Asteroids Left: " + asteroidList.size(), 25, 60);
            offg.drawString("Bullets Left: " + bulletCount, 25, 90);
            if(ship.isPUPActive()){
                offg.drawString("Current Power Up: " + ship.getCurrentPUP(), 25, 120);
            }
            if(bulletCount == 0 && bulletTimer.getTimer() < 100){
                offg.drawString("Reload Time Remaining: " + (100 - bulletTimer.getTimer()) + "ms", 25, 150);
            } else if(bulletCount == 0 && bulletTimer.getTimer() >= 100){
                offg.drawString("Reload!", 25, 150);
            }
            if(seconds < 10){
                offg.drawString(minutes + ":0" + seconds, 850, 30);
            } else{
                offg.drawString(minutes + ":" + seconds, 850, 30);
            }
            
        } else if (GAMESTATE == GameState.ENDGAME){
            offg.setColor(Color.RED);
            offg.drawString("GAME OVER!", 400, 200);
            
            if(playerWon()){
                offg.setColor(Color.YELLOW);
                offg.drawString("YOU WON.", 415, 250);
                offg.drawString("Congratulations. You did it.", 375, 300);
            } else{
                offg.setColor(Color.DARK_GRAY);
                offg.drawString("YOU DIED.", 400, 250);
                offg.drawString("Redeem Yourself.", 400, 300);
            }
            offg.setColor(Color.RED);
            offg.drawString("Press 'R' To Play Again. Press 'Q' To Quit.", 325, 350);
        }
        
        g.drawImage(offscreen, 0, 0, this);
    }
    private boolean playerWon(){
        return ship.getLifeCounter() > 0;
    }
    private void paintActivePUP(){
        if(ship.isPUPActive() && ship.getState() == SpriteState.ALIVE){
            if(ship.getCurrentPUP() == PUPChoice.LASER){
                if(gameTimer.getTimer()%2 == 0){
                    offg.setColor(Color.ORANGE);
                } else{
                    offg.setColor(Color.RED);
                }
                laser.paint(offg);
            } else if(ship.getCurrentPUP() == PUPChoice.SHIELD){
                // PRINT SHIELD
            } else if (ship.getCurrentPUP() == PUPChoice.NUKE){
                // PRINT NUKE
            }
        }
    }
    private void paintPUPList(){
        for(PUPs p : PUPList){
            offg.setColor(Color.WHITE);
            p.paint(offg);
        }
    }
    private void paintDebris(){
        for(Debris d : debrisList){
            offg.setColor(new Color((int)(Math.random()*255), (int)(Math.random()*255), (int)(Math.random()*255)));
            d.paint(offg);
        }
    }
    private void paintBullets(){
        for(Bullet b : bulletList){
            offg.setColor(Color.WHITE);
            b.paint(offg);
        }
    }
    private void paintAsteroids(){
        for(Asteroid ast : asteroidList){
            switch (ast.getLifeCounter()) {
                case 3:
                    offg.setColor(Color.GREEN);
                    break;
                case 2:
                    offg.setColor(Color.YELLOW);
                    break;
                default:
                    offg.setColor(Color.RED);
                    break;
            }
        ast.paint(offg);
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        keyCheck();
        
        if(GAMESTATE == GameState.INTRODUCTION){
            ship.updatePosition();
        }else if(GAMESTATE == GameState.PLAYING){
            // --- Update Timer --- //
            updateTimers();
            
            // --- Update Positions --- //
            respawnShip();
            ship.updatePosition();
            if(ship.isPUPActive()){
                laser.updatePosition(ship.drawShape.xpoints[5],
                        ship.drawShape.ypoints[5], ship.getXSpeed(),
                        ship.getYSpeed(), ship.getAngle());
            }
            
            for(Asteroid a : asteroidList){
                a.updatePosition();
            }
            
            for(Bullet b : bulletList){
                b.updatePosition();
            }
            
            for(Debris d : debrisList){
                d.updatePosition();
            }
            
            for(PUPs p : PUPList){
                p.updatePosition();
            }
            
            // --- Collisions --- //
            checkCollisions();
            checkAsteroids();
            checkBullets();
            checkPUPs();
            removeDeadSprites();
            
            respawnPUPs();
            
            checkGameState();
        } else if(GAMESTATE == GameState.ENDGAME){
            
        }
        repaint();
    }
    private void checkGameState(){
        if(ship.getLifeCounter() == 0 || asteroidList.isEmpty()){
            GAMESTATE = GameState.ENDGAME;
        }
    }
    private void checkBullets(){
        if(bulletCount > 0){
            bulletTimer.resetTimer();
        }
    }
    private void updateTimers(){
        gameTimer.updateTimer();
        PUPRespawnTimer.updateTimer();
        bulletTimer.updateTimer();
        if(ship.isPUPActive()){
            PUPTimer.updateTimer();
        }
        minutes = gameTimer.getTimer()/50/60;
        seconds = (gameTimer.getTimer()/50)%60;
    }
    private void checkPUPs(){
        for(int i = 0; i < PUPList.size(); i++){
            if(PUPList.get(i).getState() == SpriteState.DISABLED){
                ship.addPUP(PUPList.get(i).getPUPChoice());
                PUPTimer.resetTimer();
                PUPList.get(i).hit(); // Sets State to DEAD.
            }
        }
        
        if(ship.isPUPActive() && PUPTimer.getTimer() > 500){
            if(ship.getCurrentPUP() == PUPChoice.LASER){
                if(PUPTimer.getTimer() > 1000){
                    ship.removePUP();
                }
            }else{
                ship.removePUP();
            }
        }
    }
    
    private void resetGameTimer(GameTimer timer){timer.resetTimer();}
    
    private void fireBullets(){
        if(ship.getState() == SpriteState.ALIVE && ship.getCounter() >= 5 && !ship.isPUPActive() && bulletCount > 0){
            bulletList.add(new Bullet(ship.getXPos(), ship.getYPos(), ship.getAngle()));
            ship.setCounter(0);
            bulletCount--;
        }
    }
    private void reloadBullets(){
        if(bulletCount == 0 && bulletTimer.getTimer() > 100){
            bulletCount = maxBullets;
        }
    }
    private void usePowerUp(){
        
    }

    // --- Sprite Updates --- //
    private void respawnShip(){
        if(ship.getState() == SpriteState.DEAD && ship.getCounter() > 50 && isRespawnShip() && ship.getLifeCounter() > 0){
            ship.reset();
        }
    }
    private boolean isRespawnShip(){
        for(Asteroid ast : asteroidList){
            double x = Math.pow(ast.getXPos() - 450, 2);
            double y = Math.pow(ast.getYPos() - 300, 2);
            double h = Math.sqrt(x + y);
            
            if(h < 100){return false;}
        }
        return true;
    }
    private void removeDeadSprites(){
        for(int i = 0; i < asteroidList.size(); i++){
            if(asteroidList.get(i).getState() == SpriteState.DEAD){
                asteroidList.remove(i);
            }
        }
        
        for(int i = 0; i < bulletList.size(); i++){
            if(bulletList.get(i).getState() == SpriteState.DEAD
                    || bulletList.get(i).getCounter() >= 50){
                bulletList.remove(i);
            }
        }
        
        for(int i = 0; i < debrisList.size(); i++){
            if(debrisList.get(i).getCounter() > 50){
                debrisList.remove(i);
            }
        }
        
        for(int i = 0; i < PUPList.size(); i++){
            if(PUPList.get(i).getCounter() > 500 ||
                    PUPList.get(i).getState() == SpriteState.DEAD){
                PUPList.remove(i);
            }
        }
    }
    
    // --- Collision Functions --- //
    private void checkCollisions(){
        for(Asteroid ast : asteroidList){
            if(collision(ship, ast)){
                ship.hit();
                ast.hit();
                createDebris(ship);
            }
            for(Bullet b : bulletList){
                if(collision(ast, b)){
                    b.hit();
                    ast.hit();
                    createDebris(ast);
                }
            }
            if(collision(ast, laser) && ship.isPUPActive() && ship.getCurrentPUP() == PUPChoice.LASER){
                ast.hit();
                createDebris(ast);
            }
        }
        for(PUPs p : PUPList){
            if(collision(ship, p)){
                p.hit(SpriteState.DISABLED);
                createDebris(p);
            }
            for(Bullet b : bulletList){
                if(collision(b, p)){
                    p.hit();
                    createDebris(p);
                }
            }
        }
    }
    private void checkAsteroids(){
        for(int i = 0; i < asteroidList.size(); i++){
            if(asteroidList.get(i).getState() == SpriteState.DISABLED){
                double numOfAst = Math.random()*3+1;
                for(int j = 0; j < numOfAst; j++){
                    if(asteroidList.get(i).getLifeCounter() >= 1){
                        int x = (int)asteroidList.get(i).getXPos();
                        int y = (int)asteroidList.get(i).getYPos();
                        int s = asteroidList.get(i).getSize();
                        int l = asteroidList.get(i).getLifeCounter();
                        asteroidList.add(new Asteroid(x, y, s, l));
                    }
                }
                asteroidList.get(i).setState(SpriteState.ALIVE);
            }
        }
    }
    private void createDebris(Sprite sprite){
        double debrisCount = Math.random()*6+1;
        for(int i = 0; i < debrisCount; i++){
            debrisList.add(new Debris(sprite.getXPos(), sprite.getYPos()));
        }
    }
    private boolean collision(ArrayList<Sprite> things1, ArrayList<Sprite> things2){
        for(Sprite thing1 : things1){
            for(Sprite thing2 : things2){
                if(collision(thing1, thing2)){
                    return true;
                }
            }
        }
        return false;
    }
    private boolean collision(Sprite thing1, ArrayList<Sprite> things2){
        for(Sprite thing2 : things2){
            if(collision(thing1, thing2)){
                return true;
            }
        }
        return false;
    }
    private boolean collision(Sprite thing1, Sprite thing2){
        for(int i = 0; i < thing1.drawShape.npoints; i++){
            
            int x1 = thing1.drawShape.xpoints[i];
            int y1 = thing1.drawShape.ypoints[i];
            
            for(int j = 0; j < thing2.drawShape.npoints; j++){
                
                if(thing2.drawShape.contains(x1, y1)){
                    if(thing1.getState() == SpriteState.ALIVE && thing2.getState() == SpriteState.ALIVE){
                        return true;
                    }
                }
            }
        }
        
        for(int i = 0; i < thing2.drawShape.npoints; i++){
            
            int x1 = thing2.drawShape.xpoints[i];
            int y1 = thing2.drawShape.ypoints[i];
            
            for(int j = 0; j < thing1.drawShape.npoints; j++){
                
                if(thing1.drawShape.contains(x1, y1)){
                    if(thing1.getState() == SpriteState.ALIVE && thing2.getState() == SpriteState.ALIVE){
                        return true;
                    }
                }
            }
        }
        
        return false;
    }
    
    // --- Key Methods --- //
    private void keyCheck(){
        switch(GAMESTATE){
            case INTRODUCTION:
                if(eKey) DIFFICULTY = Difficulty.EASY;
                if(mKey) DIFFICULTY = Difficulty.MEDIUM;
                if(hKey) DIFFICULTY = Difficulty.HARD;
                if(eKey || mKey || hKey) startGame();
                break;
            case PLAYING:
                if(ship.getState() == SpriteState.ALIVE){
                    if(upKey) ship.accelerate();
                    if(leftKey) ship.rotateLeft();
                    if(rightKey) ship.rotateRight();
                    if(spaceKey) fireBullets();
                    if(reloadKey) reloadBullets();
                    if(powerUpKey) usePowerUp();
                }
                break;
            case ENDGAME:
                if(restartKey) init();
                break;
        }
    }
    @Override
    public void keyPressed(KeyEvent ke) {
        switch(GAMESTATE){
            case INTRODUCTION:
                switch(ke.getKeyCode()){
                    case KeyEvent.VK_E:
                        eKey = true;
                        break;
                    case KeyEvent.VK_M:
                        mKey = true;
                        break;
                    case KeyEvent.VK_H:
                        hKey = true;
                        break;
                }
                break;
            case PLAYING:
                switch(ke.getKeyCode()){
                    case KeyEvent.VK_LEFT:
                        leftKey = true;
                        break;
                    case KeyEvent.VK_RIGHT:
                        rightKey = true;
                        break;
                    case KeyEvent.VK_UP:
                        upKey = true;
                        break;
                    case KeyEvent.VK_SPACE:
                        spaceKey = true;
                        break;
                    case KeyEvent.VK_R:
                        reloadKey = true;
                        break;
                    case KeyEvent.VK_E:
                        powerUpKey = true;
                        break;
                }
                break;
            case ENDGAME:
                switch (ke.getKeyCode()){
                    case KeyEvent.VK_R:
                        restartKey = true;
                        break;
                }
            break;     
        }
    }
    @Override
    public void keyReleased(KeyEvent ke) {
    
        switch(GAMESTATE){
            case INTRODUCTION:
                switch(ke.getKeyCode()){
                    case KeyEvent.VK_E:
                        eKey = false;
                        break;
                    case KeyEvent.VK_M:
                        mKey = false;
                        break;
                    case KeyEvent.VK_H:
                        hKey = false;
                        break;
                }
                break;
            case PLAYING:
                switch(ke.getKeyCode()){
                    case KeyEvent.VK_LEFT:
                        leftKey = false;
                        break;
                    case KeyEvent.VK_RIGHT:
                        rightKey = false;
                        break;
                    case KeyEvent.VK_UP:
                        upKey = false;
                        break;
                    case KeyEvent.VK_SPACE:
                        spaceKey = false;
                        break;
                    case KeyEvent.VK_R:
                        reloadKey = false;
                        break;
                    case KeyEvent.VK_E:
                        powerUpKey = false;
                        break;
                }
                break;
            case ENDGAME:
                switch (ke.getKeyCode()){
                    case KeyEvent.VK_R:
                        restartKey = false;
                        break;
                }
            break;
        }
    }
    
    // --- Mich. Methods --- //
    public void start(){timer.start();}
    public void stop(){timer.stop();}
    public void update(Graphics g){paint(g);}
    @Override
    public void keyTyped(KeyEvent ke) {}
    private void respawnPUPs() {
        /*
            There is a 1/4 chance of spawning a PUP,
            30 seconds after the last one, and 30 seconds after gameStart.
        */
        if(PUPList.size() < 3 && gameTimer.getTimer() > 1500 && PUPRespawnTimer.getTimer() > 1500){
            if(new Random().nextBoolean()){
                if(new Random().nextBoolean()){
                    PUPList.add(new PUPs());
                    resetGameTimer(PUPRespawnTimer);
                }
            }
        }
    }
}