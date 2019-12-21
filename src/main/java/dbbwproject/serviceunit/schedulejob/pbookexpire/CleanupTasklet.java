package dbbwproject.serviceunit.schedulejob.pbookexpire;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import dbbwproject.serviceunit.dao.FNotification;
import dbbwproject.serviceunit.firebasehandler.DBHandle;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.List;

public class CleanupTasklet implements Tasklet {
    private final DatabaseReference dbRef;
    private String jobCode;
    private String jobName;

    @Value("#{jobParameters['jobCode']}")
    public void setJobCode(final String jobCode) {
        this.jobCode = jobCode;
    }

    @Value("#{jobParameters['jobName']}")
    public void setJobName(final String jobName) {
        this.jobName = jobName;
    }

    public CleanupTasklet() {
        dbRef = FirebaseDatabase.getInstance().getReference("resources");
    }


    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        System.out.println("Starting Pencil Booking Expire date inspection job");
        System.out.println("time: " + new Date());

        //remove old notifications generated d by job
        Query query = dbRef.child(FNotification.key).orderByKey().startAt(jobName).endAt(jobCode + "\uf8ff");
        ResponseEntity<List<FNotification>> oldNotifications = DBHandle.retrieveDataList(FNotification.class, query);
        if (oldNotifications.getBody() == null || oldNotifications.getBody().isEmpty()) {
            return RepeatStatus.FINISHED;
        }

        for (FNotification fNotification : oldNotifications.getBody()) {
            String key = fNotification.getNotificationId();
            DBHandle.deleteDataFromDB(dbRef.child(FNotification.key).child(key));
        }

        return RepeatStatus.FINISHED;
    }
}
