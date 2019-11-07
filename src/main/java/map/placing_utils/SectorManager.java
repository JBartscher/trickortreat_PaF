package map.placing_utils;

import exceptions.PlacebleBelongsToNoSectorException;
import exceptions.SectorOverlappingException;

import java.util.ArrayList;
import java.util.List;

/**
 * The SectorManager is a class to manage n Sectors, so that not all placeble objects have to be consulted, just the
 * ones of the sector that is returned by the SectorManager.
 */
public class SectorManager {

    private ArrayList<Sector> mapSectors = new ArrayList<>();

    /**
     * the constructor of the SectorManger takes a list of sectors and checks that they dont overlap.
     *
     * @param sectors a list of sectors.
     * @throws SectorOverlappingException sectors should not overlap each other to guarantee that the intersection logic
     *                                    works as intended.
     */
    SectorManager(List<Sector> sectors) throws SectorOverlappingException {
        if (sectors.isEmpty()) {
            throw new IllegalArgumentException("sectors cant be empty");
        }
        // check that sectors not overlap.
        for (Sector sector : sectors) {
            for (Sector other_sector : sectors) {
                if (sector.intersects(other_sector))
                    throw new SectorOverlappingException("Sectors cannot Overlap each other");
            }
        }
        // sector manager has all sectors.
        this.mapSectors.addAll(sectors);
    }

    /**
     * takes a placeble and assign it to a sector.
     *
     * @param placeble placeble which will be assigned to a sector.
     */
    public void assignSector(Placeble placeble) {
        for (Sector sector : this.mapSectors) {
            if (sector.contains(placeble)) {
                sector.addPlaceable(placeble);
                // placeble is fully contained by a sector. No further checkin necessary.
                break;
            } else {
                if (placeble.intersects(sector)) {
                    sector.addPlaceable(placeble);
                    // No need to break/end the loop. A placeble can be part of multiple sectors.
                }
            }
        }
    }

    /**
     * returns a list of sectors to which a already placed placeble belongs.  This method is NOT for new placebles.
     *
     * @param placeble placeble from which the lookup is done.
     * @return a list of sectors, normaly just holding one sector but it is possible that a placeble is part of more
     * than one sector.
     * @throws PlacebleBelongsToNoSectorException a placeble should always belong to a sector.
     */
    public List<Sector> belongsToSectors(Placeble placeble) throws PlacebleBelongsToNoSectorException {
        ArrayList<Sector> sectors = new ArrayList<>();
        for (Sector sector : mapSectors) {
            if (sector.contains(placeble))
                sectors.add(sector);
        }
        if (sectors.isEmpty())
            throw new PlacebleBelongsToNoSectorException("a placeble cannot belong to no sector");
        return sectors;
    }

    /**
     * this method just returns the first available sector, in which further place checking is done.
     *
     * @param placeble the new placeble
     * @return a sector in which the new placeble can possibly be placed.
     */
    public Sector getSector(Placeble placeble) throws PlacebleBelongsToNoSectorException {
        for (Sector sector : mapSectors) {
            if (placeble.intersects(sector))
                return sector;
        }
        throw new PlacebleBelongsToNoSectorException("a placeble cannot intersect with no sector");
    }


}
