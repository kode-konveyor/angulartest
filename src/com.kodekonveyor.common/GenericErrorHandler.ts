import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';


@Injectable()
export class GenericErrorHandler {

  run(error: Error): Observable<never> {
    console.log("GEHS", error);
    return of();
  }
}
