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

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

import java.util.HashMap;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.net.auth.ConnectionGroup;
import org.apache.guacamole.net.auth.Directory;
import org.apache.guacamole.net.auth.Identifiable;
import org.apache.guacamole.net.auth.User;
import org.apache.guacamole.net.auth.UserContext;
import org.apache.guacamole.net.auth.permission.ObjectPermission;
import org.apache.guacamole.net.auth.permission.ObjectPermission.Type;
import org.apache.guacamole.net.auth.simple.SimpleUser;
import org.apache.guacamole.rest.directory.DirectoryObjectResource;
import org.apache.guacamole.rest.directory.DirectoryObjectResourceFactory;
import org.apache.guacamole.rest.directory.DirectoryObjectTranslator;
import org.apache.guacamole.rest.directory.DirectoryResource;
import org.apache.guacamole.rest.directory.DirectoryResourceFactory;
import org.apache.guacamole.rest.permission.PermissionSetPatch;
import org.apache.guacamole.rest.user.APIUser;

/**
 * A REST resource which abstracts the operations available on a Directory of
 * ConnectionGroups.
 *
 * @author Michael Jumper
 */
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ConnectionGroupDirectoryResource
        extends DirectoryResource<ConnectionGroup, APIConnectionGroup> {

    /**
     * The UserContext associated with the Directory which contains the
     * ConnectionGroup exposed by this resource.
     */
    private final UserContext userContext;

    /**
     * The Directory exposed by this resource.
     */
    private final Directory<ConnectionGroup> directory;
    @Inject
    private DirectoryResourceFactory<User, APIUser> userDirectoryResourceFactory;

    /**
     * A factory which can be used to create instances of resources representing
     * ConnectionGroups.
     */
    private final DirectoryObjectResourceFactory<ConnectionGroup, APIConnectionGroup> resourceFactory;

    /**
     * Creates a new ConnectionGroupDirectoryResource which exposes the
     * operations and subresources available for the given ConnectionGroup
     * Directory.
     *
     * @param userContext
     *     The UserContext associated with the given Directory.
     *
     * @param directory
     *     The Directory being exposed.
     *
     * @param translator
     *     A DirectoryObjectTranslator implementation which handles
     *     ConnectionGroups.
     *
     * @param resourceFactory
     *     A factory which can be used to create instances of resources
     *     representing ConnectionGroups.
     */
    @AssistedInject
    public ConnectionGroupDirectoryResource(@Assisted UserContext userContext,
            @Assisted Directory<ConnectionGroup> directory,
            DirectoryObjectTranslator<ConnectionGroup, APIConnectionGroup> translator,
            DirectoryObjectResourceFactory<ConnectionGroup, APIConnectionGroup> resourceFactory) {
        super(userContext, directory, translator, resourceFactory);
        this.userContext = userContext;
        this.directory = directory;
        this.resourceFactory = resourceFactory;
    }

    @Override
    public DirectoryObjectResource<ConnectionGroup, APIConnectionGroup>
        getObjectResource(String identifier) throws GuacamoleException {

        // Use root group if identifier is the standard root identifier
        if (identifier != null && identifier.equals(APIConnectionGroup.ROOT_IDENTIFIER))
            return resourceFactory.create(userContext, directory,
                    userContext.getRootConnectionGroup());

        return super.getObjectResource(identifier);

    }
    @GET
    @Path("{identifier}/assign")
    public APIConnectionGroupUsers getConnectionGroupUsers(@PathParam("identifier") String identifier) throws GuacamoleException {
    	ConnectionGroup connectionGroup = this.directory.get(identifier);
		List<String> users = connectionGroup.getUsers();
    	APIConnectionGroupUsers apiConnectionGroupUsers = new APIConnectionGroupUsers();
    	apiConnectionGroupUsers.setAddUsers(users);
    	System.out.println("GOT USERS "+users);
    	return apiConnectionGroupUsers;
    }
    @PUT
    @Path("{identifier}/assign")
    public void assignUsers(@PathParam("identifier") String identifier,APIConnectionGroupUsers modifiedObject) throws GuacamoleException {
		System.out.println("((********************))" + modifiedObject);
		ConnectionGroup connectionGroup=this.directory.get(identifier);;
		for (String addUser : modifiedObject.getAddUsers()) {
			try {
				User user = this.userContext.getUserDirectory().get(addUser);
				if (user == null) {
					DirectoryResource<User, APIUser> create = userDirectoryResourceFactory.create(userContext,
							userContext.getUserDirectory());
					System.out.println("((********CREATE DIR************))" + create);
					APIUser apiUser = new APIUser();
					apiUser.setUsername(addUser);
					apiUser.setAttributes(new HashMap<String, String>());
					create.createObject(apiUser);
				}
				user = this.userContext.getUserDirectory().get(addUser);
				System.out.println("((********USER now************))"+user);
				PermissionSetPatch<ObjectPermission> connectionGroupUserPermissionPatch = new PermissionSetPatch<ObjectPermission>();
				connectionGroupUserPermissionPatch
						.addPermission(new ObjectPermission(ObjectPermission.Type.READ, connectionGroup.getIdentifier()));
				connectionGroupUserPermissionPatch.apply(user.getConnectionGroupPermissions());
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		for (String addUser : modifiedObject.getRemoveUsers()) {
			User user = this.userContext.getUserDirectory().get(addUser);
			if (user != null) {
				PermissionSetPatch<ObjectPermission> connectionGroupUserPermissionPatch = new PermissionSetPatch<ObjectPermission>();
				for (Type type : ObjectPermission.Type.values()) {
					connectionGroupUserPermissionPatch.removePermission( new ObjectPermission(type, connectionGroup.getIdentifier()));
				}
				connectionGroupUserPermissionPatch.apply(user.getConnectionGroupPermissions());
			}
		}
    }
}
