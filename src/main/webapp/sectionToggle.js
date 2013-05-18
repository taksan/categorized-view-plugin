function restoreJobGroupCollapseState(groupName) {
	var cookieName = "ident_group_state_"+groupName;
	var collapseState = YAHOO.util.Cookie.get(cookieName);
	if (collapseState == null)
		collapseState = 'none';
	if (collapseState == 'none') {
		hideJobGroup(groupName)
	}
	else {
		showJobGroup(groupName)
	}
}

function toggleJogGroupVisibility(handle, group) {
	if (handle.innerHTML == '[+]') {
		showJobGroup(group)
	}
	else {
		hideJobGroup(group)
	}
}

function hideJobGroup(group) {
	$$("#handle_"+group).first().innerHTML = '[+]';
	$$('.'+group).each(
		function(e){
			e.parentNode.style.display="none"
		}
	)
	YAHOO.util.Cookie.set("ident_group_state_"+group, "none");
}

function showJobGroup(group) {
	$$("#handle_"+group).first().innerHTML = '[-]';
	$$('.'+group).each(
		function(e){
			e.parentNode.style.display=""
		}
	)
	YAHOO.util.Cookie.set("ident_group_state_"+group, "");
}


