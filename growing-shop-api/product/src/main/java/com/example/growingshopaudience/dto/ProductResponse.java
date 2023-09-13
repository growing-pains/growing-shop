package com.example.growingshopaudience.dto;

import com.example.domain.product.Product;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductResponse {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class ProductRes {
        private long id;
        private String name;
        private int price;
//        private CompanyResponse.CompanyRes company;

        public static ProductRes from(Product product) {
            ProductRes res = new ProductRes();

            res.id = product.getId();
            res.name = product.getName();
            res.price = product.getPrice();

//            if (product.getCompany() != null) {
//                res.company= CompanyResponse.CompanyRes.from(product.getCompany());
//            }

            return res;
        }
    }
}
