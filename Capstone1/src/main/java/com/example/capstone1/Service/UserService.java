package com.example.capstone1.Service;

import com.example.capstone1.Model.Merchant;
import com.example.capstone1.Model.MerchantStock;
import com.example.capstone1.Model.Product;
import com.example.capstone1.Model.User;
import com.example.capstone1.Repository.MerchantStockRepository;
import com.example.capstone1.Repository.ProductRepository;
import com.example.capstone1.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final MerchantStockRepository merchantStockRepository;
    private final ProductRepository productRepository;

    public List<User> getUsers(){
        return userRepository.findAll();
    }

    public void addUser(User user){
        userRepository.save(user);
    }

    public Boolean updateUser(Integer id , User user){
      User oldUser = userRepository.getById(id);
      if (oldUser == null){
          return false;
      }
      oldUser.setUsername(user.getUsername());
      oldUser.setPassword(user.getPassword());
      oldUser.setRole(user.getRole());
      oldUser.setEmail(user.getEmail());
      oldUser.setBalance(user.getBalance());
      userRepository.save(oldUser);
      return true;
    }

    public Boolean deleteUser(Integer id){
       User oldUser = userRepository.getById(id);
       if(oldUser == null){
           return false;
       }
       userRepository.delete(oldUser);
       return true;
    }


    public Product getProductById(Integer product_id){
        for (Product product : productRepository.findAll()){
            if(product.getId().equals(product_id)){
                return product;
            }
        }
        return null;
    }

    private List<Product> productsPurchased;
    public void addProductPurchased(Product product) {
          productsPurchased = new ArrayList<>();
        productsPurchased.add(product);
    }

    public Integer buyProduct(Integer user_id ,Integer product_id ,Integer merchant_id){
        for (User user : userRepository.findAll()){
            if (user.getId().equals(user_id)){
                for (MerchantStock merchantStock : merchantStockRepository.findAll()){
                    if(merchantStock.getStock() > 0 &&
                            merchantStock.getProduct_id().equals(product_id) &&
                            merchantStock.getMerchant_id().equals(merchant_id)){
                        Product product = getProductById(product_id);
                        merchantStock.setStock(merchantStock.getStock() - 1);
                        if(user.getBalance() >= product.getPrice()){
                            user.setBalance(user.getBalance() - product.getPrice());
                            addProductPurchased(product);
                            userRepository.save(user);
                            merchantStockRepository.save(merchantStock);
                            return 1; // everything works well
                        }
                        return 2; // balance is low
                    }
                }
                return 3 ; // out of stock
            }
        }
        return 4;
    }


    //-----------------------------------

    // 1- First extra endpoint
    // This method takes three parameters: (categoryId,minPrice,maxPrice)
    // retrieve a list of products from a specific category within a given price range.
    public List<Product> getProductsByCategoryAndPriceRange(Integer category_id, Double minP_price, Double max_price){
        List<Product> productsByCategory = new ArrayList<>();
        for (Product product : productRepository.findAll()){
            //Apply Filters: For each product in the list:
            if(product.getCategory_id().equals(category_id) && product.getPrice() >= minP_price && product.getPrice() <= max_price) {
                productsByCategory.add(product);
            }
        }
        return productsByCategory.isEmpty() ? null : productsByCategory;
    }

    // Method to update product's rating
    public void updateProductRating(Product product, Integer rating_scale) {
        product.setTotal_rating(product.getTotal_rating() + rating_scale);
        product.setRating_count(product.getRating_count() + 1);
        // Recalculate the average rating
        product.setAverage_rating(product.getTotal_rating() / product.getRating_count());
        productRepository.save(product);
    }

    //2- Second extra endpoint
    // Method allow a user to rate a product by providing a rating scale between 1 and 4 ,
    // and takes three parameters (userId , productId , ratingScale)
    public String ratingProduct(Integer user_id, Integer product_id, Integer rating_scale) {
        for (User user : userRepository.findAll()) {
            if (user.getId().equals(user_id)) { // Check if the user exists
                for (Product product : productsPurchased) {
                    if (product.getId().equals(product_id)) { // Check if the product exists
                        // Update the rating of the product
                        updateProductRating(product, rating_scale);
                        return "A"; // rating successfully
                    }
                }
                return "B"; // product not found in Purchased list
            }
        }
        return "C"; // user not found
    }


    // 4- Forth extra endpoint
    //This method allows a user to return a product they have purchased, provided the product is returned within 30 days from the purchase date.
    // If the return is accepted, the user's account will be refunded, and the product will be restocked in the merchant's inventory.
    public Integer returnProduct(Integer user_id, Integer product_id, Integer merchant_id, LocalDate payment_date) {
      for (User user : userRepository.findAll()){
          if(user.getId().equals(user_id)){
             for(Product product : productsPurchased) {
                 if(product.getId().equals(product_id)){
                     for (MerchantStock merchant : merchantStockRepository.findAll()){
                         if(merchant.getMerchant_id().equals(merchant_id)){
                             long daysSincePayment = ChronoUnit.DAYS.between(payment_date, LocalDate.now());
                             if (daysSincePayment < 30){
                                 merchant.setStock(merchant.getStock() + 1); // Restock the product
                                 user.setBalance(user.getBalance() + product.getPrice()); // Refund the user
                                 productsPurchased.remove(product); // Remove the product from the purchased list
                                 merchantStockRepository.save(merchant);
                                 userRepository.save(user);
                                 return 1;
                             }
                             return 2;
                         }
                     }
                     return 3 ;
                 }
             }
             return 4; // product not found in Purchased list
          }
      }
      return 5; // user not found
    }


    // 5 - Fifth Extra endpoint
    // Method to return list of product that is bestSeller based on totalRating
    public List<Product> getBestSellers() {
        List<Product> products = productRepository.findAll();
        for (int i = 0; i < products.size() - 1; i++) {
            int maxIndex = i;
            for (int j = i + 1; j < products.size(); j++) {
                if (products.get(j).getTotal_rating() > products.get(maxIndex).getTotal_rating()) {
                    maxIndex = j;  // Update maxIndex if we find a product with a higher rating score
                }
            }
            Product temp = products.get(i);
            products.set(i, products.get(maxIndex));
            products.set(maxIndex, temp);
        }
        // Get top 3 best sellers
        int topThree = 3;
        ArrayList<Product> bestSellers = new ArrayList<>();
        for (int i = 0; i < Math.min(topThree, products.size()); i++) {
            bestSellers.add(products.get(i));
        }
        return bestSellers;
    }

}
