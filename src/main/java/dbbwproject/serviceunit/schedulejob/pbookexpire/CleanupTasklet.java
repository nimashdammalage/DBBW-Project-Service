package dbbwproject.serviceunit.schedulejob.pbookexpire;

import dbbwproject.serviceunit.dao.Notification;
import dbbwproject.serviceunit.repository.NotificationRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;
import java.util.List;

public class CleanupTasklet implements Tasklet {
    private String jobCode;
    private final SessionFactory sessionFactory;
    private final NotificationRepository notRepository;

    public CleanupTasklet(SessionFactory sessionFactory, NotificationRepository notRepository) {
        this.sessionFactory = sessionFactory;
        this.notRepository = notRepository;
    }

    @Value("#{jobParameters['jobCode']}")
    public void setJobCode(final String jobCode) {
        this.jobCode = jobCode;
    }

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) {
        System.out.println("Starting Pencil Booking Expire date inspection job");
        System.out.println("time: " + new Date());

        //remove old notifications generated d by job
        try (Session session = sessionFactory.openSession()) {
            Query<Notification> query = session.createQuery("From Notification where code like ?1", Notification.class)
                    .setParameter(1, jobCode + "%");
            List<Notification> noList = query.list();
            notRepository.deleteAll(noList);
            return RepeatStatus.FINISHED;
        }
    }
}
