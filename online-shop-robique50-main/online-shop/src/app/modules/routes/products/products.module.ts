import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProductDetailsComponent } from '../../../components/product-details/product-details.component';
import { ProductsListComponent } from '../../../components/products-list/products-list.component';
import { ProductsRoutingModule } from './products-routing.module';
import { SharedModule } from '../../shared/shared/shared.module';
import { ProductFormComponent } from '../../../components/product-form/product-form.component';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';

@NgModule({
  declarations: [
    ProductsListComponent,
    ProductDetailsComponent,
    ProductFormComponent
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    ProductsRoutingModule,
    MatSnackBarModule,
    SharedModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule
  ]
})
export class ProductsModule { }
