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

package com.liferay.salmorejo.repository;

import com.wedeploy.api.ApiClient;
import com.wedeploy.api.WeDeploy;
import com.wedeploy.api.sdk.Response;
import com.wedeploy.api.serializer.impl.JoddJsonParser;
import com.wedeploy.api.serializer.impl.JoddJsonSerializer;

import java.util.List;


/**
 * @author Manuel de la Peña
 */
public class DataRepository {

	public static DataRepository getInstance() {
		return instance;
	}


	public State findByUserId(String userId) {
		WeDeploy weDeploy = new WeDeploy(BASE_STATES_DATA_PATH);

		Response response = weDeploy.filter("userId", userId).get();

		JoddJsonParser parser = new JoddJsonParser();

		List<State> states = parser.parseAsList(response.body(), State.class);

		if (!states.isEmpty()) {
			return states.get(0);
		}

		return null;
	}

	public Response create(State state) {
		WeDeploy weDeploy = new WeDeploy(BASE_STATES_DATA_PATH);

		JoddJsonSerializer serializer = new JoddJsonSerializer();

		String body = serializer.serialize(state);

		Response response = weDeploy
			.header("Content-Type", "application/json; charset=UTF-8")
			.header("Content-Length", Long.toString(body.length()))
			.post(body);

		return response;
	}

	public Response save(State state) {
		WeDeploy weDeploy = new WeDeploy(BASE_STATES_DATA_PATH+state.getId());

		JoddJsonSerializer serializer = new JoddJsonSerializer();

		String body = serializer.serialize(state);

		Response response = weDeploy
			.header("Content-Type", "application/json; charset=UTF-8")
			.header("Content-Length", Long.toString(body.length()))
			.put(body);

		return response;
	}

	private DataRepository() {
		ApiClient.init();
	}

	private static final String BASE_STATES_DATA_PATH = "data/states";

	private static DataRepository instance = new DataRepository();

}
