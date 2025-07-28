package com.example.recipesharing.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.recipesharing.entity.Recipe;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    // 検索用のメソッドも追加可能（必要に応じて）
	List<Recipe> findByTitleContaining(String keyword);
}
