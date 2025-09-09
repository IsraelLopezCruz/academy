package com.priceshoes.academy.repository;

import com.priceshoes.academy.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByPriorityGreaterThanEqualAndIdIsNot(Integer priority, Long categoryId);

    List<Category> findByPriorityGreaterThanEqual(Integer priority);
}
