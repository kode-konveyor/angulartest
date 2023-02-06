import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './routing';
import { FormsModule } from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';

import { HeroesComponent } from './UI/heroes/heroes.component';
import { HeroeditorComponent } from './UI/heroeditor/heroeditor.component';
import { HeroitemComponent } from './UI/heroitem/heroitem.component';
import { HeroListComponent } from './UI/herolist/herolist.component';
import { HeroFilterComponent } from './UI/herofilter/herofilter.component';
import { StoreModule } from '@ngrx/store';
import { ObtainHeroesService } from './services/ObtainHeroesService';
import { Synchronizer } from '../com.kodekonveyor.common/Synchronizer';
import { AuthHttpInterceptor, AuthModule } from '@auth0/auth0-angular';
import { AuthButtonComponent } from './UI/AppAuthButton';
import { UserProfileComponent } from './UI/UserProfileComponent';
import { Pipelines } from './Pipelines';
import { GenericErrorHandlerService } from 'src/com.kodekonveyor.common/GenericErrorHandlerService';
import { repository } from './repositories/Repository';
import { EffectsModule } from '@ngrx/effects';
import { ChangeUserEffect } from './effects/ChangeUserEffect';
import { SaveHeroService } from './services/SaveHeroService';
import { CreateHeroEffect } from './effects/CreateHeroEfffect';
import { GenericErrorHandlerServiceEmmitter } from 'src/com.kodekonveyor.common/GenericErrorHandlerServiceEmitter';
import { StoreHeroesEffect } from './effects/StoreHeroesEffect';

@NgModule({
  declarations: [
    HeroesComponent,
    HeroeditorComponent,
    HeroitemComponent,
    HeroListComponent,
    HeroFilterComponent,
    AuthButtonComponent,
    UserProfileComponent
  ],
  imports: [
    EffectsModule.forRoot([ChangeUserEffect, CreateHeroEffect, Pipelines, StoreHeroesEffect]),
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    StoreModule.forRoot({ r: repository }),
    AuthModule.forRoot({
      domain: 'kode-konveyor.eu.auth0.com',
      clientId: 'OqUGGMvs9Ch8yitD3sf2lm6mN61MZqPw',
      authorizationParams: {
        redirect_uri: window.location.href,
        audience: 'https://test.kodekonveyor.com/angulartest',
        scope: 'read:current_user'
      },
      httpInterceptor: {
        allowedList: [
          {
            uri: 'http://localhost:9090/angulartest/api/v1/hero',
            tokenOptions: {
              authorizationParams: {
                audience: 'https://test.kodekonveyor.com/angulartest',
                scope: 'read:current_user'
              }
            }
          },
          {
            uri: '/angulartest/api/v1/hero',
            tokenOptions: {
              authorizationParams: {
                audience: 'https://test.kodekonveyor.com/angulartest',
                scope: 'read:current_user'
              }
            }
          }
        ]
      }
    })
  ],
  providers: [
    GenericErrorHandlerService,
    GenericErrorHandlerServiceEmmitter,
    ObtainHeroesService,
    SaveHeroService,
    Synchronizer,
    { provide: HTTP_INTERCEPTORS, useClass: AuthHttpInterceptor, multi: true },
  ],
  bootstrap: [
    AuthButtonComponent,
    UserProfileComponent,
    HeroesComponent
  ]
})
export class Angulartest { }
