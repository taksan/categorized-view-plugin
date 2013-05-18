package org.jenkinsci.plugins.categorizedview.mocks;

import hudson.model.TopLevelItem;
import hudson.model.TopLevelItemDescriptor;
import hudson.model.AbstractItem;
import hudson.model.Job;

import java.util.Collection;

public class TopLevelItemMock extends AbstractItem implements TopLevelItem {

	public TopLevelItemMock(String name) {
		super(null, name);
	}

	@Override
	public Collection<? extends Job> getAllJobs() {
		throw new RuntimeException("NOT IMPLEMENTED");
	}

	public TopLevelItemDescriptor getDescriptor() {
		throw new RuntimeException("NOT IMPLEMENTED");
	}

}
