import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ShoppingCartDetailsComponent } from '../../shopping-cart/shopping-cart/components/shopping-cart-details/shopping-cart-details/shopping-cart-details.component';
import { ShoppingCartRoutingModule } from './shopping-cart-routing.module';
import { SharedModule } from '../../shared/shared/shared.module';
import { DeliveryDetailsComponent } from '../../shopping-cart/shopping-cart/components/delivery-details/delivery-details.component';
import { ReactiveFormsModule } from '@angular/forms';

@NgModule({
  declarations: [
    DeliveryDetailsComponent, 
    ShoppingCartDetailsComponent
  ],
  imports: [
    CommonModule, 
    ReactiveFormsModule,
    SharedModule,
    ShoppingCartRoutingModule
  ],
  exports: [
    DeliveryDetailsComponent,
    ShoppingCartDetailsComponent
  ]
})
export class ShoppingCartModule {}