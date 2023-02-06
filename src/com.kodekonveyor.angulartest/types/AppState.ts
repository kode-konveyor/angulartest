import { ComponentModels } from './ComponentModels';
import { Heroes } from './Heroes';
import { States } from './States';


export interface AppState {
  heroes: Heroes;
  states: States;
  componentstates: ComponentModels
}
