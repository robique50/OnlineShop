import { Injectable } from '@angular/core';
import {
  HttpClient,
  HttpErrorResponse,
  HttpHeaders,
} from '@angular/common/http';
import { catchError, Observable, tap, throwError } from 'rxjs';
import { environment } from '../../environments/environment';
import { Product } from '../modules/shared/types/products.types';
import { v4 as uuidv4 } from 'uuid';

@Injectable({
  providedIn: 'root',
})
export class ProductService {
  private apiUrl = `${environment.apiUrl}/products`;

  constructor(private http: HttpClient) {}

  private getHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');
    return new HttpHeaders().set('Authorization', `Bearer ${token}`);
  }

  public getAllProducts(): Observable<Product[]> {
    return this.http
      .get<Product[]>(this.apiUrl, { headers: this.getHeaders() })
      .pipe(
        tap((products) => console.log('Products received:', products)),
        catchError(this.handleError)
      );
  }

  public getProduct(id: string): Observable<Product> {
    return this.http
      .get<Product>(`${this.apiUrl}/${id}`, { headers: this.getHeaders() })
      .pipe(
        tap((product) => console.log('Product received:', product)),
        catchError(this.handleError)
      );
  }

  public createProduct(product: Product): Observable<Product> {
  const newProduct = {
    ...product,
      id: uuidv4(),
  };

  return this.http
    .post<Product>(this.apiUrl, newProduct, { 
      headers: this.getHeaders() 
    })
    .pipe(
      tap(response => console.log('Create response:', response)),
      catchError(this.handleError)
    );
}

  public updateProduct(id: string, product: Product): Observable<Product> {
    const updateData = {
      ...product,
      id: id,
    };

    return this.http
      .put<Product>(`${this.apiUrl}/${id}`, updateData, {
        headers: this.getHeaders(),
      })
      .pipe(
        tap((response) => console.log('Update response:', response)),
        catchError(this.handleError)
      );
  }

  public deleteProduct(id: string): Observable<unknown> {
    return this.http
      .delete<unknown>(`${this.apiUrl}/${id}`, { headers: this.getHeaders() })
      .pipe(catchError(this.handleError));
  }
  private handleError(error: HttpErrorResponse) {
    let errorMessage = 'An error occurred';
    if (error.status === 403) {
      errorMessage = 'You do not have permission to perform this action';
    }
    console.error('API Error:', error);
    return throwError(() => errorMessage);
  }
}