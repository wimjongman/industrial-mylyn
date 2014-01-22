package com.industrialtsi.mylyn.core.dto;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ IndustrialAttachmentTest.class, IndustrialCommentTest.class,
		IndustrialQueryOptionsTest.class, IndustrialQueryParamsTest.class,
		IndustrialTaskTest.class })
public class CoreDtoTests {

}
