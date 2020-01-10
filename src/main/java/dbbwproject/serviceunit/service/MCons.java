package dbbwproject.serviceunit.service;

public class MCons {
    //CONSTANTS
    public static final String seasonCodeUrlNotMatch = "season code: %s and code in url: %s does not match";
    public static final String tripCodeUrlNotMatch = "trip code: %s and code in url: %s does not match";
    public static final String personNameUrlNotMatch = "person name: %s and person name in url: %s does not match";
    public static final String regNumberUrlNotMatch = "registration number: %s and registration number in url: %s does not match";

    public static final String seasonAlreadyExist = "season with code: %s already exists";
    public static final String seasonNotExist = "season with code: %s does not exist";
    public static final String completedSeasonFound = "can not modify/insert a trip in a completed season. season code : %s";
    public static final String duplicateCurrentSeason = "two seasons can not be in current state";
    public static final String incompleteSeason = "can not set season to COMPLETE state because all trips are not in COMPLETE state";

    public static final String tripAlreadyExists = "trip with code: %s and season: %s already exists";
    public static final String tripNotExist = "trip with code %s and season: %s does not exist";
    public static final String linkedTripExists = "trips with season code: %s found. season can not be deleted";
    public static final String completeTRipFound = "can not insert/modify a pencil booking / booking into a trip in completed stage. trip code: %s, season code: %s";
    public static final String incompleteTRipwithPbs = "can not set trip to COMPLETE state because all pencil booking customers were not arrived to meet";
    public static final String incompleteTRipwithBks = "can not set trip to COMPLETE state because all bookings are not in COMPLETE state";

    public static final String penBkAlreadyExist = "pencil booking with season code: %s, trip code: %s, and person name: %s already exists";
    public static final String penBkNotExist = "pencil booking with season code: %s, trip code: %s, and person name: %s does not exist";
    public static final String linkedPenBookingExists = "pencil bookings with trip code: %s and season code: %s found. trip can not be deleted";
    public static final String maxMeetCountReached = "meeting date is already assigned for %s number of customers. please choose another meeting date";
    public static final String duplicateRegNumbers = "registration number list contrains duplicate numbers";
    public static final String alreadyAssignedRegNumber = "registration number %s is already booked in another pencil booking";
    public static final String RegNumberExceedMaxCount = "registration number %s is can not be issued for a trip with max passenger count %s";
    public static final String RegNumberConZero = "zero is not a valid registration number";

    public static final String bookingAlreadyExist = "booking with season code: %s, trip code: %s, and registration number: %s already exists";
    public static final String bookingNotExist = "booking with season code: %s, trip code: %s, and registration number: %s does not exist";
    public static String linkedBookingExists = "bookings with trip code: %s, season code: %s and person name %s found. trip can not be deleted";
    public static final String invalidRegNum = "registration number : %s is not assigned for pencil booking customer: %s";

    public static final String settingNotExist = "setting entity does not exist";
    public static final String multipleSettingExist = "multiple setting entities exist";

}
