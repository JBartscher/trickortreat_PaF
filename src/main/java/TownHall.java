public class TownHall extends House {

    private static final int TOWN_HALL_WIDTH = 6;
    private static final int TOWN_HALL_HEIGHT = 4;

    public TownHall(int x, int y) {
        super(x, y, TownHall.TOWN_HALL_WIDTH, TownHall.TOWN_HALL_HEIGHT);
    }

    @Override
    public void visit(Player player) {
        // super.visit(player);

        // release player
    }
}
