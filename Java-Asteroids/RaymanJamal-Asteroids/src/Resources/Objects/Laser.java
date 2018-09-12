package Resources.Objects;

import Resources.States.SpriteState;
import java.awt.Polygon;

public class Laser extends Sprite{
    
    public Laser(double x, double y, double a){
        STATE = SpriteState.ALIVE;
        
        shape = new Polygon();
        drawShape = new Polygon();
        setPoints();
        
        xPos = x;
        yPos = y;
        
        THRUST = 1;
        angle = a;
        
        this.xSpeed = Math.cos(angle - Math.PI/2) * THRUST;
        this.ySpeed = Math.sin(angle - Math.PI/2) * THRUST;
        
        counter = 0;
        ROTATION = 0;
        lifeCounter = 1;
    }
    
    private void setPoints(){
        
        shape.addPoint(0, 0);
        shape.addPoint(-25, -150);
        shape.addPoint(25, -150);
        
        drawShape.addPoint(0, 0);
        drawShape.addPoint(-25, -150);
        drawShape.addPoint(25, -150);
    }
    
    public void updatePosition(double xPos, double yPos, double xSpeed, double ySpeed, double angle){
        this.angle = angle;
        this.xPos = xPos;
        this.yPos = yPos;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
        super.updatePosition();
    }
    
    @Override
    public void hit() {
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