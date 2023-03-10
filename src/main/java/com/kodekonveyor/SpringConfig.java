package com.kodekonveyor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.sql.DataSource;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.kodekonveyor.annotations.ExcludeFromCodeCoverage;
import com.kodekonveyor.annotations.InterfaceClass;
import com.kodekonveyor.webapp.ResponseFilter;
import com.kodekonveyor.webapp.WebappConstants;

import jakarta.servlet.Filter;

@EnableScheduling
@SpringBootApplication
@ServletComponentScan
@InterfaceClass
@ExcludeFromCodeCoverage("interface to underlaying framework")
@EnableWebSecurity
public class SpringConfig extends SpringBootServletInitializer {

	@Value("${com.kodekonveyor.repo.jdbcDriver}")
	private String jdbcDriver;

	@Value("${com.kodekonveyor.repo.jdbcUri}")
	private String jdbcUri;

	@Value("${com.kodekonveyor.task.max-thread:10}")
	private int maxThreadCount;


	@Value("${auth0.audience}")
	private String audience;

	@Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
	private String issuer;

	@Autowired
	private Logger logger;
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
				.authorizeHttpRequests(
						authz -> authz
						
								.requestMatchers(HttpMethod.GET, "/api/v1/hero")
								.hasAuthority("SCOPE_read:current_user")
								.anyRequest()
								.permitAll())
				.oauth2ResourceServer(oauth2 -> oauth2.jwt())
				.csrf()
				.csrfTokenRepository(
						CookieCsrfTokenRepository.withHttpOnlyFalse())
				;

		return http.build();

	}

	public static void main(final String[] args) {
		SpringApplication.run(SpringConfig.class, args);
	}

	@Bean
	public DataSource dataSource() {
		final DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder
				.create();
		dataSourceBuilder.driverClassName(jdbcDriver);
		dataSourceBuilder.url(jdbcUri);
		return dataSourceBuilder.build();
	}

	@Bean
	public FilterRegistrationBean<CharacterEncodingFilter> filterRegistrationBean() {
		final FilterRegistrationBean<CharacterEncodingFilter> registrationBean = new FilterRegistrationBean<>();
		final CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
		characterEncodingFilter.setForceEncoding(true);
		characterEncodingFilter.setEncoding(WebappConstants.UTF_8);
		registrationBean.setFilter(characterEncodingFilter);
		return registrationBean;
	}

	@Bean
	public FilterRegistrationBean<Filter> responseFilterRegistrationBean() {
		final FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(new ResponseFilter());
		return registrationBean;
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Bean(destroyMethod = "shutdown")
	public ExecutorService executorService() {// NOPMD
		return Executors.newFixedThreadPool(maxThreadCount);
	}

}
