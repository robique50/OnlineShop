import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CartItem } from '../../../../../shared/types/products.types';
import { ShoppingCartService } from '../../../services/shopping-cart.service';
import { AppRoutes } from '../../../../../shared/enums/routes.enum';
import { UntilDestroy, untilDestroyed } from '@ngneat/until-destroy';

@UntilDestroy()
@Component({
  selector: 'app-shopping-cart-details',
  standalone: false,
  templateUrl: './shopping-cart-details.component.html',
  styleUrls: ['./shopping-cart-details.component.scss'],
})
export class ShoppingCartDetailsComponent implements OnInit {
  protected cartItems: CartItem[] = [];
  protected cartTotalPrice: number = 0;

  constructor(
    private router: Router,
    private shoppingCartService: ShoppingCartService
  ) {}

  ngOnInit(): void {
    this.loadCartItems();
  }

  private loadCartItems(): void {
    const items = localStorage.getItem('cartItems');
    if (items) {
      this.cartItems = JSON.parse(items);
      this.updateCartTotal();
    }
  }

  protected updateQuantity(cartItem: CartItem, newQuantity: number): void {
    if (newQuantity < 1) {
      this.removeFromCart(cartItem);
      return;
    }

    const item = this.cartItems.find(
      (item) => item.product.id === cartItem.product.id
    );
    if (item) {
      item.quantity = newQuantity;
      item.totalPrice = item.quantity * item.product.price;
      this.updateCartTotal();
      this.saveCartItems();
    }
  }

  protected removeFromCart(cartItem: CartItem): void {
    this.cartItems = this.cartItems.filter(
      (item) => item.product.id !== cartItem.product.id
    );
    this.updateCartTotal();
    this.saveCartItems();
  }

  protected updateCartTotal(): void {
    this.cartTotalPrice = this.cartItems.reduce(
      (total, item) => total + item.totalPrice,
      0
    );
  }

  private saveCartItems(): void {
    localStorage.setItem('cartItems', JSON.stringify(this.cartItems));
  }

  protected checkout(): void {
    if (!this.cartItems.length) {
      return;
    }

    this.shoppingCartService
      .checkout(this.cartItems)
      .pipe(untilDestroyed(this))
      .subscribe({
        next: () => {
          this.router.navigate([AppRoutes.Products]).then(() => {
            localStorage.removeItem('cartItems');
            this.cartItems = [];
            this.updateCartTotal();
          });
        },
        error: (error) => {
          console.error('Error creating order:', error);
        },
      });
  }
}
