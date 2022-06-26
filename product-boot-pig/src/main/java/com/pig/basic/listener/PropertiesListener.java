package com.pig.basic.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartingEvent;
import org.springframework.context.ApplicationListener;

@Slf4j
public class PropertiesListener implements ApplicationListener<ApplicationStartingEvent> {

	private String propertyFileName;

	//
	public PropertiesListener(String propertyFileName) {
		this.propertyFileName = propertyFileName;
	}

	@Override
	public void onApplicationEvent(ApplicationStartingEvent applicationStartedEvent) {
		PropertiesListenerConfig.loadAllProperties(propertyFileName);
	}
}
