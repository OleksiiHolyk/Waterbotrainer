package ua.oleksiiholyk.jobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import ua.oleksiiholyk.services.SimpleService;


public class SimpleJob implements Job{

    @Autowired
    private SimpleService service;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        service.processData();
    }
}