package org.acme.data;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class Wishlist extends PanacheEntity {


    public Wishlist(User user, Product product) {
        this.user = user;
        this.product = product;


    }

    @OneToOne
    private User user;
    @OneToOne
    private Product product;
    private String title;

    public Wishlist() {

    }
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }



}