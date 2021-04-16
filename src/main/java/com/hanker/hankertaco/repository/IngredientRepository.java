package com.hanker.hankertaco.repository;

import com.hanker.hankertaco.domain.Ingredient;
import org.springframework.data.repository.CrudRepository;

public interface IngredientRepository extends CrudRepository<Ingredient, String> {

}
