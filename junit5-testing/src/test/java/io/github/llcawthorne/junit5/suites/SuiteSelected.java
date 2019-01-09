package io.github.llcawthorne.junit5.suites;

import io.github.llcawthorne.junit5.junit5.AssertTest;
import io.github.llcawthorne.junit5.junit5.LifecycleTest;
import io.github.llcawthorne.junit5.junit5.NamingTest;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
@SelectClasses({AssertTest.class, LifecycleTest.class, NamingTest.class})
public class SuiteSelected {
}
