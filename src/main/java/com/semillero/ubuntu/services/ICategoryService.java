package com.semillero.ubuntu.services;

import com.semillero.ubuntu.entities.Category;

import java.util.List;

public interface ICategoryService {

    List<Category> findAll();

}
