function toggleGroupVisibility(handle, group) {
	var state = 'none';
	if (handle.innerHTML == '[+]') {
		handle.innerHTML = '[-]';
		state='';
	}
	else {
		handle.innerHTML = '[+]';
	}

	$$('.'+group).each(
		function(e){
			e.parentNode.style.display=state
		}
	)
}
