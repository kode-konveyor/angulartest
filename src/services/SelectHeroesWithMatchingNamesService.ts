import { Injectable } from '@angular/core';
import { HeroesRepository } from 'src/app/repositories/HeroesRepository';
import { HeroFilterRepository } from 'src/app/repositories/HeroFilterRepository';
import { SelectedHeroRepository } from 'src/app/repositories/SelectedHeroRepository';
import { Heroes } from 'src/types/Heroes';
import { HeroFilter } from 'src/types/HeroFilter';

@Injectable()
export class SelectHeroesWithMatchingNamesService {
    heroesRepository: HeroesRepository;
    heroFilterRepository: HeroFilterRepository;

    constructor(heroesRepository: HeroesRepository, heroFilterRepository: HeroFilterRepository) {
        this.heroesRepository = heroesRepository;
        this.heroFilterRepository = heroFilterRepository;
    }

    run(): Heroes {
        console.log("filtering")
        const filter = this.heroFilterRepository.heroFilter;
        if (!filter)
            return [];
        const heroes: Heroes = []
        this.heroesRepository.heroes.forEach(hero => {
            if (hero.name.match(filter.filterString))
                heroes.push(hero)
        });
        console.log(heroes)
        return heroes;
    }
}

