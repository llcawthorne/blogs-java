package io.github.llcawthorne.junit5.suites;

import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.SuiteDisplayName;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
@SuiteDisplayName("All database tests")
@SelectPackages("io.github.llcawthorne.junit5")
@IncludeTags("database")
public class SuiteDatabaseOnly {
}
