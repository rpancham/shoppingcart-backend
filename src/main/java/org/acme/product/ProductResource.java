package org.acme.product;

import io.quarkus.panache.common.Sort;
import org.acme.data.Product;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/products")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProductResource {
    @GET
    @Path("/list")
    public List<Product> getProducts() {

        return Product.listAll(Sort.ascending("title"));
    }

    @POST
    @Path("/add")
    @Transactional
    public void addProduct(Product product) {
        Product.persist(product);
    }

    @GET
    @Path("/get/{id}")
    public Product getProduct(int id) {
        return Product.findById(id);
    }
}
