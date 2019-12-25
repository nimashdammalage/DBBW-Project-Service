package dbbwproject.serviceunit.service;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.*;
import org.modelmapper.ModelMapper;

public abstract class AbstractService {
    //CONSTANTS
    static String seasonCodeUrlNotMatch = "season code: %s and code in url: %s does not match";
    static String tripCodeUrlNotMatch = "trip code: %s and code in url: %s does not match";
    static String personNameUrlNotMatch = "person name: %s and person name in url: %s does not match";
    static String regNumberUrlNotMatch = "registration number: %s and registration number in url: %s does not match";

    static String seasonAlreadyExist = "season with code: %s already exists";
    static String seasonNotExist = "season with code: %s does not exist";
    static String completedSeasonFound = "can not modify/insert a trip in a completed season. season code : %s";
    static String duplicateCurrentSeason = "two seasons can not be in current state";

    static String tripAlreadyExists = "trip with code: %s and season: %s already exists";
    static String tripNotExist = "trip with code %s and season: %s does not exist";
    static String linkedTripExists = "trips with season code: %s found. season can not be deleted";
    static String completeTRipFound = "can not insert/modify a pencil booking / booking into a trip in completed stage. trip code: %s, season code: %s";

    static String penBkAlreadyExist = "pencil booking with season code: %s, trip code: %s, and person name: %s already exists";
    static String penBkNotExist = "pencil booking with season code: %s, trip code: %s, and person name: %s does not exist";
    static String linkedPenBookingExists = "pencil bookings with trip code: %s and season code: %s found. trip can not be deleted";
    static String maxMeetCountReached = "meeting date is already assigned for %s number of customers. please choose another meeting date";
    static String invalidRegNumList = "registration number is not match with valied format. ex: 1,45,103";
    static String duplicateRegNumbers = "registration number list contrains duplicate numbers";
    static String alreadyAssignedRegNumber = "registration number %s is already booked in another pencil booking";
    static String RegNumberExceedMaxCount = "registration number %s is can not be issued for a trip with max passenger count %s";
    static String RegNumberConZero = "zero is not a valid registration number";

    static String bookingAlreadyExist = "booking with season code: %s, trip code: %s, and registration number: %s already exists";
    static String bookingNotExist = "booking with season code: %s, trip code: %s, and registration number: %s does not exist";
    static String linkedBookingExists = "bookings with trip code: %s, season code: %s and person name %s found. trip can not be deleted";
    static String invalidRegNum = "registration number : %s is not assigned for pencil booking customer: %s";

    final ModelMapper modelMapper;
    final DatabaseReference dbRef;

    public AbstractService(ModelMapper modelMapper, FirebaseApp firebaseApp) {
        this.modelMapper = modelMapper;
        dbRef = FirebaseDatabase.getInstance(firebaseApp).getReference("resources");
    }
}
