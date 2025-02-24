import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [
  { path: '', redirectTo: '/products', pathMatch: 'full' },
  {
    path: 'products',
    loadChildren: () =>
      import('./modules/routes/products/products.module').then(
        (m) => m.ProductsModule
      ),
  },
  {
    path: 'cart',
    loadChildren: () =>
      import('./modules/routes/shopping-cart/shopping-cart.module').then(
        (m) => m.ShoppingCartModule
      ),
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}