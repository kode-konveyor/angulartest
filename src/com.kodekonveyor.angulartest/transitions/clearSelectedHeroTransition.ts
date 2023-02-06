import { initialState } from "../repositories/Repository";
import { AppState } from "../types/AppState";

export function clearSelectedHeroTransition(
  state: AppState): AppState {
  return {
    ...state,
    componentstates: {
      ...state.componentstates,
      heroeditor: initialState.componentstates.heroeditor
    }
  };
}
