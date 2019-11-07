package map.placing_utils;


public class Placeble {

    private int x;
    private int y;
    // in tiles
    private int width;
    private int height;
    // in tiles
    private int offset = 1;

    public Placeble(int x, int y, int width, int height, int offset) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.offset = offset;
    }

    public Placeble(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.offset = 1;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getOffset() {
        return offset;
    }

    /**
     * checks whether this placeble intersects with anotehr one. It is important to note that the offset is only
     * computed from this object.
     *
     * @param other
     * @return
     */
    public boolean intersects(Placeble other) {
        return (this.x - this.offset < other.x + other.width &&
                this.x + this.width + this.offset > other.x &&
                this.y - this.offset < other.y + other.height &&
                this.y + this.height + this.offset > other.y);
    }

    /**
     * checks whether this placeble contains another one.
     *
     * @param other
     * @return
     */
    public boolean contains(Placeble other) {
        if ((other.x + other.width) < (this.x + this.width)
                && (other.x) > (this.x)
                && (other.y) > (this.y)
                && (other.y + other.height) < (this.y + this.height)
        ) {
            return true;
        } else {
            return false;
        }
    }
}
