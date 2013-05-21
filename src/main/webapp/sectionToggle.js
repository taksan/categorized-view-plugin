function restoreJobGroupCollapseState(viewName, groupName) 
{
	var collapseState = getGroupState(viewName,groupName);
	if (collapseState == null) collapseState = 'none';
	
	if (collapseState == 'none') {
		hideJobGroup(viewName,groupName)
	}
	else {
		showJobGroup(viewName,groupName)
	}
}

function toggleJogGroupVisibility(handle, viewName, group) 
{
	if (handle.innerHTML == '[+]') {
		showJobGroup(viewName,group)
	}
	else {
		hideJobGroup(viewName,group)
	}
}

function hideJobGroup(viewName, group) {
	$$("#handle_"+group).first().innerHTML = '[+]';
	$$('.'+group).each(
		function(e){
			e.parentNode.style.display="none"
		}
	)
	setGroupState(viewName,group, "none");
}

function showJobGroup(viewName, group) {
	$$("#handle_"+group).first().innerHTML = '[-]';
	$$('.'+group).each(
		function(e){
			e.parentNode.style.display=""
		}
	)
	setGroupState(viewName, group, "");
}

function getGroupStates(viewName) {
	var stateCookie = YAHOO.util.Cookie.get("jenkins.categorized-view-collapse-state_"+viewName);
	if (stateCookie == null)
		return null;
	return stateCookie.split(" ");
}

function getGroupState(viewName, groupName) {
	var groupStates = getGroupStates(viewName)
	if (groupStates == null)
		return "none";
	if (groupStates.indexOf(groupName)!=-1)
		return "none";
	return "";
}

function setGroupState(viewName, groupName, state) 
{
	var groupStates = getGroupStates(viewName)
	if (groupStates == null)
		groupStates = [];
		
	if (state == "none" && (groupStates.indexOf(groupName)==-1)) {
		groupStates[groupStates.length]=groupName;
		YAHOO.util.Cookie.set("jenkins.categorized-view-collapse-state_"+viewName, groupStates.join(" "));
	} 
	if (state == "" && (groupStates.indexOf(groupName)!=-1)) {
		var index = groupStates.indexOf(groupName);
		groupName.splice(index, 1);
		YAHOO.util.Cookie.set("jenkins.categorized-view-collapse-state_"+viewName, groupStates.join(" "));
	}
}
