package DAO;

import DTO.Conference;
import DTO.Place;

import java.util.Collection;
import java.util.List;

public class ConferenceDAO extends BasicDAO{
    public static Conference getConference(int id){
        return (Conference) get(id, Conference.class);
    }
    public static List<Conference> getConferences(){
        return (List<Conference>) getAll("Conference");
    }

    /**
     *
     * @param conference hội nghị cần tạo
     * @return
     * <p>1: tạo hội nghị thành công</p>
     * <p>-1: thất bại do thời gian của hội nghị trùng với hội nghị khác tại cùng địa điểm</p>
     * <p>-2: thất bại do mã hội nghị đã tồn tại</p>
     * <p>-3: thất bại do truy vấn CSDL</p>
     */
    public static int createConference(Conference conference) {
        int result;
        if(getConference(conference.getId()) != null){
            result = -2;
        }
        else {
            Place place = PlaceDAO.getPlace(conference.getPlaceId());
            List<Conference> conferencesById = (List<Conference>) place.getConferencesById();
            for(Conference con : conferencesById){
                // 1 minute = 60000 milliseconds
                // kiểm tra thời gian bắt đầu của hội nghị tại địa điểm có trùng với hội nghị khác đang diễn tra không
                if(con.getHoldTime().getTime()+con.getConferenceTime()*60000
                        > conference.getHoldTime().getTime()){
//                    result = -1;
//                    break;
                    return -1;
                }
            }
            if(create(conference)){
                result = 1;
            }
            else{
                result = -3;
            }
        }
        return result;
    }
    public static boolean updateConference(Conference conference) {
        if(getConference(conference.getId()) == null){
            return false;
        }
        else {
            return update(conference);
        }
    }
}
