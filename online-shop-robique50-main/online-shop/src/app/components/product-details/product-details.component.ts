import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Product } from '../../modules/shared/types/products.types';
import { ProductService } from '../../services/product.service';
import { AppRoutes } from '../../modules/shared/enums/routes.enum';
import { UntilDestroy, untilDestroyed } from '@ngneat/until-destroy';
import { AuthService } from '../../services/auth.service';

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

  constructor(
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private productService: ProductService,
    private authService: AuthService
  ) {}

  public ngOnInit(): void {
    const productId = this.activatedRoute.snapshot.paramMap.get('id');
    if (productId) {
      this.loadProduct(productId);
    }
  }

  private loadProduct(id: string): void {
    this.productService
      .getProduct(id)
      .pipe(untilDestroyed(this))
      .subscribe({
        next: (product) => {
          this.product = product;
        },
        error: (error) => {
          console.error('Error loading product:', error);
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
          this.router.navigate([AppRoutes.Products]);
        },
        error: (error) => {
          console.error('Error deleting product:', error);
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

      this.router.navigate([AppRoutes.Cart]).then(() => {
        localStorage.setItem('cartItems', JSON.stringify(cartItems));
      });
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