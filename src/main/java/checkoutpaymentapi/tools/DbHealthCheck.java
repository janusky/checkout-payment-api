package checkoutpaymentapi.tools;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.stereotype.Component;

@Component
public class DbHealthCheck implements HealthIndicator {
	@Autowired
	JdbcTemplate template;
//	@Autowired
//	private DatabaseClient databaseClient;

	@Autowired
	UtilCommon utilCommon;

	@Override
	public Health health() {
		int errorCode = check(); // perform some specific health check
		if (errorCode != 1) {
			String key = "error.code";
			String message = utilCommon.getMessage(key, "503");
			return Health.down().withDetail(message, "503").build();
		}
		return Health.up().build();
	}

	public int check() {
		String query = "select 1";
		List<Object> results = template.query(query, new SingleColumnRowMapper<>());
		return results.size();
//		databaseClient.sql("select 1").then()
//			.subscribe(resp -> {
//				System.out.println(resp);
//			});
//		return Integer.valueOf(1);
	}
}
