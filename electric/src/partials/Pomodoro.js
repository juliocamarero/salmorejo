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
				busy: 1,
				date: Date.now(),
		    }).then(function(state) {
				console.info('Saved:', state);
		    })
			.catch(function(error) {
				console.error(error);
			});
	}

	countdown_() {
		setInterval(() => {
			this.countdown = this.countdown - 1000;
		},
		1000);
	}
};

Soy.register(Pomodoro, templates);

Pomodoro.STATE = {
	countdown: {
		value: 1800000
	}
}

export default Pomodoro;
