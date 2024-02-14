package checkoutpaymentapi.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.EnableWebFlux;

@Configuration
@EnableWebFlux
@EnableCaching
//@EnableR2dbcRepositories
//@ConditionalOnClass(EnableWebFlux.class) //checks that WebFlux is on the classpath
//@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE) //checks that the app is a reactive web app
public class WebConfig /* implements WebFluxConfigurer */ {
	
//	@Override
//	public void addCorsMappings(CorsRegistry registry) {
//		WebFluxConfigurer.super.addCorsMappings(registry);
//		registry.addMapping("/**")
//			.allowedOrigins("*") // any host or put domain(s) here
//			.allowedMethods("GET, POST") // put the http verbs you want allow
//			.allowedHeaders("Authorization"); // put the http headers you want allow
//	}
}