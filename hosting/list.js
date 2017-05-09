var list = document.querySelector('.list');


// Insert fetch data method below

WeDeploy
    .data('data.salmorejo.wedeploy.me')
    .orderBy('id', 'desc')
    .limit(5)
    .get('states')
    .then(function(response) {
        appendStates(response);
    })
    .catch(function(error) {
        console.error(error);
    });

// Insert fetch data method above

function appendStates(states) {
	var statesList = '<ul>';

	states.forEach(function(state) {
		statesList += `<li>${state.userId} ${state.busy} ${state.date} </li>`;
	});

	statesList = statesList + '</ul>';

	list.innerHTML = statesList;
}