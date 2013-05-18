package org.jenkinsci.plugins.categorizedview;

import hudson.model.TopLevelItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CategorizedItemsBuilder {
	final Comparator<TopLevelItem> comparator = new TopLevelItemComparator();

	public List<TopLevelItem> buildRegroupedItems(List<TopLevelItem> itemsToCategorize, String groupRegex) {
		final List<TopLevelItem> groupedItems = buildItemsGroupsByRegex(itemsToCategorize, groupRegex);
		Collections.sort(groupedItems, comparator);
		
		return buildResultItemList(groupedItems);
	}
	
	private List<TopLevelItem> buildItemsGroupsByRegex(List<TopLevelItem> itemsToCategorize, String groupRegex) {
		final List<TopLevelItem> groupedItems = new ArrayList<TopLevelItem>();
		if (groupRegex == null) {
			groupedItems.addAll(itemsToCategorize);
			return groupedItems;
		}
		
		final Map<String, GroupTopLevelItem> groupItemByGroupName = new HashMap<String, GroupTopLevelItem>();
		String regex = ".*"+groupRegex+".*";
		if (!groupRegex.contains("(")) {
			regex = ".*("+groupRegex+").*";
		}
		for (TopLevelItem item : itemsToCategorize) {
			if (item.getName().matches(regex)) {
				final String groupNamingRule = "$1";
				final String groupName = item.getName().replaceAll(regex, groupNamingRule);
				GroupTopLevelItem groupTopLevelItem = getGroupForItemOrCreateIfNeeded(groupedItems, groupItemByGroupName, groupName);
				groupTopLevelItem.add(item);
				continue;
			}
			groupedItems.add(item);
		}
		return groupedItems;
	}

	private List<TopLevelItem> buildResultItemList(final List<TopLevelItem> groupedItems) {
		final ArrayList<TopLevelItem> res = new ArrayList<TopLevelItem>();
		
		for (TopLevelItem item : groupedItems) {
			final String groupLabel = item.getName();
			res.add(new IndentedTopLevelItem(item, 0, groupLabel));
			addNestedItemsAsIndentedItemsInTheResult(res, item,	groupLabel);
		}
		
		return res;
	}

	private void addNestedItemsAsIndentedItemsInTheResult(
			final ArrayList<TopLevelItem> res, TopLevelItem item,
			final String groupLabel) {
		if (item instanceof GroupTopLevelItem) {
			List<TopLevelItem> nestedItems = ((GroupTopLevelItem)item).getNestedItems();
			Collections.sort(nestedItems, comparator);
			for (TopLevelItem aNestedItem : nestedItems) {
				final int indentLevel = 1;
				IndentedTopLevelItem idented = new IndentedTopLevelItem(aNestedItem, indentLevel, groupLabel);
				res.add(idented);
			}
		}
	}

	

	private GroupTopLevelItem getGroupForItemOrCreateIfNeeded(
			final List<TopLevelItem> groupedItems,
			final Map<String, GroupTopLevelItem> groupedItemsByName,
			final String groupName) {
		boolean groupIsMissing = !groupedItemsByName.containsKey(groupName);
		if (groupIsMissing) {
			groupedItemsByName.put(groupName, new GroupTopLevelItem(groupName));
			groupedItems.add(groupedItemsByName.get(groupName));
		}
		GroupTopLevelItem groupTopLevelItem = (GroupTopLevelItem)groupedItemsByName.get(groupName);
		return groupTopLevelItem;
	}
}
