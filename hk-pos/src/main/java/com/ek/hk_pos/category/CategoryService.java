package com.ek.hk_pos.category;

import com.ek.hk_pos.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<Category> findAll(){
        return categoryRepository.findAll();
    }

    public Category findById(Long id){
        return categoryRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Category not found with id: "+id));
    }

    public Category create(Category category){
        return categoryRepository.save(category);
    }

    public Category update(Long id, Category updated){
        Category existing = findById(id);
        existing.setName(updated.getName());
        existing.setDescription(updated.getDescription());
        return  categoryRepository.save(existing);
    }

    public void delete(Long id){
        findById(id);
        categoryRepository.deleteById(id);
    }
}
