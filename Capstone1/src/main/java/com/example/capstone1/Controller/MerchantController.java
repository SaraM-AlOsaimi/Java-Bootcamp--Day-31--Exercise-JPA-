package com.example.capstone1.Controller;

import com.example.capstone1.ApiResponse.ApiResponse;
import com.example.capstone1.Model.Merchant;
import com.example.capstone1.Service.MerchantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/merchant")
@RequiredArgsConstructor
public class MerchantController {

    private final MerchantService merchantService;

    @GetMapping("/get")
    public ResponseEntity<?> getMerchants(){
        return ResponseEntity.status(200).body(merchantService.getMerchants());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addMerchant(@RequestBody @Valid Merchant merchant , Errors errors){
        if(errors.hasErrors()){
            return  ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());
        }
        merchantService.addMerchant(merchant);
        return ResponseEntity.status(200).body(new ApiResponse("Merchant added"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateMerchant(@PathVariable Integer id , @RequestBody @Valid Merchant merchant , Errors errors){
        if(errors.hasErrors()){
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());
        }
        Boolean isUpdated = merchantService.updateMerchant(id , merchant);
        if(isUpdated){
            return ResponseEntity.status(200).body(new ApiResponse("Merchant updated"));
        }
        return ResponseEntity.status(400).body(new ApiResponse("Merchant not found"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteMerchant(@PathVariable Integer id){
        Boolean isDeleted = merchantService.deleteMerchant(id);
        if(isDeleted){
            return ResponseEntity.status(200).body(new ApiResponse("Merchant deleted"));
        }
        return ResponseEntity.status(400).body(new ApiResponse("Merchant not found"));
    }


    // 3- Third Extra endpoint // Put
    @PutMapping("/apply-discount/{merchant_id}/{product_id}/{discountAmount}")
    public ResponseEntity<?> applyDiscount(@PathVariable Integer merchant_id, @PathVariable Integer product_id, @PathVariable Integer discountAmount) {
        Integer result = merchantService.applyDiscount(merchant_id, product_id, discountAmount);
        if (result == 1) {
            return ResponseEntity.status(200).body(new ApiResponse("Discount applied successfully"));
        } else if (result == 2) {
            return ResponseEntity.status(400).body(new ApiResponse("Product not in stock or invalid stock"));
        } else if (result == 3) {
            return ResponseEntity.status(400).body(new ApiResponse("Product not found"));
        } else if (result == 4) {
            return ResponseEntity.status(400).body(new ApiResponse("Merchant not found"));
        } else
            return ResponseEntity.status(400).body(new ApiResponse("Invalid discount amount"));
    }
}
