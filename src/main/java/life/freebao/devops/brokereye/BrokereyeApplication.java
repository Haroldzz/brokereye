package life.freebao.devops.brokereye;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
public class BrokereyeApplication {
	private static final Logger log = LoggerFactory.getLogger(BrokereyeApplication.class);
	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(BrokereyeApplication.class);
		ApplicationContext context = app.run(args);
		Environment env = context.getEnvironment();
		logApplicationStartup(env);
	}
	private static void logApplicationStartup(Environment env) {
		log.info("""

				----------------------------------------------------------
				    Application '{}' is running!
				    Profile(s): {}
				----------------------------------------------------------""",
				env.getProperty("spring.application.name"),
				env.getActiveProfiles().length == 0 ? env.getDefaultProfiles() : env.getActiveProfiles()
		);
	}
}
