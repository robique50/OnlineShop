import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Product } from '../../modules/shared/types/products.types';
import { ProductService } from '../../services/product.service';
import { AppRoutes } from '../../modules/shared/enums/routes.enum';
import { UntilDestroy, untilDestroyed } from '@ngneat/until-destroy';
import { AuthService } from '../../services/auth.service';

@UntilDestroy()
@Component({
  selector: 'app-products-list',
  standalone: false,
  templateUrl: './products-list.component.html',
  styleUrls: ['./products-list.component.scss'],
})
export class ProductsListComponent implements OnInit {
  protected products: Product[] = [];
  protected loading = false;
  protected error: string | null = null;

  constructor(
    private router: Router,
    private productService: ProductService,
    private authService: AuthService
  ) {}

  public ngOnInit(): void {
    console.log('Component initialized');
    this.loadProducts();
  }

  private loadProducts(): void {
    console.log('Starting to load products');
    this.loading = true;
    this.error = null;

    this.productService
      .getAllProducts()
      .pipe(untilDestroyed(this))
      .subscribe({
        next: (products) => {
          console.log('Products loaded:', products);
          this.products = products;
          this.loading = false;
          setTimeout(() => {
            console.log('Products after timeout:', this.products);
          }, 0);
        },
        error: (error) => {
          console.error('Error loading products:', error);
          this.error = 'Failed to load products. Please try again later.';
          this.loading = false;
        },
        complete: () => {
          console.log('Products loading completed');
          this.loading = false;
        },
      });
  }

  protected navigateToProductDetails(product: Product): void {
    console.log('Navigating to product:', product);
    if (product && product.id) {
      this.router.navigate([AppRoutes.Products, product.id]);
    } else {
      console.error('Invalid product or product ID:', product);
    }
  }

  protected navigateToCart(): void {
    this.router.navigate([AppRoutes.Cart]);
  }

  protected get isCustomer(): boolean {
    return this.authService.isCustomer();
  }

  protected get isAdmin(): boolean {
    return this.authService.isAdmin();
  }

  protected createProduct(): void {
    this.router.navigate(['/products/new']);
  }
}