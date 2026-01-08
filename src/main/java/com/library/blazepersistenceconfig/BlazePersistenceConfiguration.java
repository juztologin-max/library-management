package com.library.blazepersistenceconfig;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;

import com.blazebit.persistence.Criteria;
import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.integration.view.spring.EnableEntityViews;
import com.blazebit.persistence.spring.data.repository.config.EnableBlazeRepositories;
import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.spi.EntityViewConfiguration;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceUnit;

@Configuration
@EnableBlazeRepositories(basePackages="com.library.repository")
@EnableEntityViews(basePackages="com.library.entityviews")
public class BlazePersistenceConfiguration{
	
	@PersistenceUnit
	private EntityManagerFactory  entityManagerFactory;

	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
	@Lazy(false)
	public CriteriaBuilderFactory createCriteriaBuilderFactory() {
		return Criteria.getDefault().createCriteriaBuilderFactory(entityManagerFactory);
	}
	
	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
	@Lazy(false)
	public EntityViewManager createEntityViewManager(CriteriaBuilderFactory cbf,EntityViewConfiguration conf) {
		return conf.createEntityViewManager(cbf);
	}
}