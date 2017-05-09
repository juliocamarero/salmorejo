'use strict';

import Component from 'metal-component';
import Soy from 'metal-soy';

import templates from './PomodoroList.soy';

class PomodoroList extends Component {
	created() {
		WeDeploy
	    .data('data.salmorejo.wedeploy.me')
	    .orderBy('id', 'desc')
	    .limit(5)
	    .get('states')
	    .then((response) => {
	        this.users = response;
	    })
	    .catch(function(error) {
	        console.error(error);
	    });
	}

	stop_() {
		console.log('stoping'); 

	}
};

Soy.register(PomodoroList, templates);

PomodoroList.STATE = {
	users: {

	}
}

export default PomodoroList;
