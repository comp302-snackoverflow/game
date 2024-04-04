package tr.edu.ku.comp302.domain.entity;

public class Lance {
    private int x;
    private int y;
    private int width;
    private int height;
    private double rotationDegrees;

    

    public Lance(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.rotationDegrees = 0.0;
    }

    // Getters and Setters for the Lance 
    public int getX() {
    	return x; 
    }
    
    public void setX(int x) {
    	this.x = x; 
    }

    public int getY() {
    	return y; 
    }
    
    public void setY(int y) {
    	this.y = y; 
    }

    public int getWidth() {
    	return width; 
    }
    
    public int getHeight() {
    	return height; 
    }
    
    
    public void setWidth(int width) { 
    	this.width = width; 
    }
    
    public void setHeight(int height) {
    	this.height = height; 
    }

    public double getRotationDegrees() {
    	return rotationDegrees; 
    }
    
    public void setRotationDegrees(double rotationDegrees) {
    	this.rotationDegrees = rotationDegrees; 
    }

    //Update Paddle position 
    public void move(int deltaX) {
        this.x += deltaX;
    }

    // Update Paddle Rotation
    public void rotate(double deltaDegrees) {
        this.rotationDegrees += deltaDegrees;
    }
}
