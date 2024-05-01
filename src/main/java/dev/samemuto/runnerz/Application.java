package dev.samemuto.runnerz;

import ch.qos.logback.classic.spi.EventArgUtil;
import dev.samemuto.runnerz.run.Location;
import dev.samemuto.runnerz.run.Run;
import dev.samemuto.runnerz.run.RunRepository;
import dev.samemuto.runnerz.user.UserHttpClient;
import dev.samemuto.runnerz.user.UserRestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@SpringBootApplication
public class Application {
	private static final Logger log = LoggerFactory.getLogger(Application.class);
	public static void main(String[] args) {

		SpringApplication.run(Application.class, args);
	}

	@Bean
	UserHttpClient userHttpClient() {
		RestClient restClient = RestClient.create("https://jsonplaceholder.typicode.com/");
		HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(RestClientAdapter.create(restClient)).build();
		return factory.createClient(UserHttpClient.class);
	}

	@Bean
	public CommandLineRunner commandLineRunner(UserHttpClient client) {
		return args -> {
			log.info("Running command line runner");
			var users = client.findAll();
			var user = users.stream().findFirst().orElse(null);
			log.info("Found {} users", users.size());
			log.info("User: {}", user);
		};
	}

}
