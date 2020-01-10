package dbbwproject.serviceunit.schedulejob.pbookexpire;

import dbbwproject.serviceunit.dao.Notification;
import dbbwproject.serviceunit.dao.PencilBooking;
import dbbwproject.serviceunit.repository.NotificationRepository;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;
import java.util.List;

public class NotificationWriter implements ItemWriter<PencilBooking> {
    private final NotificationRepository noRepository;
    private String jobCode;

    NotificationWriter(NotificationRepository noRepository) {
        this.noRepository = noRepository;
    }

    @Value("#{jobParameters['jobCode']}")
    public void setJobCode(final String jobCode) {
        this.jobCode = jobCode;
    }

    @Override
    public void write(List<? extends PencilBooking> pbks) {
        System.out.println("ItemWriter met");
        for (PencilBooking pb : pbks) {
            String code = jobCode + "_" + pb.getTrip().getSeason().getCode() + "_" + pb.getTrip().getCode() + "_" + pb.getPersonName();
            String msg = "Person " + pb.getPersonName() + " has not come to office before meeting date " + pb.getMeetUpDate() + ".\n"
                    + "TP no: " + pb.getTpNo() + ".\n"
                    + "trip code: " + pb.getTrip().getCode() + " season code: " + pb.getTrip().getSeason().getCode();
            Notification not = new Notification(code, new java.sql.Date(new Date().getTime()), msg);
            System.out.println("inserting FNotification: " + code + " to DB");
            noRepository.save(not);
        }
    }
}
