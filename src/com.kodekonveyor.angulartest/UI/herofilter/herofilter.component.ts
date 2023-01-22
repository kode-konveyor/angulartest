import { Component, Input } from '@angular/core'
import { Store } from '@ngrx/store';
import { setHeroFilter } from 'src/com.kodekonveyor.angulartest/repositories/actions';
import { AppStore } from 'src/com.kodekonveyor.angulartest/repositories/AppStore';

@Component({
  selector: 'herofilter',
  templateUrl: './herofilter.component.html'
})
export class HeroFilterComponent {

  @Input() id!: string;
  heroFilter: String = "";

  constructor(
    private readonly store: Store<AppStore>
  ) { }

  onInput(): void {
    this.store.dispatch(setHeroFilter({ heroFilter: this.heroFilter }))
  }
}


