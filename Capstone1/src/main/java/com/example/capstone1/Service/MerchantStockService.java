package com.example.capstone1.Service;

import com.example.capstone1.Model.Merchant;
import com.example.capstone1.Model.MerchantStock;
import com.example.capstone1.Model.Product;
import com.example.capstone1.Repository.MerchantRepository;
import com.example.capstone1.Repository.MerchantStockRepository;
import com.example.capstone1.Repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MerchantStockService {


 private final MerchantStockRepository merchantStockRepository;
    private final ProductRepository productRepository;
    private final MerchantRepository merchantRepository;


 public List<MerchantStock> getMerchantsStocks(){
     return merchantStockRepository.findAll();
 }


    public Integer addMerchantStocks(MerchantStock merchantStock){
        for (Product product : productRepository.findAll()){
            if(merchantStock.getProduct_id().equals(product.getId())){  // check if the product is exist
                for (Merchant merchant :merchantRepository.findAll()){
                    if(merchant.getId().equals(merchantStock.getMerchant_id())){  // check if the merchant is exist
                        merchantStockRepository.save(merchantStock);
                        return 1; // the product and merchant both exist, stock added successfully
                    }
                }
                return 2; // merchant not found
            }
        }
        return 3; // product not found
    }

    public boolean updateMerchantStocks(Integer id, MerchantStock merchantStock){
     MerchantStock oldStock = merchantStockRepository.getById(id);
     if(oldStock == null){
         return false;
        }
        oldStock.setMerchant_id(merchantStock.getMerchant_id());
        oldStock.setProduct_id(merchantStock.getProduct_id());
        oldStock.setStock(merchantStock.getStock());
        merchantStockRepository.save(oldStock);
        return true;
    }

    // delete merchantStocks
    public boolean deleteMerchantStocks(Integer id){
        MerchantStock oldStock = merchantStockRepository.getById(id);
        if(oldStock == null){
            return false;
        }
        merchantStockRepository.delete(oldStock);
        return true;
    }

    // endpoint where merchant can add more stocks of product to a merchant Stock
    // this endpoint should accept a product id and merchant id and the amount of additional stock.
    public Boolean addStockOfProductToMerchantStock(Integer product_id ,Integer merchant_id , Integer amountOfAdditionalStock){
        for (MerchantStock merchantStock : merchantStockRepository.findAll()){
          if(merchantStock.getProduct_id().equals(product_id) && merchantStock.getMerchant_id().equals(merchant_id)){
              merchantStock.setStock(merchantStock.getStock() + amountOfAdditionalStock);
              merchantStockRepository.save(merchantStock);
              return true;
             }
           }
        return false; // merchant or product not found
    }

}

