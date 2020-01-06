package com.example.ajax.api.config;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import java.util.concurrent.ThreadLocalRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.MongoItemReader;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.oxm.xstream.XStreamMarshaller;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.annotation.Scheduled;

import com.example.ajax.api.model.LibraryMember;
import com.example.ajax.api.util.MailUtility;

@Configuration
@EnableBatchProcessing
public class BatchApplicationConfig {

	final static Logger logger = LoggerFactory.getLogger(BatchApplicationConfig.class);

	@Autowired(required=true)
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private MailUtility mailUtil;
	
	@Autowired
    JobLauncher jobLauncher;
    
	JobParameters params;
	
	@Bean
	public MongoItemReader<LibraryMember> reader() {
		MongoItemReader<LibraryMember> reader = new MongoItemReader<LibraryMember>();
		reader.setTemplate(mongoTemplate);
		reader.setQuery("{}");
		reader.setTargetType(LibraryMember.class);
		reader.setSort(new HashMap<String, Sort.Direction>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			{
				put("_id", Direction.DESC);

			}
		});

		return reader;
	}

	@Bean
	public StaxEventItemWriter<LibraryMember> writer() {
		StaxEventItemWriter<LibraryMember> writer = new StaxEventItemWriter<LibraryMember>();
		writer.setRootTagName("LibraryMember");
		//TODO: change to JOBID later
		String fileName = "booksRented_Report"+String.valueOf(System.currentTimeMillis()+"_"+ThreadLocalRandom.current().nextInt(1,Integer.MAX_VALUE));
		logger.info("Writing to file : " + fileName);
		writer.setResource(new FileSystemResource("xml/"+fileName+".xml"));
		
		writer.setMarshaller(marshaller());
		return writer;
	}
	
	private XStreamMarshaller marshaller() {
		XStreamMarshaller marshaller = new XStreamMarshaller();
		//TODO: Handle LocalDate Serialization and Deserialization
		Map<String, Class> toBeSerializedMap = new HashMap<String, Class>();
		toBeSerializedMap.put("LibraryMember", LibraryMember.class);
		
		marshaller.setAliases(toBeSerializedMap);
		return marshaller;
	}
	
	
	@Bean
	public Step step1() {
		return stepBuilderFactory.get("step1").<LibraryMember, LibraryMember>chunk(10).reader(reader()).processor(process()).writer(writer()).build();
		}
	
	
	@Bean
	public ItemProcessor<? super LibraryMember, ? extends LibraryMember> process() {
		ItemProcessor<LibraryMember,LibraryMember> process = null;
		;
		
		return processSendingEmail(process);
	}

	private ItemProcessor<? super LibraryMember, ? extends LibraryMember> processSendingEmail(ItemProcessor<LibraryMember,LibraryMember> process) {
		process = new ItemProcessor<LibraryMember,LibraryMember>(){

			@Override
			public LibraryMember process(LibraryMember libraryMember) throws Exception {
				if(libraryMember.getBooks() != null) {
					String msg = mailUtil.sendEmail(libraryMember.getEmail(), buildMessage(libraryMember));
					logger.info("msg sent : " + msg);
					
					return libraryMember;
				}
				return null;
			}

			private String buildMessage(LibraryMember libraryMember) {
				String mailBody = "Dear " + libraryMember.getMemberName() +",\n \n  The due date is approaching,"
						+ " kindly return the books rented or renew the same. \n" + libraryMember.hashCode() + "**" 
						+" has been generated." + "\n Due Date is" + LocalDate.now().plusDays(5)
						+".\n Please ignore if not applicable. Thank you.";
				return mailBody;
			}
			
		};
		return process;
	}

	@Bean
	public Job runJob() {
		return jobBuilderFactory.get("Rented Library Books report generation with notification email").flow(step1()).end().build();
	}
	
//	@Scheduled(cron = "0 */1 * * * ?")
//	public void perform() throws Exception {
//		params = new JobParametersBuilder()
//                .addString("JobID", String.valueOf(System.currentTimeMillis()))
//                .toJobParameters();
//		jobLauncher.run(runJob(), params);
//	}
	
	
}
