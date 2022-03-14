package org.acme.cart;

import org.acme.data.Cart;
import org.acme.data.Product;
import org.acme.data.User;
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

@Path("/Cart")
public class CartResource {

    @Inject
    JsonWebToken jwt;

    @Claim(standard = Claims.preferred_username)
    String username;

    @POST
    @Path("/cart/{product_id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public void addItemstoCart(@Context SecurityContext ct, @PathParam("product_id") Long product_id) {
        System.out.println("pid:" + product_id);
        Cart cart = new Cart();
        Product product = Product.findById(product_id);
        System.out.println("pname:" + product.getTitle());
        String uname = ct.getUserPrincipal().getName();
        User user = User.find("email", uname).firstResult();
        cart.setUser(user);
        cart.setProduct(product);
        cart.setQuantity(1);
        cart.setTitle(product.getTitle());
        Cart.persist(cart);
    }

    @GET
    @Path("/cart/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Cart findCartById(@PathParam("id") long id) {
        return Cart.findById(id);
    }

    @GET
    @Path("/cart")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public List<Cart> getAllCart() {
        return Cart.findAll().list();
    }

    @GET
    @Path("/cart/items")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public List<Cart> getUserCartItems(@Context SecurityContext ct) {
        String uname = ct.getUserPrincipal().getName();
        User user = User.find("email", uname).firstResult();
        return Cart.find("user_id", user.id).list();
    }

    @DELETE
    @Path("/cart")
    @Produces(MediaType.APPLICATION_JSON)
    public void deleteAllCart() {
        Cart.deleteAll();
    }

    @PUT
    @Path("/cart/{id}/{d}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Cart updateCart(@PathParam("id") long id, @PathParam("d") int d) {
        Cart existingCart = Cart.findById(id);
        existingCart.setQuantity(existingCart.getQuantity() + d);
        if (existingCart.getQuantity() <= 0) {
            Cart.deleteById(existingCart.id);
        } else {

            Cart.persist(existingCart);
        }
        return existingCart;
    }

    @DELETE
    @Path("/cart/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public boolean deleteCart(@PathParam("id") long id) {
        System.out.println("cid:" + id);
        return Cart.deleteById(id);
    }
}
