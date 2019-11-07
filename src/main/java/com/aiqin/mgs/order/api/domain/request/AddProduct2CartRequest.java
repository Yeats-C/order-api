package com.aiqin.mgs.order.api.domain.request;

import com.aiqin.mgs.product.api.domain.Product;

public class AddProduct2CartRequest {

    private Product product;
    private Long userId;

//    public Product getProduct() {
//        return product;
//    }

//    public void setProduct(Product product) {
//        this.product = product;
//    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
