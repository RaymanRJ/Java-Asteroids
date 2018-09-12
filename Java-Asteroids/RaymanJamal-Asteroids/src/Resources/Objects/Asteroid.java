package Resources.Objects;

import Resources.States.SpriteState;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.Collections;

public class Asteroid extends Sprite{

    private int[] positionRanges;
    private int[] quad1X, quad2X, quad3X, quad4X;
    private int[] quad1Y, quad2Y, quad3Y, quad4Y;
    private ArrayList<Boolean> usablePoints;
    private int size;
    
    // --- Constructors --- //
    public Asteroid(Difficulty difficulty){
        DIFFICULTY = difficulty;
        
        // --- HitCounter --- //
        switch (DIFFICULTY){
            case EASY:
                lifeCounter = 1;
                size = 1;
                break;
            case MEDIUM:
                lifeCounter = 2;
                size = 2;
                break;
            case HARD:
                lifeCounter = 3;
                size = 3;
                break;
        }
        
        initializeAsteroid();
    }
    public Asteroid(int x, int y, int size, int lifeCounter){
        this.size = size-1;
        initializeAsteroid();
        this.lifeCounter = lifeCounter;
        xPos = x;
        yPos = y;
    }
    public int getSize(){return this.size;}
    private void initializeAsteroid(){
        
        STATE = SpriteState.ALIVE;
        
        // --- Create Shape & Set Points
        shape = new Polygon();
        drawShape = new Polygon();
        
        createPoints();
        
        // --- Angle & Rotation --- //
        ROTATION = r.nextDouble();
        while(ROTATION == 0 || ROTATION > 0.3){
            ROTATION = r.nextDouble();
        }
        if(!r.nextBoolean()){
            ROTATION = -ROTATION;
        }
        angle = 0;
        
        // --- Speed --- //
        double h, a;
        h = r.nextDouble() + 1;
        a = r.nextDouble() * 2 * Math.PI;
        xSpeed = Math.cos(a)*h;
        ySpeed = Math.sin(a)*h;
        
        // --- Position --- //
        h = r.nextDouble()*400 + 100;
        a = r.nextDouble()*2*Math.PI;
        xPos = Math.cos(a)*h + 450;
        yPos = Math.sin(a)*h + 300;
    }
    
    @Override
    public void hit(){
        lifeCounter--;
        STATE = SpriteState.DISABLED;
        if(lifeCounter == 0)STATE = SpriteState.DEAD;
    }
    // --- Designing Asteroids --- //
    private void createPoints(){
        // -- Possible points to use to generate asteroids -- //
        positionRanges = new int[]{-16,-8,-4,-2,-1,0,1,2,4,8,16};
        quad1X = new int[]{10, 15, 15};
        quad1Y = new int[]{-15, -15, -10};
        quad2X = new int[]{-15, -15, -10};
        quad2Y = new int[]{-10, -15, -15};
        quad3X = new int[]{-10, -15, -15};
        quad3Y = new int[]{15, 15, 10};
        quad4X = new int[]{15, 15, 10};
        quad4Y = new int[]{10, 15, 15};
//        quad1X = new int[]{25, 45, 50};
//        quad1Y = new int[]{-50, -45, -25};
//        quad2X = new int[]{-50, -45, -25};
//        quad2Y = new int[]{-25, -45, -50};
//        quad3X = new int[]{-50, -45, -20};
//        quad3Y = new int[]{20, 45, 50};
//        quad4X = new int[]{25, 45, 50};
//        quad4Y = new int[]{50, 45, 25};
        usablePoints = new ArrayList();
        
        // --- Quadrant Points --- //
        addPoints(quad1X, quad1Y);
        addPoints(quad4X, quad4Y);
        addPoints(quad3X, quad3Y);
        addPoints(quad2X, quad2Y);
    }
    private void addPoints(int[]x, int[]y){
        randomizeBoolList();
        for(int i = 0; i < usablePoints.size(); i++){            
            if(usablePoints.get(i)){
                int pointModifer = r.nextInt(positionRanges.length-1);
                shape.addPoint(x[i]*size + positionRanges[pointModifer],
                        y[i]*size + positionRanges[pointModifer]);
                
                pointModifer = r.nextInt(positionRanges.length-1);
                drawShape.addPoint(x[i]*size + positionRanges[pointModifer],
                        y[i]*size + positionRanges[pointModifer]);
            }
        }
    }
    private void randomizeBoolList(){
        usablePoints.clear();
        usablePoints.add(true);
        for(int i = 0; i < quad1X.length-1; i++){
            usablePoints.add(r.nextBoolean());
        }
        Collections.shuffle(usablePoints);
    }
    public void updatePosition(){
        angle += ROTATION;
        super.updatePosition();
    }
    public void setState(SpriteState state){this.STATE = state;}
}