package dbbwproject.serviceunit.schedulejob.pbookexpire;

import dbbwproject.serviceunit.dao.FPencilBooking;
import org.modelmapper.ModelMapper;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

@Configuration
@EnableScheduling
public class PenBookPPExpireNotifyJob {
    private AtomicBoolean enabled = new AtomicBoolean(true);
    private final ModelMapper modelMapper;
    private final JobBuilderFactory jobBuilders;
    private final StepBuilderFactory stepBuilders;
    private final JobLauncher jobLauncher;

    @Autowired
    public PenBookPPExpireNotifyJob(ModelMapper modelMapper, JobBuilderFactory jobBuilders, StepBuilderFactory stepBuilders, JobLauncher jobLauncher) {
        this.modelMapper = modelMapper;
        this.jobBuilders = jobBuilders;
        this.stepBuilders = stepBuilders;
        this.jobLauncher = jobLauncher;
    }

    //1000*60*60*24=8 640 0000
    @Scheduled(fixedRate = 100000)
    public void run() throws Exception {
        String jobCode = "PenBookPPExpireNotifyJob_" + new Date().getTime();
        if (!enabled.get()) {
            return;
        }
        JobExecution jobExecution = jobLauncher.run(getJob(),
                new JobParametersBuilder()
                        .addDate("launchDate", new Date())
                        .addString("jobCode", jobCode)
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
    public Tasklet tasklet() {
        //task tht run repeatedly until meets RepeatStatus.FINISHED; or exception occures
        Tasklet tasklet = (contribution, chunkContext) -> {
            System.out.println("Starting Pencil Booking Expire date inspection job");
            System.out.println("time: " + new Date());
            return RepeatStatus.FINISHED;
        };
        return tasklet;
    }

    @Bean
    public Step chunkStep() {
        return stepBuilders.get("chunkStep")
                .<FPencilBooking, FPencilBooking>chunk(2)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

    @Bean
    @StepScope
    public ItemReader<FPencilBooking> reader() {
        return new PBReader();
    }

    @Bean
    @StepScope
    public ItemProcessor<FPencilBooking, FPencilBooking> processor() {
        return new PBFilter();
    }
//can combine one or more processors together

    @Bean
    @StepScope
    public ItemWriter<FPencilBooking> writer() {
        return new NotificationWriter(modelMapper);
    }

}
