package Controllers.PojoController;

import Model.Pojo.Place;

import java.util.List;

public class PlaceController extends BasicDAO{
    public static Place getPlace(int id){
        return (Place) get(id, Place.class);
    }
    public static List<Place> getPlaces(){
        return (List<Place>) getAll("Place");
    }

    public static boolean createPlace(Place place) {
        if(getPlace(place.getId()) != null){
            return false;
        }
        else {
            return create(place);
        }
    }
    public static boolean updatePlace(Place place) {
        if(getPlace(place.getId()) == null){
            return false;
        }
        else {
            return update(place);
        }
    }
}
