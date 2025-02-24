import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { Product } from '../../modules/shared/types/products.types';
import { ProductService } from '../../services/product.service';
import { AppRoutes } from '../../modules/shared/enums/routes.enum';
import { UntilDestroy, untilDestroyed } from '@ngneat/until-destroy';

@UntilDestroy()
@Component({
  selector: 'app-products-list',
  standalone: false,
  templateUrl: './products-list.component.html',
  styleUrls: ['./products-list.component.scss'],
})
export class ProductsListComponent {
  protected products: Product[] = [];

  constructor(private router: Router, private productService: ProductService) {
    this.router = router;
    this.productService = productService;
  }

  ngOnInit(): void {
    this.loadProducts();
  }

  private loadProducts(): void {
    this.productService
      .getAllProducts()
      .pipe(untilDestroyed(this))
      .subscribe({
        next: (products) => {
          this.products = products;
        },
        error: (error) => {
          console.error('Error loading products:', error);
        },
      });
  }

  protected navigateToProductDetails(product: Product): void {
    if (product?.id) {
      this.router.navigate([AppRoutes.Products, product.id]);
    } else {
      console.error('Invalid product or product ID:', product);
    }
  }

  protected navigateToCart(): void {
    this.router.navigate([AppRoutes.Cart]);
  }
}