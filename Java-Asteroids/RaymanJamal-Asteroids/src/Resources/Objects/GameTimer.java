package Resources.Objects;

public class GameTimer{
    private int timer;
    
    public GameTimer(){
        timer = 0;
    }
    
    public void updateTimer(){
        timer++;
    }
    
    public int getTimer(){return timer;}
    public void resetTimer(){timer = 0;}
}