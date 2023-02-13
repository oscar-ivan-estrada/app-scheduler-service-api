package co.microservicios.job;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.quartz.InterruptableJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.UnableToInterruptJobException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

import co.microservicios.dto.Response;
import co.microservicios.model.Job;
import co.microservicios.model.Trigger;
import co.microservicios.repository.JobRepository;
import co.microservicios.repository.TriggerRepository;
import co.microservicios.services.JobService;

public class CronJob extends QuartzJobBean implements InterruptableJob{
	

	@Autowired
	JobService jobService;

	@Autowired
    JobRepository jobRepository;

	@Autowired
    TriggerRepository triggerRepository;

	private static RestTemplate restTemplate ;


	@Override
	protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		JobKey key = jobExecutionContext.getJobDetail().getKey();
		
		if(restTemplate == null){
			restTemplate = new RestTemplate();
		}

		Job job = jobRepository.findJobByGroupAndJobName(key.getGroup(),key.getName());
	
		Trigger t = new Trigger();
		t.setEventDate(new Date());
		t.setJob(job);
		t.setViewed(false);
		this.triggerRepository.save(t);

	}

	

	@Override
	public void interrupt() throws UnableToInterruptJobException {
		System.out.println("Stopping thread... ");
	}

}