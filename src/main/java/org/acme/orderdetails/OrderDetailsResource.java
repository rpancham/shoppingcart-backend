package org.acme.orderdetails;

import org.acme.data.OrderDetails;
import org.acme.data.User;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/OrderDetails")
public class OrderDetailsResource {

    @POST
    @Path("/OrderDetails")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public void addItemstoOrderDetails(OrderDetails orderDetails) {
        OrderDetails.persist(orderDetails);
    }

    @GET
    @Path("/OrderDetails/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public OrderDetails findOrderDetailsById(@PathParam("id") long id) {
        return OrderDetails.findById(id);
    }

    @GET
    @Path("/OrderDetails")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public List<OrderDetails> getAllOrderDetails() {
        return OrderDetails.findAll().list();
    }

    @GET
    @Path("/OrderDetails/items")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public List<OrderDetails> getUserOrderDetailsItems() {
        User user = new User();
        return OrderDetails.find("user_id", user.id).list();
    }

    @GET
    @Path("/OrderDetails/products")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public List<OrderDetails> geOrderDetailsItems(@QueryParam("id") long id) {
        System.out.println("oid:" + id);
        List<OrderDetails> oDetilsList = OrderDetails.find("order_id", id).list();
        for (OrderDetails orderDetails : oDetilsList) {
            {
                System.out.println("Product: " + orderDetails.getProduct().getTitle());
            }

        }
        return oDetilsList;
    }

    @DELETE
    @Path("/OrderDetails")
    @Produces(MediaType.APPLICATION_JSON)
    public void deleteAllOrderDetails() {
        OrderDetails.deleteAll();
    }

    @PUT
    @Path("/OrderDetails")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public OrderDetails updateOrderDetails(OrderDetails orderDetails) {
        OrderDetails existingOrderDetails = OrderDetails.find("product_id", orderDetails.getProduct().id).firstResult();
        if (orderDetails.getQuantity() <= 0) {
            OrderDetails.deleteById(existingOrderDetails.id);
        } else {
            existingOrderDetails.setQuantity(orderDetails.getQuantity());
            OrderDetails.persist(existingOrderDetails);
        }
        return orderDetails;
    }

    @DELETE
    @Path("/OrderDetails/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public boolean deleteOrderDetails(@PathParam("id") long id) {
        return OrderDetails.deleteById(id);
    }
}
