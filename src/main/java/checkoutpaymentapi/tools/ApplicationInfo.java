package checkoutpaymentapi.tools;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.stereotype.Component;

import checkoutpaymentapi.payload.AppInfoResponse;
import checkoutpaymentapi.payload.InfoDetail;
import lombok.extern.slf4j.Slf4j;

/**
 * Componente que permite recuperar información de la aplicación.
 * 
 * @see {@link http://biblioint.afip.gob.ar/pdf/IG_5_18_DIECCS_MONITSALUD.pdf}
 * @see afip.app.payload.AppInfoResponse
 * 
 */
@Component
@Slf4j
public class ApplicationInfo {
	@Autowired
	private DbHealthCheck dbHealthCheck;

	@Value("${info.app.system:janusky}")
	private String system;

	@Value("${info.app.name:checkout-payment-api}")
	private String application;

	@Value("${info.app.version:demo}}")
	private String version;

	@Value("${info.app.environment:production}")
	private String environment;

	public AppInfoResponse report() {
		List<InfoDetail> details = new ArrayList<>();
		details.add(dataBaseStatus());
		AppInfoResponse response = AppInfoResponse.builder()
				.system(system)
				.application(application)
				.version(version)
				.environment(environment)
				.details(details)
				.build();
		log.debug(" Application status {}", response);
		return response;
	}

	private InfoDetail dataBaseStatus() {
		Health health = dbHealthCheck.health();

		InfoDetail appInfoDetail = InfoDetail.builder()
				.name("DB Conection")
				.status(health.getStatus().getCode())
				.details(health.getDetails())
				.build();
		return appInfoDetail;
	}
}
