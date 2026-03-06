package com.ek.hk_pos.product;

import com.ek.hk_pos.category.Category;
import com.ek.hk_pos.category.CategoryRepository;
import com.ek.hk_pos.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public List<Product> findAll(){
        return  productRepository.findAll();
    }

    public Product findById(Long id){
        return  productRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Product not found with id: "+ id));
    }

    public List<Product> findByCategoryId(Long categoryId){
        return productRepository.findByCategoryId(categoryId);
    }

    public  List<Product> searchByName(String name){
        return productRepository.findByNameContainingIgnoreCase(name);
    }

    public Product create(ProductRequest request){

        Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow(()-> new ResourceNotFoundException("Category not found with id:" + request.getCategoryId()));

        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .stockQuantity(request.getStockQuantity())
                .category(category)
                .build();

        return productRepository.save(product);
    }

    public Product update(Long id, ProductRequest request){

        Product existing = findById(id);

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(()->new ResourceNotFoundException("Category not found with id: "+ request.getCategoryId()));

        existing.setName(request.getName());
        existing.setDescription(request.getDescription());
        existing.setPrice(request.getPrice());
        existing.setStockQuantity(request.getStockQuantity());
        existing.setCategory(category);

        return productRepository.save(existing);
    }

    public void delete(Long id){
        findById(id);
        productRepository.deleteById(id);
    }



}
