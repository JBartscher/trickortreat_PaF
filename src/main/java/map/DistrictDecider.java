package main.java.map;

import main.java.gameobjects.mapobjects.districts.District;
import main.java.gameobjects.mapobjects.districts.NormalDistrict;
import main.java.gameobjects.mapobjects.districts.PoorDistrict;
import main.java.gameobjects.mapobjects.districts.RichDistrict;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class DistrictDecider {
    int width;
    int height;

    public DistrictDecider(Map map) {
        width = map.getSize_x();
        height = map.getSize_y();
    }

    /**
     * creates four sectors which represent the four different district. Of the four districts are three fixed as a
     * rich, a poor and a normal district. The fourth district is picked at random.
     *
     * @return a List which contains all 4 Districts
     */
    public List<District> generateDistricts() {
        //create the sectors
        List<Sector> sectors = partionateIntoMapSectors();
        //shuffle to get a random outcome each time so that the pos of a sector isnt fixed
        Collections.shuffle(sectors);

        RichDistrict richSector = new RichDistrict(sectors.get(0));
        NormalDistrict normalDistrict = new NormalDistrict(sectors.get(1));
        PoorDistrict poorDistrict = new PoorDistrict(sectors.get(2));
        District randomDistrict = randomDistrict(sectors.get(3));

        ArrayList<District> districts = new ArrayList<>();
        districts.add(richSector);
        districts.add(normalDistrict);
        districts.add(poorDistrict);
        districts.add(randomDistrict);

        return districts;
    }

    /**
     * creates a random District
     *
     * @param sector the place where the District will lay
     * @return a random sector of the type NormalDistrict, RichDistrict or PoorDistrict
     */
    protected District randomDistrict(Sector sector) {
        District randomDistrict = null;
        Random r = new Random();
        // picks a random number in the range of 1-3 and decides a district based on that value.
        switch (r.nextInt(2)) {
            case 0:
                randomDistrict = new NormalDistrict(sector);
                break;
            case 1:
                randomDistrict = new RichDistrict(sector);
                break;
            case 2:
                randomDistrict = new PoorDistrict(sector);
                break;
            default:
                randomDistrict = new NormalDistrict(sector);
                break;
        }
        return randomDistrict;
    }

    /**
     * For a rectangle map this method creates 4 evenly big sectors.
     *
     * @return a list of 4 evenly spaced sectors
     */
    private List<Sector> partionateIntoMapSectors() {
        ArrayList<Sector> sectors = new ArrayList<>();
        int map_half_width = width / 2;
        int map_half_height = height / 2;
        // create sectors
        Sector sector_left_bottom = new Sector(0, 0, map_half_width, map_half_height);
        Sector sector_right_bottom = new Sector(0 + map_half_width, 0, map_half_width, map_half_height);
        Sector sector_left_top = new Sector(0, 0 + map_half_height, map_half_width, map_half_height);
        Sector sector_right_top = new Sector(0 + map_half_width, 0 + map_half_height, map_half_width, map_half_height);
        // add to list
        sectors.add(sector_left_bottom);
        sectors.add(sector_right_bottom);
        sectors.add(sector_left_top);
        sectors.add(sector_right_top);
        return sectors;
    }
}
