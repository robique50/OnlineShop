import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ProductsListComponent } from '../../../components/products-list/products-list.component';
import { ProductDetailsComponent } from '../../../components/product-details/product-details.component';
import { ProductFormComponent } from '../../../components/product-form/product-form.component';
import { AdminGuard } from '../../../guards/admin.guard';
import { AuthGuard } from '../../../guards/auth.guard';

const routes: Routes = [
  {
    path: '',
    component: ProductsListComponent,
    canActivate: [AuthGuard],
  },
  {
    path: 'new',
    component: ProductFormComponent,
    canActivate: [AuthGuard, AdminGuard],
  },
  {
    path: ':id',
    component: ProductDetailsComponent,
    canActivate: [AuthGuard],
  },
  {
    path: ':id/edit',
    component: ProductFormComponent,
    canActivate: [AuthGuard, AdminGuard],
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class ProductsRoutingModule {}
