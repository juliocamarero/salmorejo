'use strict';

import Component from 'metal-component';
import Soy from 'metal-soy';

import templates from './Pomodoro.soy';

class Pomodoro extends Component {
	start_() {
		console.log('starting');

		let userId = this.element.querySelector('#name').value;

		WeDeploy
			.data('data.salmorejo.wedeploy.me') // this should be io in production
		    .create('states', {
				id: userId,
				userId: userId,
				busy: true,
				date: Date.now(),
		    }).then((state) => {
				console.log('Saved:', state);
				this.countdown_();
		    })
			.catch(function(error) {
				console.log(error);
			});
	}

	countdown_() {
		let countdown = 1800000;

		setInterval(() => {
			countdown = countdown - 1000;
			this.countdown = parseInt(countdown / 60000);
		},
		1000);
	}
};

Soy.register(Pomodoro, templates);

Pomodoro.STATE = {
	countdown: {
		value: 30
	}
}

export default Pomodoro;
