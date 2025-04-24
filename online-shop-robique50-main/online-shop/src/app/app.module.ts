import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import {
  provideHttpClient,
  withInterceptors,
} from '@angular/common/http';
import { SharedModule } from './modules/shared/shared/shared.module';
import { HeaderComponent } from './components/headers/header/header.component';
import { AuthModule } from './modules/auth/auth/auth.module';
import { authInterceptor } from './interceptors/auth.interceptor';
import { ShoppingCartModule } from './modules/routes/shopping-cart/shopping-cart.module';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { RegisterComponent } from './modules/auth/components/register/register.component';

@NgModule({
  declarations: [
    AppComponent, 
    HeaderComponent
  ],
  imports: [
    AppRoutingModule, 
    BrowserModule, 
    BrowserAnimationsModule,
    MatSnackBarModule,
    SharedModule, 
    AuthModule,
    ShoppingCartModule,
  ],
  providers: [
    provideHttpClient(withInterceptors([authInterceptor]))
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
