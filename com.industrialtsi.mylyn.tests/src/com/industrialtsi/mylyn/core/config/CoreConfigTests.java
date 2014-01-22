package com.industrialtsi.mylyn.core.config;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ IbatisConfigTest.class, PersistorConfigTest.class,
		RepositoryAttributesConfigTest.class, TaskAttributesConfigTest.class })
public class CoreConfigTests {

}
