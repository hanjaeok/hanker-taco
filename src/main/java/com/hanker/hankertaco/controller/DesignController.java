package com.hanker.hankertaco.controller;

import com.hanker.hankertaco.domain.Ingredient;
import com.hanker.hankertaco.domain.Order;
import com.hanker.hankertaco.domain.Taco;
import com.hanker.hankertaco.domain.User;
import com.hanker.hankertaco.repository.IngredientRepository;
import com.hanker.hankertaco.repository.TacoRepository;
import com.hanker.hankertaco.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@Slf4j
@RequestMapping("/design")
@SessionAttributes("order")
public class DesignController {

    private final IngredientRepository ingredientRepository;
    private TacoRepository tacoRepository;
    private UserRepository userRepository;

    @Autowired
    public DesignController(IngredientRepository ingredientRepository, TacoRepository tacoRepository,
                            UserRepository userRepository) {
        this.ingredientRepository = ingredientRepository;
        this.tacoRepository = tacoRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public String showDesignForm(Model model, Principal principal){

        List<Ingredient> ingredients = new ArrayList<>();
        ingredientRepository.findAll().forEach(i -> ingredients.add(i));

        Ingredient.Type[] types = Ingredient.Type.values();

        for(Ingredient.Type type : types){
            model.addAttribute(type.toString().toLowerCase(), filterByType(ingredients, type));
        }

        String username = principal.getName();
        User user = userRepository.findByUsername(username);
        model.addAttribute("user", user);

        return "design";
    }

    private List<Ingredient> filterByType(List<Ingredient> ingredients, Ingredient.Type type){
        return ingredients.stream()
                .filter(x -> x.getType().equals(type))
                .collect(Collectors.toList());
    }

    @ModelAttribute(name = "order")
    public Order order(){
        return new Order();
    }

    @ModelAttribute(name = "taco")
    public Taco taco(){
        return new Taco();
    }

    @PostMapping
    public String processDesign(@Valid Taco design, Errors errors, @ModelAttribute Order order){

        if(errors.hasErrors()){
            return "design";
        }

//        // 선택된 식자재 내역을 저장
//        log.info("Processing design : " + design);
        Taco saved = tacoRepository.save(design);
        order.addDesign(saved);

        return "redirect:/orders/current";
    }
}
