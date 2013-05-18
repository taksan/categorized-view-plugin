package org.jenkinsci.plugins.categorizedview;

import hudson.model.TopLevelItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class CategorizedItemsBuilder {
	final Comparator<TopLevelItem> comparator = new TopLevelItemComparator();

	public List<TopLevelItem> buildRegroupedItems(List<TopLevelItem> itemsToCategorize, String groupRegex) 
	{
		final List<IndentedTopLevelItem> groupedItems = buildCategorizedList(itemsToCategorize, groupRegex);

		return flattenList(groupedItems);
	}
	
	private List<IndentedTopLevelItem> buildCategorizedList(List<TopLevelItem> itemsToCategorize, String groupRegex) {
		final List<IndentedTopLevelItem> categorizedItems = new ArrayList<IndentedTopLevelItem>();
		if (StringUtils.isEmpty(groupRegex)) {
			for (TopLevelItem indentedTopLevelItem : itemsToCategorize) {
				categorizedItems.add(new IndentedTopLevelItem(indentedTopLevelItem));
			}
			return categorizedItems;
		}
		
		final String normalizedRegex = normalizeRegex(groupRegex);
		for (TopLevelItem item : itemsToCategorize) {
			if (item.getName().matches(normalizedRegex)) 
				addItemInMatchingGroup(categorizedItems, normalizedRegex, item);
			else
				categorizedItems.add(new IndentedTopLevelItem(item));
		}
		return categorizedItems;
	}

	private String normalizeRegex(String groupRegex) {
		String regex = ".*"+groupRegex+".*";
		if (!groupRegex.contains("(")) {
			regex = ".*("+groupRegex+").*";
		}
		return regex;
	}

	private void addItemInMatchingGroup(final List<IndentedTopLevelItem> groupedItems, String regex, TopLevelItem item) 
	{
		final String groupNamingRule = "$1";
		final String groupName = item.getName().replaceAll(regex, groupNamingRule);
		IndentedTopLevelItem groupTopLevelItem = getGroupForItemOrCreateIfNeeded(groupedItems, groupName);
		IndentedTopLevelItem subItem = new IndentedTopLevelItem(item, 1, groupName, "");
		groupTopLevelItem.add(subItem);
	}

	private List<TopLevelItem> flattenList(final List<IndentedTopLevelItem> groupedItems) 
	{
		final ArrayList<TopLevelItem> res = new ArrayList<TopLevelItem>();
		
		Collections.sort(groupedItems, comparator);
		for (IndentedTopLevelItem item : groupedItems) {
			final String groupLabel = item.getName();
			addNestedItemsAsIndentedItemsInTheResult(res, item,	groupLabel);
		}
		
		return res;
	}

	private void addNestedItemsAsIndentedItemsInTheResult(final ArrayList<TopLevelItem> res, IndentedTopLevelItem item, final String groupLabel) {
		res.add(item);
		if (item.getNestedItems().size() > 0) {
			List<IndentedTopLevelItem> nestedItems = item.getNestedItems();
			Collections.sort(nestedItems, comparator);
			res.addAll(nestedItems);
		}
	}

	
	final Map<String, IndentedTopLevelItem> groupItemByGroupName = new HashMap<String, IndentedTopLevelItem>();
	private IndentedTopLevelItem getGroupForItemOrCreateIfNeeded(
			final List<IndentedTopLevelItem> groupedItems,
			final String groupName) {
		boolean groupIsMissing = !groupItemByGroupName.containsKey(groupName);
		if (groupIsMissing) {
			GroupTopLevelItem value = new GroupTopLevelItem(groupName);
			IndentedTopLevelItem value2 = new IndentedTopLevelItem(value, 0, groupName, value.getCss());
			groupItemByGroupName.put(groupName, value2);
			groupedItems.add(groupItemByGroupName.get(groupName));
		}
		return groupItemByGroupName.get(groupName);
	}
}
