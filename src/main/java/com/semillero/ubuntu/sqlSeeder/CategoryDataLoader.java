package com.semillero.ubuntu.sqlSeeder;

import com.semillero.ubuntu.entities.Category;
import com.semillero.ubuntu.repositories.ICategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CategoryDataLoader{

    private final ICategoryRepository repository;

    public void loadData(){

        if (repository.count() == 0){

            Category cat1 = new Category();
            Category cat2 = new Category();
            Category cat3 = new Category();
            Category cat4 = new Category();

            cat1.setName("Economía social / Desarrollo local / Inclusión financiera");
            cat2.setName("Agroecología / Orgánicos / Alimentación saludable");
            cat3.setName("Conservación / Regeneración / Servicios ecosistémicos");
            cat4.setName("Empresas / Organismos de impacto / Economía circular");

            List<Category> list = Arrays.asList(cat1, cat2, cat3, cat4);

            repository.saveAll(list);

        }

    }

}
