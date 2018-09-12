package Resources.Objects;

import Resources.States.PUPChoice;
import Resources.States.SpriteState;
import java.awt.Polygon;

public class PUPs extends Sprite{
    
    PUPChoice powerUp;
    
    public PUPs(){
        STATE = SpriteState.ALIVE;
        powerUp = PUPChoice.choose();
        powerUp = PUPChoice.LASER;
        
        shape = new Polygon();
        drawShape = new Polygon();
        setPoints();
        
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
        
        counter = 0;
        ROTATION = Math.random();
        while(ROTATION == 0 || ROTATION > 0.25){ROTATION = Math.random();}
        lifeCounter = 1;
    }
    
    public void updatePosition(){
        angle += ROTATION;
        super.updatePosition();
    }
    
    private void setPoints(){
        if(powerUp == PUPChoice.HEALTH){
            shape.addPoint(10, 20);
            shape.addPoint(5, 20);
            shape.addPoint(5, 5);
            shape.addPoint(-5, 5);
            shape.addPoint(-5, 20);
            shape.addPoint(-10, 20);
            shape.addPoint(-10, -20);
            shape.addPoint(-5, -20);
            shape.addPoint(-5, -5);
            shape.addPoint(5, -5);
            shape.addPoint(5, -20);
            shape.addPoint(10, -20);
            
            drawShape.addPoint(10, 20);
            drawShape.addPoint(5, 20);
            drawShape.addPoint(5, 5);
            drawShape.addPoint(-5, 5);
            drawShape.addPoint(-5, 20);
            drawShape.addPoint(-10, 20);
            drawShape.addPoint(-10, -20);
            drawShape.addPoint(-5, -20);
            drawShape.addPoint(-5, -5);
            drawShape.addPoint(5, -5);
            drawShape.addPoint(5, -20);
            drawShape.addPoint(10, -20);
            
        } else if(powerUp == PUPChoice.LASER){
            shape.addPoint(5, 20);
            shape.addPoint(-5, 20);
            shape.addPoint(-2, 15);
            shape.addPoint(-5, 10);
            shape.addPoint(-2, 5);
            shape.addPoint(-5, 0);
            shape.addPoint(-2, -5);
            shape.addPoint(-5, -10);
            shape.addPoint(0, -20);
            shape.addPoint(5, -10);
            shape.addPoint(2, -5);
            shape.addPoint(5, 0);
            shape.addPoint(2, 5);
            shape.addPoint(5, 10);
            shape.addPoint(2, 15);
            
            drawShape.addPoint(5, 20);
            drawShape.addPoint(-5, 20);
            drawShape.addPoint(-2, 15);
            drawShape.addPoint(-5, 10);
            drawShape.addPoint(-2, 5);
            drawShape.addPoint(-5, 0);
            drawShape.addPoint(-2, -5);
            drawShape.addPoint(-5, -10);
            drawShape.addPoint(0, -20);
            drawShape.addPoint(5, -10);
            drawShape.addPoint(2, -5);
            drawShape.addPoint(5, 0);
            drawShape.addPoint(2, 5);
            drawShape.addPoint(5, 10);
            drawShape.addPoint(2, 15);   
        } else if (powerUp == PUPChoice.SHIELD){
            shape.addPoint(10, 10);
            shape.addPoint(-10, 10);
            shape.addPoint(-10, -10);
            shape.addPoint(10, -10);
            
            drawShape.addPoint(10, 10);
            drawShape.addPoint(-10, 10);
            drawShape.addPoint(-10, -10);
            drawShape.addPoint(10, -10);
        } else if (powerUp == PUPChoice.NUKE){
            shape.addPoint(0, -25);
            shape.addPoint(15, 0);
            shape.addPoint(5, 20);
            shape.addPoint(15, 25);
            shape.addPoint(-15, 25);
            shape.addPoint(-5, 20);
            shape.addPoint(-15, 0);
            
            drawShape.addPoint(0, -25);
            drawShape.addPoint(15, 0);
            drawShape.addPoint(5, 20);
            drawShape.addPoint(15, 25);
            drawShape.addPoint(-15, 25);
            drawShape.addPoint(-5, 20);
            drawShape.addPoint(-15, 0);
        }
    }
    
    @Override
    public void hit() {
        lifeCounter = 0;
        STATE = SpriteState.DEAD;
    }
    
    public void hit(SpriteState state){
        lifeCounter = 0;
        STATE = state;
    }
    
    public PUPChoice getPUPChoice(){
        return powerUp;
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