import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProductDetailsComponent } from '../../../components/product-details/product-details.component';
import { ProductsListComponent } from '../../../components/products-list/products-list.component';
import { ProductsRoutingModule } from './products-routing.module';
import { SharedModule } from '../../shared/shared/shared.module';


@NgModule({
  declarations: [
    ProductsListComponent,
    ProductDetailsComponent
  ],
  imports: [
    CommonModule,
    ProductsRoutingModule,
    SharedModule
  ]
})
export class ProductsModule { }
