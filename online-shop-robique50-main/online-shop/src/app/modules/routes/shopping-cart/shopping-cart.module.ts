import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ShoppingCartDetailsComponent } from '../../shopping-cart/shopping-cart/components/shopping-cart-details/shopping-cart-details/shopping-cart-details.component';
import { ShoppingCartRoutingModule } from './shopping-cart-routing.module';
import { SharedModule } from '../../shared/shared/shared.module';

@NgModule({
  declarations: [ShoppingCartDetailsComponent],
  imports: [
    CommonModule,
    ShoppingCartRoutingModule,
    SharedModule
  ]
})
export class ShoppingCartModule { }
