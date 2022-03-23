package io.snyk.snyklabs;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.jackson.datatype.VavrModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MultiroomChatApplication {

	public static void main(String[] args) {
		SpringApplication.run(MultiroomChatApplication.class, args);
	}

	@Bean
	public ObjectMapper jacksonBuilder() {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.registerModule(new VavrModule());
	}

}
