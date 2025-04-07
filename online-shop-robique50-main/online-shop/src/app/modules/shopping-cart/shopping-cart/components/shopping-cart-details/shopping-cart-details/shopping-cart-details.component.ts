import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CartItem } from '../../../../../shared/types/products.types';
import { ShoppingCartService } from '../../../services/shopping-cart.service';
import { AppRoutes } from '../../../../../shared/enums/routes.enum';
import { UntilDestroy, untilDestroyed } from '@ngneat/until-destroy';
import { DeliveryDetails } from '../../../../../shared/types/order.types';
import { finalize } from 'rxjs';

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
  protected showDeliveryForm = false;
  protected isProcessingCheckout = false;

  constructor(
    private router: Router,
    private shoppingCartService: ShoppingCartService
  ) {}

  public ngOnInit(): void {
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
    if (!this.cartItems.length || this.isProcessingCheckout) {
      return;
    }
    this.showDeliveryForm = true;
  }

  protected onDeliverySubmit(deliveryDetails: DeliveryDetails): void {
    if (this.isProcessingCheckout) {
      return;
    }

    this.isProcessingCheckout = true;

    this.shoppingCartService
      .checkout(this.cartItems, deliveryDetails)
      .pipe(
        untilDestroyed(this),
        finalize(() => {
          this.isProcessingCheckout = false;
          this.shoppingCartService.resetProcessingState();
        })
      )
      .subscribe({
        next: () => {
          localStorage.removeItem('cartItems');
          this.cartItems = [];
          this.updateCartTotal();
          this.showDeliveryForm = false;
          this.router.navigate([AppRoutes.Products]);
        },
        error: (error) => {
          console.error('Error creating order:', error);
        },
      });
  }

  protected onDeliveryCancel(): void {
    this.showDeliveryForm = false;
  }
}