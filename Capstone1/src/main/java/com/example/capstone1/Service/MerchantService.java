package com.example.capstone1.Service;

import com.example.capstone1.Model.Merchant;
import com.example.capstone1.Model.MerchantStock;
import com.example.capstone1.Model.Product;
import com.example.capstone1.Repository.MerchantRepository;
import com.example.capstone1.Repository.MerchantStockRepository;
import com.example.capstone1.Repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MerchantService {

    private final MerchantRepository merchantRepository;
    private final MerchantStockRepository merchantStockRepository;
    private final ProductRepository productRepository;


    public List<Merchant> getMerchants(){
        return merchantRepository.findAll();
    }

    public void addMerchant(Merchant merchant){
        merchantRepository.save(merchant);
    }

    public Boolean updateMerchant(Integer id , Merchant merchant){
        Merchant merchant1 = merchantRepository.getById(id);
        if(merchant1 != null){
            merchant1.setName(merchant.getName());
            merchantRepository.save(merchant1);
            return true;
        }
        return false;
    }

    public Boolean deleteMerchant(Integer id){
        Merchant merchant = merchantRepository.getById(id);
        if(merchant == null){
            return false;
        }
        merchantRepository.delete(merchant);
        return true;
    }

//---------------------------------------
    // 3- Third extra endpoint
    // This method allows a merchant to apply a discount on a specific product.
    public Integer applyDiscount(Integer merchant_id,Integer product_id, Integer discountAmount) {
        for (Merchant merchant : merchantRepository.findAll()) {
            if (merchant.getId().equals(merchant_id)) {  // check if the merchant exists
                 // Ensure that the product exists and in the stock
                  for (MerchantStock merchantStock : merchantStockRepository.findAll()) {
                     if (merchantStock.getProduct_id().equals(product_id) && merchantStock.getStock() > 0) {
                         // Check for valid discount amount
                         for (Product product : productRepository.findAll()) {
                             if (discountAmount > 0 && discountAmount <= product.getPrice()) {
                                 // Apply the discount to the product price
                                 product.setPrice(product.getPrice() - discountAmount);
                                 productRepository.save(product);
                                 return 1; // Successfully applied discount
                             }
                             return 5; // Invalid discount amount
                         }
                     }
                  }
               return 2; // Product not in stock or stock is invalid
            }
        }
        return 4; // Merchant not found
    }

}
