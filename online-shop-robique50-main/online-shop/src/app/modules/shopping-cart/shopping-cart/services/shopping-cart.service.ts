import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../../environments/environment';
import { CartItem } from '../../../shared/types/products.types';
import { OrderProduct, OrderRequest } from '../../../shared/types/order.types';
import { AppRoutes } from '../../../shared/enums/routes.enum';

@Injectable({
  providedIn: 'root',
})
export class ShoppingCartService {
  private apiUrl = environment.apiUrl;
  private readonly HARDCODED_USER_ID = '550e8400-e29b-41d4-a716-446655440001';
  private readonly HARDCODED_LOCATION = {
    country: 'Romania',
    city: 'Bucharest',
    streetAddress: 'Example Street 123',
  };

  constructor(private http: HttpClient) {}

  public checkout(cartItems: CartItem[]): Observable<unknown> {
    const orderProducts: OrderProduct[] = cartItems.map((item) => ({
      productId: item.product.id,
      productName: item.product.name,
      quantity: item.quantity,
    }));

    const orderRequest: OrderRequest = {
      customerId: this.HARDCODED_USER_ID,
      orderTimestamp: new Date().toISOString(),
      deliveryCountry: this.HARDCODED_LOCATION.country,
      deliveryCity: this.HARDCODED_LOCATION.city,
      deliveryStreetAddress: this.HARDCODED_LOCATION.streetAddress,
      products: orderProducts,
    };
    return this.http.post<unknown>(
      `${this.apiUrl}/${AppRoutes.Order}`,
      orderRequest
    );
  }
}