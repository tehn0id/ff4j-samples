package org.ff4j.springboot.strategy;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.ff4j.core.FeatureStore;
import org.ff4j.core.FlippingExecutionContext;
import org.ff4j.strategy.AbstractFlipStrategy;

public class ProjectFlipStrategy extends AbstractFlipStrategy {

    /**
     * Parameter to be checked in context.
     */
    public static final String PROJECT = "project";
    /**
     * Constant for projects.
     */
    private static final String PARAM_PROJECTLIST = "projects";
    /**
     * List of client to be accepted.
     */
    private static final String SPLITTER = ",";
    public static final String ALL_PROJECTS = "all";

    /**
     * Validate the target client against the available hostname.
     */
    private final Set<String> setOfProjects = new HashSet<>();

    /**
     * raw client list.
     */
    private String rawProjectList = null;

    /**
     * Default constructor for introspection.
     */
    public ProjectFlipStrategy() {
    }

    public ProjectFlipStrategy(String projects) {
        this.rawProjectList = projects;
        getInitParams().put(PARAM_PROJECTLIST, projects);
        for (String server : rawProjectList.split(SPLITTER)) {
            setOfProjects.add(server.trim());
        }
    }

    @Override
    public void init(String featureName, Map<String, String> initParams) {
        super.init(featureName, initParams);
        if (initParams != null && initParams.containsKey(PARAM_PROJECTLIST)) {
            this.rawProjectList = initParams.get(PARAM_PROJECTLIST);
        }
        setOfProjects.addAll(Arrays.asList(rawProjectList.split(SPLITTER)));
    }

    @Override
    public boolean evaluate(String featureName, FeatureStore store, FlippingExecutionContext ctx) {
        if (ctx == null || !ctx.containsKey(PROJECT)) {
            throw new IllegalArgumentException("To work with " + getClass().getName() + " you must provide '"
                    + PROJECT + "' parameter in execution context");
        }
        return setOfProjects.contains(ALL_PROJECTS) || setOfProjects.contains(ctx.getString(PROJECT));
    }
}
