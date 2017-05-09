var form = document.querySelector('form');

form.addEventListener('submit', function(e) {
	e.preventDefault();

	var userId = form.userId.value;

	var existingStateforUser;

	WeDeploy
		.data('data.salmorejo.wedeploy.io') // this should be io in production
	    .get('states/' + userId)
	    .then(function(state) {
	        console.log(state);

			existingStateforUser = state;
	    });

	if (existingStateforUser) {
		WeDeploy
			.data('data.salmorejo.wedeploy.io') // this should be io in production
			.update(
				'states/' + userId,
				{
					busy: true,
					date: Date.now(),
				}
			)
			.then(function(state) {
				form.reset();
				form.userId.focus();
				console.info('Saved:', state);
			})
			.catch(function(error) {
				console.error(error);
			    });
	}
	else {
		WeDeploy
			.data('data.salmorejo.wedeploy.io') // this should be io in production
		    .create('states', {
				id: userId,
				userId: userId,
				busy: true,
				date: Date.now(),
		    }).then(function(state) {
				form.reset();
				form.userId.focus();
				console.info('Saved:', state);
		    })
			.catch(function(error) {
				console.error(error);
			});

	}


});