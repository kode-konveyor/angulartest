import { Hero } from '../types/Hero';
import { EventEmitter, Injectable } from '@angular/core';
import { obtainHeroesService } from './ObtainHeroesService';

@Injectable()
export class HeroesRepository {
  private _heroes!: Hero[];
  public readonly heroesEvent = new EventEmitter<Hero[]>();

  public addHero(hero: Hero): void {
    this.initializeIfUndefined();
    this._heroes.push(hero)
    this.heroesEvent.emit(this._heroes)
  }

  private initializeIfUndefined(): void {
    if (this._heroes === undefined) {
      this._heroes = obtainHeroesService();
    }
  }

  get heroes(): Hero[] {
    this.initializeIfUndefined();
    return this._heroes.slice()
  }

}

