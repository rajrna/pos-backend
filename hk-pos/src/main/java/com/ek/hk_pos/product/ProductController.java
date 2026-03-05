package com.ek.hk_pos.product;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<Product>> getAll(){
        return new ResponseEntity<>(productService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getById(@PathVariable Long id){
        return new ResponseEntity<>(productService.findById(id), HttpStatus.OK);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Product>> getByCategory(@PathVariable Long categoryId){
        return new ResponseEntity<>(productService.findByCategoryId(categoryId), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Product>> search(@RequestParam String name){
        return new ResponseEntity<>(productService.searchByName(name), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Product> create(@RequestBody ProductRequest request){
        return  new ResponseEntity<>(productService.create(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public  ResponseEntity<Product> update(@PathVariable Long id, @RequestBody ProductRequest request){
        return new ResponseEntity<>(productService.update(id, request), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
