package org.jenkinsci.plugins.categorizedview;

import hudson.Extension;
import hudson.Util;
import hudson.model.TopLevelItem;
import hudson.model.Descriptor;
import hudson.model.Descriptor.FormException;
import hudson.model.ListView;
import hudson.model.ViewDescriptor;
import hudson.util.DescribableList;
import hudson.util.FormValidation;
import hudson.views.ViewJobFilter;
import hudson.views.ListViewColumn;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.servlet.ServletException;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;

public class CategorizedJobsView extends ListView {
	@SuppressWarnings("unused")
	private DescribableList<ListViewColumn, Descriptor<ListViewColumn>> columns = new DescribableList<ListViewColumn, Descriptor<ListViewColumn>>(
						this, CategorizedJobsListViewColumn.createDefaultInitialColumnList());
	
	private List<GroupingRule> groupingRules = new ArrayList<GroupingRule>();
	
	@DataBoundConstructor
	public CategorizedJobsView(String name) {
		super(name);
	}
	
	@Override
	public List<TopLevelItem> getItems() {
		return new CategorizedItemsBuilder().buildRegroupedItems(
				super.getItems(), 
				groupingRules);
	}
	
	@Override
	protected void submit(StaplerRequest req) throws ServletException, FormException, IOException {
		super.submit(req);
		groupingRules = new ArrayList<GroupingRule>();
		
		if (req.getSubmittedForm().get("groupingRules") instanceof JSONObject) {
			GroupingRule bindJSON = req.bindJSON(GroupingRule.class, req.getSubmittedForm().getJSONObject("groupingRules"));
			groupingRules.add(bindJSON);
		}
		else {
			JSONArray jsonArray = req.getSubmittedForm().getJSONArray("groupingRules");
			for (Object object : jsonArray) {
				JSONObject json= (JSONObject) object;
				GroupingRule bindJSON = req.bindJSON(GroupingRule.class, json);
				groupingRules.add(bindJSON);
			}
		}
	}
    
    public List<GroupingRule> getGroupingRules() {
        return groupingRules;
    }
	
	public String getCss() {
		StringBuilder builder = new StringBuilder();
		builder.append("padding-left:");
		builder.append("font-style:italic;font-size:smaller;font-weight:bold;");
		return builder.toString();
	}

	@Extension
	public static final class DescriptorImpl extends ViewDescriptor {
		public String getDisplayName() {
			return "Categorized Jobs View";
		}
		
		/**
		 * Checks if the include regular expression is valid.
		 */
		public FormValidation doCheckIncludeRegex(@QueryParameter String value)
				throws IOException, ServletException, InterruptedException {
			String v = Util.fixEmpty(value);
			if (v != null) {
				try {
					Pattern.compile(v);
				} catch (PatternSyntaxException pse) {
					return FormValidation.error(pse.getMessage());
				}
			}
			return FormValidation.ok();
		}
	}
	
	protected void initColumns() {
		try {
			Field field = ListView.class.getDeclaredField("columns");
			field.setAccessible(true);
			field.set(
					this,
					new DescribableList<ListViewColumn, Descriptor<ListViewColumn>>(
							this, CategorizedJobsListViewColumn.createDefaultInitialColumnList()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
