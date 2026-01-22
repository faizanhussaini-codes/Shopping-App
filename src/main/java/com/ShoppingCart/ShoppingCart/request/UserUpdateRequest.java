package com.ShoppingCart.ShoppingCart.request;

import lombok.Data;

@Data
public class UserUpdateRequest {
    private String firstName;
    private String lastName;
    //We will create a special class for updating email and password
}
