import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Product } from '../../modules/shared/types/products.types';
import { ProductService } from '../../services/product.service';
import { AppRoutes } from '../../modules/shared/enums/routes.enum';
import { UntilDestroy, untilDestroyed } from '@ngneat/until-destroy';
import { AuthService } from '../../services/auth.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { HttpErrorResponse } from '@angular/common/http';

@UntilDestroy()
@Component({
  selector: 'app-product-details',
  standalone: false,
  templateUrl: './product-details.component.html',
  styleUrls: ['./product-details.component.scss'],
})
export class ProductDetailsComponent implements OnInit {
  protected product: Product | null = null;
  protected quantity: number = 1;
  protected isLoading: boolean = true;
  protected errorLoading: string | null = null;

  constructor(
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private productService: ProductService,
    private authService: AuthService,
    private snackBar: MatSnackBar
  ) {}

  public ngOnInit(): void {
    const productId = this.activatedRoute.snapshot.paramMap.get('id');
    if (productId) {
      this.loadProduct(productId);
    } else {
      console.error('Product ID missing from route.');
      this.isLoading = false;
      this.snackBar.open('Invalid route: Product ID is missing.', 'Close', {
        duration: 4000,
        panelClass: ['error-snackbar'],
      });
      this.router.navigate([AppRoutes.Products]);
    }
  }

  private loadProduct(id: string): void {
    this.isLoading = true;
    this.errorLoading = null;
    this.product = null;

    this.productService
      .getProduct(id)
      .pipe(untilDestroyed(this))
      .subscribe({
        next: (product) => {
          this.product = product;
          this.isLoading = false;
        },
        error: (error: HttpErrorResponse) => {
          console.error('Error loading product:', error);
          this.isLoading = false;
          if (error.status === 404) {
            this.snackBar.open('Product not found.', 'Close', {
              duration: 4000,
              panelClass: ['warning-snackbar'],
            });
            this.router.navigate(['/products']);
          } else {
            this.errorLoading =
              'Failed to load product details. Please try again later.';
            this.snackBar.open(this.errorLoading, 'Close', {
              duration: 5000,
              panelClass: ['error-snackbar'],
            });
            this.router.navigate(['/products']);
          }
        },
      });
  }

  protected updateQuantity(change: number): void {
    const newQuantity = this.quantity + change;
    if (newQuantity >= 1) {
      this.quantity = newQuantity;
    }
  }

  protected deleteProduct(): void {
    if (this.product?.id) {
      this.productService.deleteProduct(this.product.id.toString()).subscribe({
        next: () => {
          this.snackBar.open('Product deleted successfully.', 'Close', {
            duration: 3000,
            panelClass: ['success-snackbar'],
          });
          this.router.navigate(['/products']);
        },
        error: (error) => {
          console.error('Error deleting product:', error);
          this.snackBar.open('Failed to delete product.', 'Close', {
            duration: 4000,
            panelClass: ['error-snackbar'],
          });
        },
      });
    }
  }

  protected addToCart(): void {
    if (this.product) {
      const cartItems = JSON.parse(localStorage.getItem('cartItems') || '[]');
      const existingItem = cartItems.find(
        (item: any) => item.product.id === this.product?.id
      );

      if (existingItem) {
        existingItem.quantity += this.quantity;
        existingItem.totalPrice = existingItem.quantity * this.product.price;
      } else {
        cartItems.push({
          product: this.product,
          quantity: this.quantity,
          totalPrice: this.quantity * this.product.price,
        });
      }
      localStorage.setItem('cartItems', JSON.stringify(cartItems));
      this.snackBar.open(`${this.product.name} added to cart.`, 'Close', {
        duration: 2000,
        panelClass: ['success-snackbar'],
      });
      this.router.navigate([AppRoutes.Cart]);
    }
  }

  public get isAdmin(): boolean {
    return this.authService.isAdmin();
  }

  public get isCustomer(): boolean {
    return this.authService.isCustomer();
  }

  protected editProduct(): void {
    if (this.product?.id) {
      this.router.navigate(['/products', this.product.id, 'edit']);
    }
  }
}
