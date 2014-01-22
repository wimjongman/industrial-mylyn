package com.industrialtsi.mylyn.core.persistence;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ IbatisPersistorTest.class, PersistorsManagerTest.class,
		TasksSqlMapConfigTest.class })
public class CorePersistenceTests {

}
