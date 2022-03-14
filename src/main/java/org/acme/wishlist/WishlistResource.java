package org.acme.wishlist;

import org.acme.data.Product;
import org.acme.data.User;
import org.acme.data.Wishlist;
import org.eclipse.microprofile.jwt.Claim;
import org.eclipse.microprofile.jwt.Claims;
import org.eclipse.microprofile.jwt.JsonWebToken;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import java.util.List;

@Path("/Wishlist")
public class WishlistResource {

    @Inject
    JsonWebToken jwt;

    @Claim(standard = Claims.preferred_username)
    String username;


    @POST
    @Path("/Wishlist/{product_id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public void addItemstoWishlist(@Context SecurityContext ct, @PathParam("product_id") Long product_id) {
        System.out.println("pid:" + product_id);
        Product product = Product.findById(product_id);
        System.out.println("pname:" + product.getTitle());

        Wishlist wishlist = new Wishlist();
        String uname = ct.getUserPrincipal().getName();
        User user = User.find("email", uname).firstResult();
        wishlist.setUser(user);
        wishlist.setProduct(product);
        wishlist.setTitle(product.getTitle());
        Wishlist.persist(wishlist);

    }

    @GET
    @Path("/Wishlist/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Wishlist findWishlistById(@PathParam("id") long id) {
        return Wishlist.findById(id);
    }

    @GET
    @Path("/Wishlist")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public List<Wishlist> getAllWishlist() {
        return Wishlist.findAll().list();
    }

    @GET
    @Path("/Wishlist/items")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public List<Wishlist> getUserWishlistItems(@Context SecurityContext ct) {
        String uname = ct.getUserPrincipal().getName();
        User user = User.find("email", uname).firstResult();
        return Wishlist.find("user_id", user.id).list();
    }

    @DELETE
    @Path("/Wishlist")
    @Produces(MediaType.APPLICATION_JSON)
    public void deleteAllWishlist() {
        Wishlist.deleteAll();
    }

    @PUT
    @Path("/Wishlist")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Wishlist updateWishlist(Wishlist wishlist) {
        Wishlist existingWishlist = Wishlist.findById(wishlist.getProduct().id);
        Wishlist.persist(existingWishlist);

        return wishlist;
    }

    @DELETE
    @Path("/Wishlist/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public boolean deleteWishlist(@PathParam("id") long id) {
        return Wishlist.deleteById(id);
    }
}
