package map;

import exceptions.PlacebleBelongsToNoSectorException;
import exceptions.SectorOverlappingException;
import gameobjects.mapobjects.districts.District;
import map.placing_utils.Placeble;

import java.util.ArrayList;
import java.util.List;

/**
 * The SectorManager is a class to manage n Sectors, so that not all placeble objects have to be consulted, just the
 * ones of the sector that is returned by the SectorManager.
 */
public class DistrictManager {

    private ArrayList<District> mapDistircts = new ArrayList<>();

    /**
     * the constructor of the SectorManger takes a list of sectors and checks that they dont overlap.
     *
     * @param districts a list of sectors.
     * @throws SectorOverlappingException sectors should not overlap each other to guarantee that the intersection logic
     *                                    works as intended.
     */
    public DistrictManager(List<District> districts) throws SectorOverlappingException {
        if (districts.isEmpty()) {
            throw new IllegalArgumentException("sectors cant be empty");
        }
        // TODO: Sectoren besser abtrennen damit sie sich nciht mehr überschneiden und dann wieder einkommentieren
        // check that sectors not overlap.
        /*
        for (District district : districts) {
            for (District other_district : districts) {
                if (district.getSector().intersects(other_district.getSector()))
                    throw new SectorOverlappingException("Sectors cannot Overlap each other");
            }
        }
        */
        // sector manager has all sectors.
        this.mapDistircts.addAll(districts);
    }

    /**
     * takes a placeble and assign it to a district.
     *
     * @param placeble placeble which will be assigned to a sector.
     */
    public void assignDistrict(Placeble placeble) {
        for (District district : this.mapDistircts) {
            if (district.getSector().contains(placeble)) {
                district.getSector().addPlaceable(placeble);
                // placeble is fully contained by a sector. No further checkin necessary.
                break;
            } else {
                if (placeble.intersects(district.getSector())) {
                    district.getSector().addPlaceable(placeble);
                    // No need to break/end the loop. A placeble can be part of multiple sectors.
                }
            }
        }
    }

    /**
     * returns a list of districts to which a already placed placeble belongs. This method is NOT for new placebles.
     *
     * @param placeble placeble from which the lookup is done.
     * @return a list of districts, normally just holding one sector but it is possible that a placeble is part of more
     * than one district.
     * @throws PlacebleBelongsToNoSectorException a placeble should always belong to a sector.
     */
    public List<District> belongsToDistricts(Placeble placeble) throws PlacebleBelongsToNoSectorException {
        ArrayList<District> districts = new ArrayList<>();
        for (District district : mapDistircts) {
            if (district.getSector().contains(placeble))
                districts.add(district);
        }
        if (districts.isEmpty())
            throw new PlacebleBelongsToNoSectorException("a placeble cannot belong to no sector");
        return districts;
    }

    /**
     * this method just returns the first available district, in which further place checking is done.
     *
     * @param placeble the new placeble
     * @return a district in which the new placeble can possibly be placed.
     */
    public District getDistrict(Placeble placeble) throws PlacebleBelongsToNoSectorException {
        for (District district : mapDistircts) {
            if (placeble.intersects(district.getSector()))
                return district;
        }
        throw new PlacebleBelongsToNoSectorException("a placeble cannot intersect with no sector");
    }


}
