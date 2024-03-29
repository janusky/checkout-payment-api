//package checkoutpaymentapi.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.r2dbc.connection.init.CompositeDatabasePopulator;
//import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
//import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;
//
//import io.r2dbc.spi.ConnectionFactory;
//
////@EnableR2dbcRepositories
//public class R2dbcConfig {
//
//	@Bean
//	public ConnectionFactoryInitializer initializer(ConnectionFactory connectionFactory) {
//
//	    ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
//	    initializer.setConnectionFactory(connectionFactory);
//
//	    CompositeDatabasePopulator populator = new CompositeDatabasePopulator();
//	    populator.addPopulators(new ResourceDatabasePopulator(new ClassPathResource("schema.sql")));
//	    populator.addPopulators(new ResourceDatabasePopulator(new ClassPathResource("data.sql")));
//	    initializer.setDatabasePopulator(populator);
//
//	    return initializer;
//	}
//}
