package Resources.Objects;

import Resources.States.SpriteState;
import java.awt.Graphics;
import java.awt.Polygon;

public abstract class Sprite {
    
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
    
    public Sprite(){
        r = new Random();
    }
    
    /**
     * User must deduct lifeCounter, and set State depending on object.
     */
    abstract void hit();
    
    public void updateSpriteState(){
        
    }
    public Difficulty getDifficulty(){return this.DIFFICULTY;}
    public double getXPos(){return this.xPos;}
    public double getYPos(){return this.yPos;}
    public double getXSpeed(){return this.xSpeed;}
    public double getYSpeed(){return this.ySpeed;}
    public double getAngle(){return this.angle;}
    public SpriteState getState(){return this.STATE;}
    public int getCounter(){return counter;}
    public void setCounter(int counter){this.counter = counter;}
    public void paint(Graphics g){
        if(this instanceof Asteroid){
            g.fillPolygon(drawShape);
        }else if(this instanceof Laser){
            g.fillPolygon(drawShape);
        }
        else{
            g.drawPolygon(drawShape);
        }
    }
    public void updatePosition(){
        
        counter++;
        
        xPos += xSpeed;
        yPos += ySpeed;
        
        wrapAround();
        
        int x,y;
        
        for (int i = 0; i < shape.npoints; i++)
        {
            x = (int)Math.round(shape.xpoints[i]*Math.cos(angle) - shape.ypoints[i]*Math.sin(angle));
            y = (int)Math.round(shape.xpoints[i]*Math.sin(angle) + shape.ypoints[i]*Math.cos(angle));
            drawShape.xpoints[i] = x;
            drawShape.ypoints[i] = y;
        }
        
        drawShape.invalidate();
        drawShape.translate((int)Math.round(xPos), (int)Math.round(yPos));
    }
    private void wrapAround(){
        if(xPos < 0) xPos = 900;
        if(xPos > 900) xPos = 0;
        if(yPos < 0) yPos = 600;
        if(yPos > 600) yPos = 0;
    }
    public int getLifeCounter(){return this.lifeCounter;}
    
}
