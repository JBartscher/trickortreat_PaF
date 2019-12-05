public class Mansion extends House {

    private static final int MANSION_WIDTH = 8;
    private static final int MANSION_HEIGHT = 5;

    /**
     * create the manison of Alice Cooper
     *
     * @param x
     * @param y
     */
    public Mansion(int x, int y) {
        super(x, y, MANSION_WIDTH, MANSION_HEIGHT);
    }

    @Override
    public void visit(Player player) {
        // super.visit(player);

        // play music
        // scare away witch
        // animate Alice Cooper
    }
}
