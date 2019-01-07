package com.llcawthorne.suites;

import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.SuiteDisplayName;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
@SuiteDisplayName("All database tests")
@SelectPackages("com.llcawthorne")
@IncludeTags("database")
public class SuiteDatabaseOnly {
}
