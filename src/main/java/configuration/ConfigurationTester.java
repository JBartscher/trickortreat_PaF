package configuration;

class ConfigurationTester {
    public static void main(final String[] args) throws Exception {

        Configuration<Object> test = new Configuration<Object>();

        test.setParam("mapSize", 60);
        test.setParam("paused", true);
        test.setParam("controls", "mouse");

        test.getParam("mapSize");
        test.getParam("paused");
        test.getParam("controls");

        test.setParam("mapSize", 50);
        test.setParam("paused", false);
        test.setParam("controls", "keyboard");

        test.getParam("mapSize");
        test.getParam("paused");
        test.getParam("controls");
    }
}