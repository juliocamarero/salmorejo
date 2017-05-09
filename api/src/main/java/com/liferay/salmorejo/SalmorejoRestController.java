/**
 *    Copyright 2017-today Manuel de la Peña
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.liferay.salmorejo;

import com.liferay.salmorejo.repository.State;
import com.liferay.salmorejo.repository.DataRepository;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @author Julio CAmarero
 */
@RestController
@RequestMapping("/states")
public class SalmorejoRestController {

	private final DataRepository dataRepository = DataRepository.getInstance();

	@CrossOrigin(origins = "*")
	@RequestMapping(method = RequestMethod.GET, value = "/{userId}")
	public String getUserState(@PathVariable String userId) {

		State state = dataRepository.findByUserId(userId);

		if (state != null && state.isBusy()) {
			return "busy";
		}

		return "idle";
	}

	@CrossOrigin(origins = "*")
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public String postState(@RequestParam("user_name") String userId, @RequestParam("text") String text) {
		if (text.equals("") || text.equals("start")) {
			return startPomodoro(userId);
		}
		else if (text.equals("stop")) {
			return stopPomodoro(userId);
		}
		else if (text.equals("list")) {
			return "All these users are in Pomodoro....";
		}
		else {
			return checkUserPomodoro(text);
		}
	}

	private String checkUserPomodoro(String otherUserId) {
		State state = dataRepository.findByUserId(otherUserId);

		if (state != null && state.isBusy()) {
			Date time = new Date();

			long difference = (time.getTime() - state.getDate())/60000;

			if (difference > 0) {
				return otherUserId + " is still busy for " + difference + " minutes";
			}
			else {
				return otherUserId + " has been available since " + (-difference) + " minutes ago";
			}
		}

		return otherUserId + " is available :)";
	}

	private String startPomodoro(String userId) {
		State state = new State();

		state.setBusy(true);
		state.setDate(new Date().getTime());
		state.setId(userId);
		state.setUserId(userId);

		dataRepository.create(state);
		dataRepository.save(state);

		return "Pomodoro Started for User " + userId;
	}

	private String stopPomodoro(String userId) {
		State state = new State();

		state.setBusy(false);
		state.setDate(new Date().getTime());
		state.setId(userId);
		state.setUserId(userId);

		dataRepository.save(state);

		return "Pomodoro Stopped for User " + userId;
	}

}
