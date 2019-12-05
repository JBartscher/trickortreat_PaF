public class Entity {

    protected double xPos;
    protected double yPos;
    protected double size;

    // Default-Werte
    protected Entity(){
        xPos = 64;
        yPos = 64;
        size = Tile.TILE_SIZE;
    }

    public double getxPos() {
        return xPos;
    }

    public void setxPos(double xPos) {
        this.xPos = xPos;
    }

    public double getyPos() {
        return yPos;
    }

    public void setyPos(double yPos) {
        this.yPos = yPos;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

}
