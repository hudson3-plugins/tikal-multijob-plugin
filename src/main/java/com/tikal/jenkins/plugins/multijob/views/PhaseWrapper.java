package com.tikal.jenkins.plugins.multijob.views;

import hudson.model.BallColor;
import hudson.model.Item;
import hudson.model.ItemGroup;
import hudson.model.Result;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Hudson;
import hudson.model.Job;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("rawtypes")
public class PhaseWrapper extends AbstractWrapper {

	final int nestLevel;

	final String phaseName;

	public PhaseWrapper(int nestLevel, String phaseName) {
		this.nestLevel = nestLevel;
		this.phaseName = phaseName;
	}

	@SuppressWarnings("unchecked")
	public Collection<? extends Job> getAllJobs() {
		return Collections.EMPTY_LIST;
	}

	public String getName() {
		return phaseName;
	}

	public String getFullName() {
		return phaseName;
	}

	public String getDisplayName() {
		return phaseName;
	}

	public String getFullDisplayName() {
		return phaseName;
	}

	public int getNestLevel() {
		return nestLevel;
	}

	// public AbstractProject getProject() {
	// return project;
	// }

	public BallColor getIconColor() {
		AbstractBuild worseBuild = null;
		for (BuildState buildState : childrenBuildState) {
			AbstractProject project = (AbstractProject) Hudson.getInstance()
					.getItem(buildState.getJobName());
			AbstractBuild build = (AbstractBuild) project
					.getBuildByNumber(buildState.getLastBuildNumber());
			if (build != null) {
                // Ensure worseBuild is never assigned a build with a null result.
				if (worseBuild == null) {
                    if (build.getResult() != null) {
                        worseBuild = build;
                    }
				} else {
                    Result buildResult = build.getResult();
					if (buildResult != null && buildResult.isWorseThan(worseBuild.getResult())) {
						worseBuild = build;
					}
				}
			}
		}
		if (worseBuild != null) {
			return worseBuild.getIconColor();
		}
		return null;
	}

	public String getCss() {
		StringBuilder builder = new StringBuilder();
		builder.append("padding-left:");
		builder.append(String.valueOf((getNestLevel() + 1) * 20));
		builder.append("px;");
		builder.append("font-style:italic;font-size:smaller;font-weight:bold;");
		return builder.toString();
	}

	public String getPhaseName() {
		return phaseName;
	}

	public boolean isPhase() {
		return true;
	}

	List<BuildState> childrenBuildState = new ArrayList<BuildState>();

	public void addChildBuildState(BuildState jobBuildState) {
		childrenBuildState.add(jobBuildState);
	}

	public String getRelativeNameFrom(ItemGroup g) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getRelativeNameFrom(Item item) {
		// TODO Auto-generated method stub
		return null;
	}
}
