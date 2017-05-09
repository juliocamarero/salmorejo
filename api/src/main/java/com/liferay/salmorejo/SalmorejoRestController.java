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

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

/**
 * @author Manuel de la Peña
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

}
