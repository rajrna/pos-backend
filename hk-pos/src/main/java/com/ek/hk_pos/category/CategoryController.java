package com.ek.hk_pos.category;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getAll(){
//        return  new ResponseEntity<>(categoryService.findAll(), HttpStatus.OK);
        return  ResponseEntity.ok(categoryService.findAll());
    }

    @GetMapping("/categories/{id}")
    public ResponseEntity<Category> getById(@PathVariable Long id){
        return ResponseEntity.ok(categoryService.findById(id));
    }

    @PostMapping("/categories")
    public ResponseEntity<Category> create(@RequestBody Category category){
//        return new ResponseEntity<>(categoryService.create(category), HttpStatus.CREATED);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.create(category));
    }

    @PutMapping("/categories/{id}")
    public ResponseEntity<Category> update(@PathVariable Long id, @RequestBody Category category){
//        return new ResponseEntity<>(categoryService.update(id, category), HttpStatus.OK);
        return ResponseEntity.ok(categoryService.update(id,category));
    }

    @DeleteMapping("/categories/{id}")
    public  ResponseEntity<Void> delete(@PathVariable Long id){
        categoryService.delete(id);
        return  ResponseEntity.noContent().build();
    }
}
