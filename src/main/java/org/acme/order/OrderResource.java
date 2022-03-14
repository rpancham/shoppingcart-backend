package org.acme.order;

import org.acme.data.Cart;
import org.acme.data.CustomerOrder;
import org.acme.data.OrderDetails;
import org.acme.data.User;
import org.acme.data.Product;
import org.eclipse.microprofile.jwt.Claim;
import org.eclipse.microprofile.jwt.Claims;
import org.eclipse.microprofile.jwt.JsonWebToken;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

import java.util.Date;
import java.util.List;

@Path("/Orders")
public class OrderResource {
    @Inject
    JsonWebToken jwt;

    @Claim(standard = Claims.preferred_username)
    String username;

    @POST
    @Path("/order")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public CustomerOrder addItemstoOrder(@Context SecurityContext ct) {
        System.out.println();
        float order_amount = 0;
        float discount = 0;
        String uname = ct.getUserPrincipal().getName();
        User user = User.find("email", uname).firstResult();
        List<Cart> cart_items1 = Cart.find("user_id", user.id).list();
        for (Cart cart : cart_items1) {
            float amt = cart.getProduct().getPrice() * cart.getQuantity();
            order_amount += amt;
        }
        if (order_amount >= 500 && order_amount < 1000) {
            discount = (5 * order_amount) / 100;
        } else if (order_amount >= 1000) {
            discount = (10 * order_amount) / 100;
        }
        CustomerOrder order = new CustomerOrder();

        order.setUser(user);
        Date d1 = new Date();
        int day = d1.getDate();
        int month = d1.getMonth() + 1;
        int year = d1.getYear() + 1900;
        String cdate = day + "/" + month + "/" + year;
        order.setOrderDate(cdate);
        order.setOrderStatus("Confirmed");
        order.setDiscount(discount);
        order.setOrderAmount(order_amount);
        CustomerOrder.persist(order);

        List<Cart> cat_items = Cart.find("user_id", user.id).list();
        for (Cart cart : cat_items) {
            float price = cart.getProduct().getPrice();
            int quantity = cart.getQuantity();
            float amount = quantity * price;
            Product p = cart.getProduct();
            p.setCurrentStock(p.getCurrentStock() - cart.getQuantity());
            Product.persist(p);
            OrderDetails order_details = new OrderDetails();
            order_details.setOrder(order);
            order_details.setProduct(cart.getProduct());
            order_details.setQuantity(quantity);
            order_details.setPrice(price);
            order_details.setAmount(amount);
            OrderDetails.persist(order_details);
            cart.delete();

        }
        return order;

    }

    @GET
    @Path("/Order/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public CustomerOrder findOrderById(@PathParam("id") long id) {
        return CustomerOrder.findById(id);
    }

    @GET
    @Path("/Order")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public List<CustomerOrder> getAllOrder() {
        return CustomerOrder.findAll().list();
    }

    @GET
    @Path("/Order/items")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public List<CustomerOrder> getUserOrderItems(@Context SecurityContext ct) {
        String uname = ct.getUserPrincipal().getName();
        User user = User.find("email", uname).firstResult();
        return CustomerOrder.find("user_id", user.id).list();
    }

    @DELETE
    @Path("/Order")
    @Produces(MediaType.APPLICATION_JSON)
    public void deleteAllOrder() {
        CustomerOrder.deleteAll();
    }

    @PUT
    @Path("/Order")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public CustomerOrder updateOrder(CustomerOrder order) {
        CustomerOrder existingOrder = CustomerOrder.findById(order.id);

        CustomerOrder.persist(existingOrder);

        return order;
    }

    @DELETE
    @Path("/Order/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public boolean deleteOrder(@PathParam("id") long id) {
        return CustomerOrder.deleteById(id);
    }
}