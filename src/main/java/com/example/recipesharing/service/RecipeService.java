package com.example.recipesharing.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.recipesharing.entity.Recipe;
import com.example.recipesharing.repository.RecipeRepository;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;

    // コンストラクタインジェクション（推奨）
    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    // レシピ一覧を取得
    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }

    // レシピを保存
    public Recipe saveRecipe(Recipe recipe) {
        return recipeRepository.save(recipe);
    }

    // タイトルによる検索
    public List<Recipe> searchByTitle(String keyword) {
        return recipeRepository.findByTitleContaining(keyword);
    }

    // IDでレシピを取得（詳細ページなどで使用）
    public Recipe getRecipeById(Long id) {
        return recipeRepository.findById(id).orElse(null);
    }
    
 // レシピを削除
    public void deleteRecipe(Long id) {
        recipeRepository.deleteById(id);
    }

}