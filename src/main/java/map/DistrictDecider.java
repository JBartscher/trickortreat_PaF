package main.java.map;

import main.java.gameobjects.mapobjects.districts.District;
import main.java.gameobjects.mapobjects.districts.NormalDistrict;
import main.java.gameobjects.mapobjects.districts.PoorDistrict;
import main.java.gameobjects.mapobjects.districts.RichDistrict;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * this class decides in which district a map object is placed
 */
public class DistrictDecider {
    final int width;
    final int height;

    public DistrictDecider(Map map) {
        width = map.getSize();
        height = map.getSize();
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
        RichDistrict richSector1 = new RichDistrict(sectors.get(4));
        sectors.remove(sectors.get(4));
        RichDistrict richSector2 = new RichDistrict(sectors.get(0));
        sectors.remove(sectors.get(0));
        Collections.shuffle(sectors);

        NormalDistrict normalDistrict1 = new NormalDistrict(sectors.get(0));
        NormalDistrict normalDistrict2 = new NormalDistrict(sectors.get(4));

        PoorDistrict poorDistrict1 = new PoorDistrict(sectors.get(1));
        PoorDistrict poorDistrict2 = new PoorDistrict(sectors.get(5));

        District randomDistrict1 = randomDistrict(sectors.get(2));
        District randomDistrict2 = randomDistrict(sectors.get(6));
        District randomDistrict3 = randomDistrict(sectors.get(3));


        ArrayList<District> districts = new ArrayList<>();
        districts.add(richSector1);
        districts.add(richSector2);
        districts.add(normalDistrict1);
        districts.add(normalDistrict2);
        districts.add(poorDistrict1);
        districts.add(poorDistrict2);
        districts.add(randomDistrict1);
        districts.add(randomDistrict2);
        districts.add(randomDistrict3);

        for(District district: districts) {
            Random random = new Random();
            district.setBiomeType(District.BiomeType.values()[random.nextInt(District.BiomeType.values().length)]);

        }

        return districts;
    }

    /**
     * creates a random District
     *
     * @param sector the place where the District will lay
     * @return a random sector of the type NormalDistrict, RichDistrict or PoorDistrict
     */
    protected District randomDistrict(Sector sector) {
        District randomDistrict;
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
        int map_third_width = width / 3;
        int map_third_height = height / 3;

        //int map_second_third_width = (width / 3) * 2;
        //int map_second_third_height = (height / 3) * 2;


        // create sectors
        //Sector sector_top_left = new Sector(0, 0, map_third_width, map_second_third_height);
        //Sector sector_top_centre = new Sector(0, 0, map_third_width, map_second_third_height);


        //Sector sector_left_bottom = new Sector(0, 0, map_half_width, map_half_height);
        //Sector sector_right_bottom = new Sector(map_half_width, 0, map_half_width, map_half_height);
        //Sector sector_left_top = new Sector(0, map_half_height, map_half_width, map_half_height);
        //Sector sector_right_top = new Sector(map_half_width, map_half_height, map_half_width, map_half_height);

        for(int y = 0; y < 3; y++) {
            for(int x = 0; x < 3; x++) {
                Sector sector = new Sector(x * map_third_width, y * map_third_height, map_third_width, map_third_height);
                sectors.add(sector);
            }
        }

        // add to list
        //sectors.add(sector_left_bottom);
        //sectors.add(sector_right_bottom);
        //sectors.add(sector_left_top);
        //sectors.add(sector_right_top);
        return sectors;
    }
}
