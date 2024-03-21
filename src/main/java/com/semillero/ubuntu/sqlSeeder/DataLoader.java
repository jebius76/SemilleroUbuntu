package com.semillero.ubuntu.sqlSeeder;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final CategoryDataLoader categoryDataLoader;
    private final CountryDataLoader countryDataLoader;
    private final ProvinceDataLoader provinceDataLoader;

    @Override
    public void run(String... args) throws Exception {
            categoryDataLoader.loadData();
            countryDataLoader.loadData();
            provinceDataLoader.loadData();
    }

}
