package com.example.capstone1.Controller;

import com.example.capstone1.ApiResponse.ApiResponse;
import com.example.capstone1.Model.Product;
import com.example.capstone1.Model.User;
import com.example.capstone1.Service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/get")
    public ResponseEntity<?> getUsers(){
        return ResponseEntity.status(200).body(userService.getUsers());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addUser(@RequestBody @Valid User user , Errors errors){
        if(errors.hasErrors()){
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());
        }
        userService.addUser(user);
        return ResponseEntity.status(200).body(new ApiResponse("User added"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Integer id , @RequestBody @Valid User user , Errors errors){
        if(errors.hasErrors()){
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());
        }
        Boolean isUpdated = userService.updateUser(id , user);
        if(isUpdated){
            return ResponseEntity.status(200).body(new ApiResponse("User Updated"));
        }
        return ResponseEntity.status(400).body(new ApiResponse("User not found"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer id){
        Boolean isDeleted = userService.deleteUser(id);
        if(isDeleted){
            return ResponseEntity.status(200).body(new ApiResponse("User Deleted"));
        }
        return ResponseEntity.status(400).body(new ApiResponse("User not found"));
    }

    @PutMapping("/buy/{user_id}/{product_id}/{merchant_id}")
    public ResponseEntity<?> buyProduct(@PathVariable Integer user_id ,@PathVariable Integer product_id ,@PathVariable Integer merchant_id){
        Integer isHeBuy = userService.buyProduct(user_id , product_id,merchant_id);
        if(isHeBuy == 1){
        return ResponseEntity.status(200).body(new ApiResponse("Product purchased successfully"));
        }
        if(isHeBuy == 2){
            return ResponseEntity.status(400).body(new ApiResponse("Unable to complete the purchase due to insufficient balance"));
        }
        if(isHeBuy == 3){
            return ResponseEntity.status(400).body(new ApiResponse("Product is out of stock"));
        }
        return ResponseEntity.status(400).body(new ApiResponse("user not found"));
    }



    //------------------------------
    // Extra endpoints
    // 1- First extra endpoint //Get
    @GetMapping("/get/product/by/{category_id}/{min_price}/{max_price}")
    public ResponseEntity<?> getProductsByCategoryAndPriceRange(@PathVariable Integer category_id,@PathVariable Double min_price,@PathVariable Double max_price){
        List<Product> productsByCategory = userService.getProductsByCategoryAndPriceRange(category_id,min_price,max_price);
        if(productsByCategory == null){
            return ResponseEntity.status(400).body(new ApiResponse("No products in the given category with the specified price range"));
        }
        return ResponseEntity.status(200).body(productsByCategory);
    }




    // 2- Second extra endpoint //Put
    @PutMapping("/rating/{user_id}/{product_id}/{rating_scale}")
    public ResponseEntity<?> ratingProduct(@PathVariable Integer user_id, @PathVariable Integer product_id, @PathVariable Integer rating_scale){
        if (rating_scale < 1 || rating_scale > 5) {
            return ResponseEntity.status(400).body(new ApiResponse("Invalid Rating scale"));
        }
        String rating = userService.ratingProduct(user_id,product_id,rating_scale);
        if(rating.equals("A")){
            return ResponseEntity.status(200).body(new ApiResponse("Thank you for your rating!"));
        }
        if(rating.equals("B")){
            return ResponseEntity.status(400).body(new ApiResponse("Product you trying to rate not found"));
        }
        return ResponseEntity.status(400).body(new ApiResponse("User not found or not registered"));
    }

    // 4 - Forth Extra endpoint // Put
    @PutMapping("/return/{user_id}/{product_id}/{merchant_id}/{payment_date}")
    public ResponseEntity<?> returnProduct(@PathVariable Integer user_id, @PathVariable Integer product_id, @PathVariable Integer merchant_id,@PathVariable LocalDate payment_date){
        Integer returned = userService.returnProduct(user_id,product_id,merchant_id,payment_date);
        if(returned == 1){
            return ResponseEntity.status(200).body(new ApiResponse("Return process done successfully"));
        } else if(returned == 2){
            return ResponseEntity.status(400).body(new ApiResponse("Return period has passed"));
        } else if(returned == 3){
            return ResponseEntity.status(400).body(new ApiResponse("Merchant not found"));
        } else if(returned == 4){
            return ResponseEntity.status(400).body(new ApiResponse("Product not found"));
        }
        return ResponseEntity.status(400).body(new ApiResponse("User not found"));
    }

    @GetMapping("/best/seller")
    public ResponseEntity<?> getBestSellers(){
        List bestSeller = userService.getBestSellers();
        if(bestSeller == null){
            return ResponseEntity.status(400).body(new ApiResponse("Not Products"));
        }
        return ResponseEntity.status(200).body(bestSeller);
    }


}
