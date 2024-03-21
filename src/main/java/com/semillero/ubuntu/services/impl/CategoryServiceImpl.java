package com.semillero.ubuntu.services.impl;

import com.semillero.ubuntu.entities.Category;
import com.semillero.ubuntu.repositories.ICategoryRepository;
import com.semillero.ubuntu.services.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements ICategoryService {

    private final ICategoryRepository categoryRepository;

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }
}
