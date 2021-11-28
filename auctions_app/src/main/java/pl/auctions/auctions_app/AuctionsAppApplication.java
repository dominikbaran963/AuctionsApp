package pl.auctions.auctions_app;

import java.util.Collections;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@SpringBootApplication
@EnableScheduling
public class AuctionsAppApplication {
	public static void main(String[] args) {
		SpringApplication.run(AuctionsAppApplication.class, args);
	}
	
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
	    final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	    CorsConfiguration configuration = new CorsConfiguration().applyPermitDefaultValues();
	    configuration.setAllowedOrigins(Collections.singletonList("*"));
	    configuration.addAllowedMethod(HttpMethod.GET);
	    configuration.addAllowedMethod(HttpMethod.POST);
	    configuration.addAllowedMethod(HttpMethod.DELETE);
	    configuration.addAllowedMethod(HttpMethod.OPTIONS);

	    source.registerCorsConfiguration("/**", configuration);
	    
	    return source;
	}

}
