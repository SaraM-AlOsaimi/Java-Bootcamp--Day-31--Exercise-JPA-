package com.example.capstone1.Controller;

import com.example.capstone1.ApiResponse.ApiResponse;
import com.example.capstone1.Model.MerchantStock;
import com.example.capstone1.Service.MerchantStockService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/merchant-stocks")
public class MerchantStockController {

    private final MerchantStockService merchantStockService;


    @GetMapping("/get")
    public ResponseEntity<?> getMerchantStocks(){
        return ResponseEntity.status(200).body(merchantStockService.getMerchantsStocks());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addMerchantStocks(@RequestBody @Valid MerchantStock merchantStock , Errors errors){
        if(errors.hasErrors()){
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());
        }
        Integer addMerchantStocks =  merchantStockService.addMerchantStocks(merchantStock);
        if(addMerchantStocks == 1){
            return ResponseEntity.status(200).body(new ApiResponse("Merchant Stocks added"));
        }
        if (addMerchantStocks == 2){
            return ResponseEntity.status(400).body(new ApiResponse("Merchant not found "));
        }
        return ResponseEntity.status(400).body(new ApiResponse("product not found"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateMerchantStocks(@PathVariable Integer id , @RequestBody @Valid MerchantStock merchantStock,Errors errors){
        if(errors.hasErrors()){
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());
        }
        boolean isUpdated = merchantStockService.updateMerchantStocks(id,merchantStock);
        if(isUpdated){
            return ResponseEntity.status(200).body(new ApiResponse("MerchantStocks Updated "));
        }
        return ResponseEntity.status(400).body(new ApiResponse("ID not found"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteMerchantStocks(@PathVariable Integer id){
        boolean isDeleted = merchantStockService.deleteMerchantStocks(id);
        if(isDeleted){
            return ResponseEntity.status(200).body(new ApiResponse("MerchantStocks Deleted"));
        }
        return ResponseEntity.status(400).body(new ApiResponse("ID not found"));
    }


    @PutMapping("/added/stock/{product_id}/{merchant_id}/{amount_additional_stock}")
    //String productId , String merchantId , Integer amountOfAdditionalStock
    public ResponseEntity<?> addStockOfProductToMerchantStock(@PathVariable Integer product_id , @PathVariable Integer merchant_id , @PathVariable @Positive Integer amount_additional_stock){
        Boolean addedStock = merchantStockService.addStockOfProductToMerchantStock(product_id,merchant_id,amount_additional_stock);
        if(addedStock){
            return ResponseEntity.status(200).body(new ApiResponse("Stock amount updated"));
        }
        return ResponseEntity.status(400).body(new ApiResponse("Merchant stock not found"));
    }



}
