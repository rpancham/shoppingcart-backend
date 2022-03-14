package org.acme.data;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class OrderDetails extends PanacheEntity {


    public OrderDetails(CustomerOrder order, Product product, int quantity, float price, float amount) {
        this.order = order;
        this.product = product;
        this.quantity = quantity;
        this.price = price;
        this.amount = amount;

    }

    @OneToOne
    private CustomerOrder order;
    @OneToOne
    private Product product;
    private int quantity;
    private float price;
    private float amount;
    public OrderDetails() {

    }


    public CustomerOrder getOrder() {
        return order;
    }

    public void setOrder(CustomerOrder order) {
        this.order = order;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }


}