/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.guacamole.rest.connectiongroup;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * A simple connection group to expose through the REST endpoints.
 * 
 * @author James Muehlner
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class APIConnectionGroupUsers {
    
    private List<String> addUsers = new ArrayList<String>(); 
    
    private List<String> removeUsers=new ArrayList<String>();
    
	public List<String> getAddUsers() {
		return addUsers;
	}
	public void setAddUsers(List<String> addUsers) {
		this.addUsers = addUsers;
	}
	public List<String> getRemoveUsers() {
		return removeUsers;
	}
	public void setRemoveUsers(List<String> removeUsers) {
		this.removeUsers = removeUsers;
	}
	@Override
	public String toString() {
		return "APIConnectionGroupUsers [addUsers=" + addUsers + ", removeUsers=" + removeUsers + "]";
	} 

}
