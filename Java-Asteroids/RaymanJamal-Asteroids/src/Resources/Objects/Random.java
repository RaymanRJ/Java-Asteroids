package Resources.Objects;

public class Random{
    
    int number;
    private java.util.Random r;
    
    public Random(){
        r = new java.util.Random();
    }
    
    public int nextInt(){
        return r.nextInt();
    }
    
    public int nextInt(int max){
        return r.nextInt(max+1);
    }
    
    public int nextInt(int min, int max){
        int temp = r.nextInt(max+1);
        while(temp < min){
            temp = r.nextInt(max+1);
        }
        return temp;
    }
    
    public double nextDouble(){
        return r.nextDouble();
    }
    
    public double nextDouble(int max){
        return r.nextDouble()*max + 1;
    }
    
    public boolean nextBoolean(){
        return r.nextBoolean();
    }
}