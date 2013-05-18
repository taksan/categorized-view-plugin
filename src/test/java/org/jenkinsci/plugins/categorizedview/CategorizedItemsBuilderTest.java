package org.jenkinsci.plugins.categorizedview;

import static org.junit.Assert.assertEquals;
import hudson.model.TopLevelItem;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jenkinsci.plugins.categorizedview.mocks.TopLevelItemMock;
import org.junit.Test;

public class CategorizedItemsBuilderTest {
	
	private List<TopLevelItem> itemsToCategorize;
	final CategorizedItemsBuilder subject = new CategorizedItemsBuilder();

	public CategorizedItemsBuilderTest() {
		itemsToCategorize = new ArrayList<TopLevelItem>();
		itemsToCategorize.add(new TopLevelItemMock("xa"));
		itemsToCategorize.add(new TopLevelItemMock("ba"));
		itemsToCategorize.add(new TopLevelItemMock("me"));
		itemsToCategorize.add(new TopLevelItemMock("ma"));
	}
	
	@Test
	public void getItems_withNullRegex_ShouldReturnSortedList() {
		String groupRegex = null;
		List<TopLevelItem> items = subject.buildRegroupedItems(itemsToCategorize, groupRegex);
		String expected = 
				"ba\n" +
				"ma\n" +
				"me\n" +
				"xa\n";
		
		String actual = buildResultToCompare(items);
		assertEquals(expected, actual);
	}
	
	@Test
	public void getItems_withRegex_ShouldGroupByRegex() {
		itemsToCategorize.add(new TopLevelItemMock("8.03-bar"));
		itemsToCategorize.add(new TopLevelItemMock("8.02-foo"));
		itemsToCategorize.add(new TopLevelItemMock("8.02-baz"));
		itemsToCategorize.add(new TopLevelItemMock("8.03-foo"));
		itemsToCategorize.add(new TopLevelItemMock("a8.03-foo"));
		
		String groupRegex = "(8...)";
		List<TopLevelItem> items = subject.buildRegroupedItems(itemsToCategorize, groupRegex);
		String expected =
				"8.02\n"+
				"  8.02-baz\n"+
				"  8.02-foo\n"+
				"8.03\n"+
				"  8.03-bar\n"+
				"  8.03-foo\n"+
				"  a8.03-foo\n"+	
				"ba\n" +
				"ma\n" +
				"me\n" +
				"xa\n";
		
		String actual = buildResultToCompare(items);
		assertEquals(expected, actual);
	}

	private String buildResultToCompare(List<TopLevelItem> items) {
		StringBuffer sb = new StringBuffer();
		for (TopLevelItem topLevelItem : items) {
			IndentedTopLevelItem identedItem = (IndentedTopLevelItem)topLevelItem;
			sb.append(StringUtils.repeat("  ", identedItem.getNestLevel()));
			sb.append(identedItem.getName());
			sb.append("\n");
		}
		return sb.toString();
	}
}