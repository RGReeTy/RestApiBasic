package com.epam.esm.config;

import lombok.AllArgsConstructor;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.sql.DataSource;
import java.util.Objects;

@AllArgsConstructor
@Configuration
@ComponentScan("com.epam.esm")
@EnableWebMvc
@EnableTransactionManagement
@PropertySources({@PropertySource("classpath:application.properties")})
public class SpringJdbcConfig implements TransactionManagementConfigurer {

    private final static int DEFAULT_INITIAL_SIZE = 3;
    private final static String DATABASE_DRIVER = "ds.database-driver";
    private final static String DATABASE_URL = "ds.url";
    private final static String DATABASE_USERNAME = "ds.username";
    private final static String DATABASE_PASSWORD = "ds.password";
    private final static String DATABASE_INIT_SIZE = "db.initial-size";

    private final ApplicationContext applicationContext;
    private Environment environment;

    @Bean
    public DataSource mysqlDataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(environment.getProperty(DATABASE_DRIVER));
        dataSource.setUrl(environment.getProperty(DATABASE_URL));
        dataSource.setUsername(environment.getProperty(DATABASE_USERNAME));
        dataSource.setPassword(environment.getProperty(DATABASE_PASSWORD));
        String initSizeProperty = environment.getProperty(DATABASE_INIT_SIZE);
        dataSource.setInitialSize
                (!Objects.isNull(initSizeProperty) ? Integer.valueOf(initSizeProperty) : DEFAULT_INITIAL_SIZE);
        return dataSource;
    }

    @Bean
    public PlatformTransactionManager txManager() {
        return new DataSourceTransactionManager(mysqlDataSource());
    }

    @Override
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return txManager();
    }

    @Bean
    public JdbcTemplate getJdbcTemplate() {
        return new JdbcTemplate(mysqlDataSource());
    }


}
