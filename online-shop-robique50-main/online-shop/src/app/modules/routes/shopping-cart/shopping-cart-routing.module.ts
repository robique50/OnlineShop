import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ShoppingCartDetailsComponent } from '../../shopping-cart/shopping-cart/components/shopping-cart-details/shopping-cart-details/shopping-cart-details.component';

const routes: Routes = [
  { path: '', component: ShoppingCartDetailsComponent }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class ShoppingCartRoutingModule {}