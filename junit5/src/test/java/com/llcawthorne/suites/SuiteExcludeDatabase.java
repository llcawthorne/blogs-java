package com.llcawthorne.suites;

import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.ExcludeTags;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.SuiteDisplayName;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
@SuiteDisplayName("All non-database tests")
@SelectPackages("com.llcawthorne")
@ExcludeTags("database")
public class SuiteExcludeDatabase {
}
