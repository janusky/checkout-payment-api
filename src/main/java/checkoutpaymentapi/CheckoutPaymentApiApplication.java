package checkoutpaymentapi;

import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class CheckoutPaymentApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(CheckoutPaymentApiApplication.class, args);
	}

	@PostConstruct
	void init() {
		TimeZone.setDefault(TimeZone.getTimeZone("America/Buenos_Aires"));
	}
}
