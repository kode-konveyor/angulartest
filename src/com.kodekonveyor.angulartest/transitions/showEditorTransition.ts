import produce from "immer";
import { AppState } from "../types/AppState";

export function showEditorTransition(
  state: AppState,
): AppState {
  return produce(state, draft => {
    draft.componentstates.heroeditor = {
      createMode: true,
      show: true,
      selectedHeroId: "",
      selectedHeroName: ""
    }
  })
}
