package org.jenkinsci.plugins.categorizedview;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.acegisecurity.AccessDeniedException;

import hudson.model.Item;
import hudson.model.ItemGroup;
import hudson.model.TopLevelItem;
import hudson.model.TopLevelItemDescriptor;
import hudson.model.Job;
import hudson.search.SearchIndex;
import hudson.search.Search;
import hudson.security.ACL;
import hudson.security.Permission;

public class IndentedTopLevelItem implements TopLevelItem {
	
	private final TopLevelItem decorated;
	private int nestLevel;
	private final String groupLabel;
	public IndentedTopLevelItem(TopLevelItem decorated, int nestLevel, String groupLabel, String css) {
		this.decorated = decorated;
		this.nestLevel = nestLevel;
		this.groupLabel = groupLabel;
		this.specificCss.append(css);
	}
	
	public IndentedTopLevelItem(TopLevelItem item) {
		this(item,0,"","");
	}

	public int getNestLevel() {
		return nestLevel;
	}

	public ACL getACL() {
		return decorated.getACL();
	}

	public File getRootDir() {
		return decorated.getRootDir();
	}

	public void checkPermission(Permission permission)
			throws AccessDeniedException {
		decorated.checkPermission(permission);
	}

	public Search getSearch() {
		return decorated.getSearch();
	}

	public boolean hasPermission(Permission permission) {
		return decorated.hasPermission(permission);
	}

	public String getSearchName() {
		return decorated.getSearchName();
	}

	public String getSearchUrl() {
		return decorated.getSearchUrl();
	}

	public TopLevelItemDescriptor getDescriptor() {
		return decorated.getDescriptor();
	}

	public SearchIndex getSearchIndex() {
		return decorated.getSearchIndex();
	}

	public String getFullName() {
		return decorated.getFullName();
	}

	public String getDisplayName() {
		return decorated.getDisplayName();
	}

	public String getFullDisplayName() {
		return decorated.getFullDisplayName();
	}

	public String getRelativeNameFrom(ItemGroup g) {
		return decorated.getRelativeNameFrom(g);
	}

	public String getRelativeNameFrom(Item item) {
		return decorated.getRelativeNameFrom(item);
	}

	public String getUrl() {
		return decorated.getUrl();
	}

	public String getShortUrl() {
		return decorated.getShortUrl();
	}

	@SuppressWarnings("deprecation")
	public String getAbsoluteUrl() {
		return decorated.getAbsoluteUrl();
	}

	public void onLoad(ItemGroup<? extends Item> parent, String name)
			throws IOException {
		decorated.onLoad(parent, name);
	}

	public void onCopiedFrom(Item src) {
		decorated.onCopiedFrom(src);
	}

	public void onCreatedFromScratch() {
		decorated.onCreatedFromScratch();
	}

	public void save() throws IOException {
		decorated.save();
	}

	public void delete() throws IOException, InterruptedException {
		decorated.delete();
	}

	public ItemGroup<? extends Item> getParent() {
		return decorated.getParent();
	}

	public Collection<? extends Job> getAllJobs() {
		return decorated.getAllJobs();
	}

	public String getName() {
		return decorated.getName();
	}
	
	public boolean hasLink() {
		return getShortUrl() != null;
	}
	
	public String getGroupClass() {
		return groupLabel.replace(".", "_");
	}

	public String getCss() {
		StringBuilder builder = getBasicCss();
		return builder.toString();
	}

	private StringBuilder getBasicCss() {
		StringBuilder builder = new StringBuilder();
		builder.append("padding-left:");
		builder.append(String.valueOf((getNestLevel() + 1) * 20));
		builder.append("px;");
		builder.append(specificCss.toString());
		return builder;
	}

	StringBuilder specificCss = new StringBuilder();
	
	public void add(IndentedTopLevelItem item) {
		nestedItems.add(item);
	}
	
	public List<IndentedTopLevelItem> getNestedItems() {
		return nestedItems;
	}
	
	private List<IndentedTopLevelItem> nestedItems = new ArrayList<IndentedTopLevelItem>();
}
