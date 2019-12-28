package com.okanciftci.cukatify.services.abstr;

import com.okanciftci.cukatify.entities.mongo.Category;

import java.util.List;

public interface CategoryService {

    List<Category> takeAllCategories();

    Category findCategoryById(String id);
}
