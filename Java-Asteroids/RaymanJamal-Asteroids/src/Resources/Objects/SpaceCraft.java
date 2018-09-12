package Resources.Objects;

import Resources.States.PUPChoice;
import Resources.States.SpriteState;
import java.awt.Polygon;

public class SpaceCraft extends Sprite {
    
    boolean PUPActive;
    PUPChoice currentPUP;
    
    // --- Constructors --- //
    public SpaceCraft(Difficulty difficulty){
        initializeShip();
        DIFFICULTY = difficulty;
        
        switch (DIFFICULTY){
            case EASY:
                lifeCounter = 20;
                break;
            case MEDIUM:
                lifeCounter = 10;
                break;
            case HARD:
                lifeCounter = 5;
                break;
        }
    }
    public SpaceCraft(int x, int y){
        initializeShip();
        STATE = SpriteState.DISABLED;
        xPos = x;
        yPos = y;
    }
    private void addLives(){lifeCounter += 2;}
    public void reset(){
        initializeShip();
    }
    @Override
    public void hit(){
        if(lifeCounter > 0){lifeCounter--;}
        counter = 0;
        STATE = SpriteState.DEAD;
    }
    public void rotateRight(){
        angle += ROTATION;
    }
    public void rotateLeft(){
        angle -= ROTATION;
    }
    public void accelerate(){
        xSpeed += Math.cos(angle - Math.PI/2)*THRUST;
        ySpeed += Math.sin(angle - Math.PI/2)*THRUST;
    }
    private void initializeShip(){
        STATE = SpriteState.ALIVE;
        PUPActive = false;
        currentPUP = null;
        
        xPos = 450;
        yPos = 300;
        
        xSpeed = 0;
        ySpeed = 0;
        
        THRUST = 0.5;
        ROTATION = 0.15;
        
        counter = 0;
        
        shape = new Polygon();
        drawShape = new Polygon();
        
        shape.addPoint(10, 15);
        shape.addPoint(-10, 15);
        shape.addPoint(-20, 0);
        shape.addPoint(-10, -15);
        shape.addPoint(-10, -5);
        shape.addPoint(0, 0);
        shape.addPoint(10, -5);
        shape.addPoint(10, -15);
        shape.addPoint(20, 0);
        
        drawShape.addPoint(10, 15);
        drawShape.addPoint(-10, 15);
        drawShape.addPoint(-20, 0);
        drawShape.addPoint(-10, -15);
        drawShape.addPoint(-10, -5);
        drawShape.addPoint(0, 0);
        drawShape.addPoint(10, -5);
        drawShape.addPoint(10, -15);
        drawShape.addPoint(20, 0);
    }

    public void addPUP(PUPChoice PUP) {
        if(PUP == PUPChoice.HEALTH){addLives();}
        else{
            PUPActive = true;
            currentPUP = PUP;
        }
    }
    
    public void removePUP(){
        PUPActive = false;
        currentPUP = null;
    }
    
    public boolean isPUPActive(){return PUPActive;}
    public PUPChoice getCurrentPUP(){return currentPUP;}
}
