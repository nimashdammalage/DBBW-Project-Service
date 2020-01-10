package dbbwproject.serviceunit.schedulejob.pbookexpire;

import dbbwproject.serviceunit.dao.PencilBooking;
import dbbwproject.serviceunit.repository.NotificationRepository;
import org.hibernate.SessionFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.persistence.EntityManagerFactory;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

@Configuration
@EnableScheduling
public class PenBookPPExpireNotifyJob {
    private static final String JOB_NAME = "PenBookPPExpireNotifyJob";
    private final AtomicBoolean enabled = new AtomicBoolean(true);
    private final JobBuilderFactory jobBuilders;
    private final StepBuilderFactory stepBuilders;
    private final JobLauncher jobLauncher;
    private final SessionFactory sessionFactory;
    private final NotificationRepository noRepository;

    @Autowired
    public PenBookPPExpireNotifyJob(JobBuilderFactory jobBuilders, StepBuilderFactory stepBuilders, JobLauncher jobLauncher, EntityManagerFactory emf, NotificationRepository noRepository) {
        this.jobBuilders = jobBuilders;
        this.stepBuilders = stepBuilders;
        this.jobLauncher = jobLauncher;
        this.noRepository = noRepository;
        if (emf.unwrap(SessionFactory.class) == null) {
            throw new NullPointerException("factory is not a hibernate factory");
        }
        this.sessionFactory = emf.unwrap(SessionFactory.class);
    }

    //1000*60*60*24=8 640 0000
    @Scheduled(fixedRate = 86400000)
    public void run() throws Exception {
        String jobCode = JOB_NAME + "_" + new Date().getTime();
        if (!enabled.get()) {
            return;
        }
        jobLauncher.run(getJob(),
                new JobParametersBuilder()
                        .addDate("launchDate", new Date())
                        .addString("jobCode", jobCode)
                        .addString("jobName", JOB_NAME)
                        .toJobParameters());
    }

    @Bean
    public Job getJob() {
        return jobBuilders.get("PenBookPPExpireNotifyJob")
                .start(taskletStep())
                .next(chunkStep())
                .build();
    }

    @Bean
    public Step taskletStep() {
        return stepBuilders.get("taskletStep")
                .tasklet(tasklet())
                .build();
    }

    @Bean
    @StepScope
    public CleanupTasklet tasklet() {
        //task tht run repeatedly until meets RepeatStatus.FINISHED; or exception occures
        return new CleanupTasklet(sessionFactory, noRepository);
    }

    @Bean
    public Step chunkStep() {
        return stepBuilders.get("chunkStep")
                .<PencilBooking, PencilBooking>chunk(100)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

    @Bean
    @StepScope
    public PBReader reader() {
        return new PBReader(sessionFactory);
    }

    @Bean
    @StepScope
    public PBFilter processor() {
        return new PBFilter();
    }
//can combine one or more processors together

    @Bean
    @StepScope
    public NotificationWriter writer() {
        return new NotificationWriter(noRepository);
    }

}
