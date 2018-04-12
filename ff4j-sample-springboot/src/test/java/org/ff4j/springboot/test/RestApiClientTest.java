package org.ff4j.springboot.test;

import org.ff4j.FF4j;
import org.ff4j.core.FlippingExecutionContext;
import org.ff4j.springboot.conf.FF4jConfiguration;
import org.ff4j.springboot.strategy.ProjectFlipStrategy;
import org.ff4j.web.jersey2.store.FeatureStoreHttp;
import org.ff4j.web.jersey2.store.PropertyStoreHttp;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class RestApiClientTest {

    private static final String REST_API_URL = "http://localhost:8080/api/ff4j";
    private static final String USERNAME = "user";
    private static final String PASSWORD = "userPass";

    private FF4j ff4j;

    @Before
    public void setUp() {
        // Init FF4j as HTTP CLIENT
        ff4j = new FF4j();
        ff4j.setFeatureStore(new FeatureStoreHttp(REST_API_URL, USERNAME, PASSWORD));
        ff4j.setPropertiesStore(new PropertyStoreHttp(REST_API_URL, USERNAME, PASSWORD));
    }

    @Test
    public void check_SpfEnabledForProject() {
        ff4j.enable(FF4jConfiguration.SPECIFIC_PROJECTS_FEATURE);

        FlippingExecutionContext ctx = new FlippingExecutionContext();
        ctx.addValue(ProjectFlipStrategy.PROJECT, "100344");
        Assert.assertTrue(ff4j.check(FF4jConfiguration.SPECIFIC_PROJECTS_FEATURE, ctx));
    }

    @Test
    public void check_SpfDisabledForProject() {
        ff4j.enable(FF4jConfiguration.SPECIFIC_PROJECTS_FEATURE);

        FlippingExecutionContext ctx = new FlippingExecutionContext();
        ctx.addValue(ProjectFlipStrategy.PROJECT, "123");
        Assert.assertFalse(ff4j.check(FF4jConfiguration.SPECIFIC_PROJECTS_FEATURE, ctx));
    }

    @Test
    public void check_AllpfEnabledForAllProjects() {
        ff4j.enable(FF4jConfiguration.ALL_PROJECTS_FEATURE);

        FlippingExecutionContext ctx = new FlippingExecutionContext();
        ctx.addValue(ProjectFlipStrategy.PROJECT, "123");
        Assert.assertTrue(ff4j.check(FF4jConfiguration.ALL_PROJECTS_FEATURE, ctx));
    }

}
