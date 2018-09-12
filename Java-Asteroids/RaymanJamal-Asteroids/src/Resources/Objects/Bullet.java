package Resources.Objects;

import Resources.States.SpriteState;
import java.awt.Polygon;

public class Bullet extends Sprite{
    
    public Bullet(double x, double y, double a){
        STATE = SpriteState.ALIVE;
        
        shape = new Polygon();
        drawShape = new Polygon();
        setPoints();
        
        xPos = x;
        yPos = y;
        
        
        THRUST = 10;
        angle = a;
        
        this.xSpeed = Math.cos(angle - Math.PI/2) * THRUST;
        this.ySpeed = Math.sin(angle - Math.PI/2) * THRUST;
        
        counter = 0;
        ROTATION = 0;
        lifeCounter = 1;
    }
    
    private void setPoints(){
        
        shape.addPoint(-2, 3);
        shape.addPoint(-2, -2);
        shape.addPoint(0, -3);
        shape.addPoint(2, -2);
        shape.addPoint(2, 3);
        
        drawShape.addPoint(-2, 3);
        drawShape.addPoint(-2, -2);
        drawShape.addPoint(0, -3);
        drawShape.addPoint(2, -2);
        drawShape.addPoint(2, 3);
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