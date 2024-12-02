package com.example.capstone1.Service;

import com.example.capstone1.Model.Category;
import com.example.capstone1.Model.Product;
import com.example.capstone1.Repository.CategoryRepository;
import com.example.capstone1.Repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public List<Product> getProducts(){
        return productRepository.findAll();
    }

    public Boolean addProduct(Product product){
        for (Category category : categoryRepository.findAll()){
            if(product.getCategory_id().equals(category.getId())){
                productRepository.save(product);
                return true;
            }
        }
        return false;
    }


    public Integer updateProduct(Integer id , Product product){
        Product oldProduct = productRepository.getById(id);
        if(oldProduct != null) {
            for (int i = 0; i < categoryRepository.findAll().size(); i++) {
                if (oldProduct.getCategory_id().equals(categoryRepository.findAll().get(i).getId())) {
                    oldProduct.setName(product.getName());
                    oldProduct.setPrice(product.getPrice());
                    oldProduct.setCategory_id(product.getCategory_id());
                    productRepository.save(oldProduct);
                    return 1;
                }
            }
            return 2;
        }
        return 3;
    }


    public Boolean deleteProduct(Integer id){
      Product oldProduct = productRepository.getById(id);
      if(oldProduct== null){
          return false;
      }
      productRepository.delete(oldProduct);
      return true;
    }


}
