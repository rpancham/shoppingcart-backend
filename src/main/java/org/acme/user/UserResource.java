package org.acme.user;

import org.acme.data.User;
import org.acme.security.TokenService;
import org.jboss.resteasy.plugins.server.servlet.ServletSecurityContext;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {
    @Inject
    TokenService service;

    @POST
    @Path("/signup")
    @Transactional
    public User register(User user) {
        user.persist(); // super simplified registration, no checks of uniqueness
        return user;
    }

    @GET
    @Path("/user")
    public String getUser(@Context ServletSecurityContext ctx) {
        // super simplified registration, no checks of uniqueness
        System.out.println("x");
        return ctx.getUserPrincipal().getName();
    }

    @GET
    @Path("/login")
    public String login(@QueryParam("email") String email, @QueryParam("password") String password) {
        System.out.println("Email: " + email);
        System.out.println("Password: " + password);
        User existingUser = User.find("email", email).firstResult();
        if (existingUser == null || !existingUser.getPassword().equals(password)) {
            throw new WebApplicationException(
                    Response.status(404).entity("No user found or password is incorrect").build());
        }
        return service.generateUserToken(existingUser.getEmail(), email);
    }
}
