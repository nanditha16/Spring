package com.example.ajax.api.batch.config;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.MongoItemReader;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.oxm.xstream.XStreamMarshaller;
import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.Scheduled;

import com.example.ajax.api.model.Book;
import com.example.ajax.api.model.LibraryMember;
import com.example.ajax.api.util.DateConverter;
import com.example.ajax.api.util.MailUtility;

@Configuration
@EnableBatchProcessing
@EnableScheduling
public class RentedBooksReportAndEmailNotificationBatchConfig {

	final static Logger logger = LoggerFactory.getLogger(RentedBooksReportAndEmailNotificationBatchConfig.class);

	@Autowired(required = true)
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private MailUtility mailUtil;

	@Autowired
	private DateConverter dateConverter;
	
	@Autowired
	JobLauncher jobLauncher;

	JobParameters params;

	@Autowired
	private JobOperator operator;

	@Autowired
	private JobExplorer jobs;

	/*
	 * chunk-oriented processing, refers to reading the data sequentially and
	 * creating “chunks” that will be written out within a transaction boundary.
	 * 
	 * Each individual item is read in from an ItemReader, handed to an
	 * ItemProcessor, and aggregated.
	 * 
	 * Once the number of items read equals the commit interval, the entire chunk is
	 * written out via the ItemWriter, and then the transaction is committed.
	 * 
	 * The chunk() method builds a step that processes items in chunks with the size
	 * provided, with each chunk then being passed to the specified reader,
	 * processor, and writer.
	 * 
	 * For chunk based processing the reader and writer are mandatory, only the
	 * processor is optional
	 * 
	 * Read the data from MongoDB and write to xml
	 */
	@Bean
	public Step chunkStep1() {
		/*
		 * Can also do with SimpleMailMessageItemWriter and StaxEventItemWriter using
		 * CompositeItemWriter
		 */
		return stepBuilderFactory.get("chunkStep1").<Book, Book>chunk(20).reader(readerBook()).writer(writerBook())
				.build();
	}

	@Bean
	public Step chunkStep2() {
		return stepBuilderFactory.get("chunkStep2").<LibraryMember, LibraryMember>chunk(20)
				.reader(readerLibraryMember()).processor(processorEmail()).writer(writerDummy()).build();
	}

	/*
	 * ItemProcessors transform input items and introduce business logic in an
	 * item-oriented processing scenario.
	 * 
	 * Input : Book Output: LibraryMember email id
	 */
	@Bean
	public ItemProcessor<? super LibraryMember, ? extends LibraryMember> processorEmail() {
		ItemProcessor<LibraryMember, LibraryMember> process = null;
		process = processSendingEmail1(process);
		return process;
	}

	private ItemProcessor<LibraryMember, LibraryMember> processSendingEmail1(
			ItemProcessor<LibraryMember, LibraryMember> process) {
		process = new ItemProcessor<LibraryMember, LibraryMember>() {

			@Override
			public LibraryMember process(LibraryMember libraryMember) throws Exception {
				if (libraryMember.getBooks() != null) {
					String msg = mailUtil.sendEmail(libraryMember.getEmail(), buildMessage(libraryMember));
					logger.info("msg sent : " + msg);

					return libraryMember;
				}
				return null;
			}

			private String buildMessage(LibraryMember libraryMember) {
				String mailBody = "Dear " + libraryMember.getMemberName() + ",\n \n  The due date is approaching,"
						+ " kindly return the books rented or renew the same. \n" + libraryMember.hashCode() + "**"
						+ " has been generated." + "\n Due Date is" + LocalDate.now().plusDays(5)
						+ ".\n Please ignore if not applicable. Thank you.";
				return mailBody;
			}

		};
		return process;
	}

	/*
	 * The write() method is responsible for making sure that any internal buffers
	 * are flushed. If a transaction is active, it will also usually be necessary to
	 * discard the output on a subsequent rollback.
	 * 
	 * The resource to which the writer is sending data should normally be able to
	 * handle this itself.
	 * 
	 * Writing to xml
	 */
	@Bean
	public StaxEventItemWriter<? super Book> writerBook() {
		StaxEventItemWriter<Book> writer = new StaxEventItemWriter<Book>();
		writer.setRootTagName("Book");
		Random rand = new Random();
		int randomNum = rand.nextInt((10 - 1) + 1) + 1;

		String fileName = "booksRented_Report" + String.valueOf(System.nanoTime()) + "_" + randomNum;
		logger.info("Writing to file : " + fileName);
		writer.setResource(new FileSystemResource("xml/" + fileName + ".xml"));

		writer.setMarshaller(marshallerBook());
		return writer;
	}

	private XStreamMarshaller marshallerBook() {
		XStreamMarshaller marshaller = new XStreamMarshaller();
		
		// Handle LocalDate Serialization and Deserialization in XStream while writing to xml
		marshaller.setConverters(dateConverter);
		
		Map<String, Class> toBeSerializedMap = new HashMap<String, Class>();
		toBeSerializedMap.put("Book", Book.class);
		marshaller.setAliases(toBeSerializedMap);
		return marshaller;
	}

	@Bean
	public DummyItemWriter<? super LibraryMember> writerDummy() {
		return new DummyItemWriter();
	}

	/*
	 * An ItemReader provides the data and is expected to be stateful.
	 * 
	 * It is typically called multiple times for each batch, with each call to
	 * read() returning the next value and finally returning null when all input
	 * data has been exhausted.
	 * 
	 * Spring know that this class is a step-scoped Spring component and will be
	 * created once per step execution
	 * 
	 * Reading Data from MongoDB
	 */
	@Bean
	public MongoItemReader<? extends Book> readerBook() {
		MongoItemReader<Book> reader = new MongoItemReader<Book>();
		reader.setTemplate(mongoTemplate);
		reader.setQuery("{\"bookStatus\" : \"Rented\"}");
		reader.setTargetType(Book.class);
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
	public MongoItemReader<? extends LibraryMember> readerLibraryMember() {
		MongoItemReader<LibraryMember> reader = new MongoItemReader<LibraryMember>();
		reader.setTemplate(mongoTemplate);
		reader.setQuery("{}"); // Can set the query here only
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

	/*
	 * chunkSteps should always have reader and writer, process is optional
	 */
	/*
	 * Tasklet is an interface, which will be called to perform a single task only,
	 * like clean or set up resources before or after any step execution.
	 * 
	 */
	@Bean
	public Job runJob() {
		return jobBuilderFactory.get("rentedBooksReportJob").start(taskletStep()) // nothing is implemented
				.start(chunkStep1()).next(chunkStep2()).build();
	}

	@Bean
	public Step taskletStep() {
		return stepBuilderFactory.get("taskletStep").tasklet(tasklet()).build();
	}

	/*
	 * A Tasklet supports a simple interface that has only one method, execute(),
	 * which is called repeatedly until it either returns RepeatStatus.FINISHED or
	 * throws an exception to signal a failure.
	 * 
	 * Each call to the Tasklet is wrapped in a transaction.
	 */
	@Bean
	public Tasklet tasklet() {
		return (contribution, chunkContext) -> {
			return RepeatStatus.FINISHED;
		};
	}

	/*
	 * Job runs first time and sends JobInstanceAlreadyCompleteException as only
	 * unique JobInstances may be created and executed ; Spring Batch has no way of
	 * distinguishing between the first and second JobInstance so - introduce one or
	 * more unique parameters or - launch the next job in a sequence of JobInstances
	 * determined by the JobParametersIncrementer attached to the specified job
	 */

	// @Scheduled(fixedRate = 5000)
	@Scheduled(cron = "0 */1 * * * ?")
	public void perform() throws Exception {
		// introduce one or more unique parameters
		params = new JobParametersBuilder().addString("JobID", String.valueOf(System.nanoTime()))
				.addString("JOB_NAME", "RentedBooksReportJob").toJobParameters();
//		jobLauncher.run(runJob(), params);

		List<JobInstance> lastInstances = jobs.getJobInstances(params.getString("JobID"), 0, 1);
		if (lastInstances.isEmpty()) {
			jobLauncher.run(runJob(), params);
		} else {
			operator.startNextInstance(params.getString("JobID"));
		}
	}

}
