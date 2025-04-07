import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, switchMap, take } from 'rxjs';
import { environment } from '../../../../../environments/environment';
import { CartItem } from '../../../shared/types/products.types';
import { DeliveryDetails, OrderProduct, OrderRequest } from '../../../shared/types/order.types';
import { AppRoutes } from '../../../shared/enums/routes.enum';
import { AuthService } from '../../../../services/auth.service';

@Injectable({
  providedIn: 'root',
})
export class ShoppingCartService {
  private apiUrl = environment.apiUrl;
  private isProcessingOrder = false;

  constructor(private http: HttpClient, private authService: AuthService) {}

  public checkout(
    cartItems: CartItem[],
    deliveryDetails: DeliveryDetails
  ): Observable<unknown> {
    if (this.isProcessingOrder) {
      return new Observable((subscriber) => subscriber.complete());
    }

    this.isProcessingOrder = true;

    return this.authService.loadUserProfile().pipe(
      take(1),
      switchMap((user) => {
        const orderProducts: OrderProduct[] = cartItems.map((item) => ({
          productId: item.product.id,
          productName: item.product.name,
          quantity: item.quantity,
        }));

        const orderRequest: OrderRequest = {
          customerId: user.id,
          orderTimestamp: new Date().toISOString(),
          deliveryCountry: deliveryDetails.country,
          deliveryCity: deliveryDetails.city,
          deliveryStreetAddress: deliveryDetails.streetAddress,
          products: orderProducts,
        };

        return this.http.post<unknown>(
          `${this.apiUrl}/${AppRoutes.Order}`,
          orderRequest
        );
      })
    );
  }

  public resetProcessingState(): void {
    this.isProcessingOrder = false;
  }
}
