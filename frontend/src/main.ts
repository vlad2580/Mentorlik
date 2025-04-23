import { bootstrapApplication }   from '@angular/platform-browser';
import { importProvidersFrom }     from '@angular/core';
import { provideRouter }           from '@angular/router';
import { AppComponent }            from './app/app.component';
import { routes }                  from './app/app.routes';
import { HttpClientModule }        from '@angular/common/http';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ToastrModule }            from 'ngx-toastr';

bootstrapApplication(AppComponent, {
  providers: [
    importProvidersFrom(
      HttpClientModule,
      BrowserAnimationsModule,
      ToastrModule.forRoot({
        positionClass: 'toast-top-right',
        timeOut: 3000,
      })
    ),
    provideRouter(routes),
  ]
})
.catch(err => console.error(err));
