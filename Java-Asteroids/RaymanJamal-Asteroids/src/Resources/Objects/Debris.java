package Resources.Objects;

import Resources.States.SpriteState;
import java.awt.Polygon;

public class Debris extends Sprite{
    
    public Debris(double x, double y){
        STATE = SpriteState.ALIVE;
        
        shape = new Polygon();
        drawShape = new Polygon();
        setPoints();
        
        xPos = x;
        yPos = y;
        
        
        THRUST = r.nextInt(1, 10);
        angle = Math.random() * 2 * Math.PI;
        
        this.xSpeed = Math.cos(angle - Math.PI/2) * THRUST;
        this.ySpeed = Math.sin(angle - Math.PI/2) * THRUST;
        
        counter = 0;
        ROTATION = 0;
        lifeCounter = 1;
    }
    
    private void setPoints(){
        
        int size = r.nextInt(1,5);
        shape.addPoint(-1*size, 0);
        drawShape.addPoint(-1*size, 0);
        
        size = r.nextInt(1,5);
        shape.addPoint(0, -1*size);
        drawShape.addPoint(0, -1*size);
        
        size = r.nextInt(1,5);
        shape.addPoint(1*size, 0);
        drawShape.addPoint(1*size, 0);
        
        size = r.nextInt(1,5);
        shape.addPoint(0, 1*size);
        drawShape.addPoint(0, 1*size);
    }
    

    @Override
    public void hit() {
        lifeCounter = 0;
        STATE = SpriteState.DEAD;
    }
    
}


/*
    protected SpriteState STATE;
    protected Difficulty DIFFICULTY;

    protected Polygon shape;
    public Polygon drawShape;
    
    protected double xPos, yPos;
    protected double xSpeed, ySpeed;
    protected int counter;
    protected double angle;
    protected double THRUST;
    protected double ROTATION;
    protected int lifeCounter;
    
    protected Random r;

*/