import { Component, OnInit } from '@angular/core';
import { Product } from '../../modules/shared/types/products.types';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { ProductService } from '../../services/product.service';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-product-form',
  standalone: false,
  templateUrl: './product-form.component.html',
  styleUrl: './product-form.component.scss',
})
export class ProductFormComponent implements OnInit {
  productForm: FormGroup;
  isEditMode = false;
  productId: string | null = null;
  originalProduct: Product | null = null;
  errorMessage: string | null = null;
  categories: string[] = [];
  previewImageUrl: string | null = null;

  constructor(
    private fb: FormBuilder,
    private productService: ProductService,
    private route: ActivatedRoute,
    private router: Router,
    private snackBar: MatSnackBar
  ) {
    this.productForm = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(3)]],
      description: ['', Validators.required],
      price: ['', [Validators.required, Validators.min(0.01)]],
      weight: ['', [Validators.required, Validators.min(0.01)]],
      categoryName: ['', Validators.required],
      imageUrl: ['', [Validators.required]],
    });

    this.productForm.get('imageUrl')?.valueChanges.subscribe((url) => {
      if (url && this.isValidUrl(url)) {
        this.previewImageUrl = url;
      } else {
        this.previewImageUrl = null;
      }
    });
  }

  ngOnInit(): void {
    this.loadCategories();
    this.productId = this.route.snapshot.paramMap.get('id');
    if (this.productId) {
      this.isEditMode = true;
      this.loadProduct(this.productId);
    }
  }

  private loadCategories(): void {
    this.productService.getAllProducts().subscribe({
      next: (products) => {
        this.categories = [
          ...new Set(products.map((product) => product.categoryName)),
        ].sort();
      },
      error: (_error) => {
        this.snackBar.open('Failed to load categories', 'Close', {
          duration: 3000,
          panelClass: ['error-snackbar'],
        });
      },
    });
  }

  private isValidUrl(url: string): boolean {
    try {
      new URL(url);
      return true;
    } catch {
      return false;
    }
  }

  protected onImageError(): void {
    this.previewImageUrl = null;
    this.snackBar.open('Invalid image URL', 'Close', {
      duration: 3000,
      panelClass: ['error-snackbar'],
    });
  }

  protected formatNumber(control: string, event: any): void {
    const value = event.target.value;
    if (value) {
      this.productForm.patchValue(
        {
          [control]: parseFloat(value).toFixed(2),
        },
        { emitEvent: false }
      );
    }
  }

  private loadProduct(id: string): void {
    this.productService.getProduct(id).subscribe({
      next: (product) => {
        this.originalProduct = product;
        this.productForm.patchValue({
          name: product.name,
          description: product.description,
          price: product.price,
          weight: product.weight,
          categoryName: product.categoryName,
          imageUrl: product.imageUrl,
        });
      },
      error: (error) => {
        this.errorMessage = 'Failed to load product';
        console.error('Error loading product:', error);
      },
    });
  }

  protected onSave(): void {
    if (this.productForm.valid) {
      const productData = this.productForm.value;

      if (this.isEditMode && this.productId && this.originalProduct) {
        const updatedProduct: Product = {
          ...this.originalProduct,
          ...productData,
          id: this.productId,
        };

        this.productService
          .updateProduct(this.productId, updatedProduct)
          .subscribe({
            next: () => {
              this.router.navigate(['/products']);
            },
            error: (error) => {
              this.errorMessage = 'Failed to update product';
              console.error('Error updating product:', error);
            },
          });
      } else {
        this.productService.createProduct(productData).subscribe({
          next: () => {
            this.router.navigate(['/products']);
          },
          error: (error) => {
            this.errorMessage = 'Failed to create product';
            console.error('Error creating product:', error);
          },
        });
      }
    }
  }

  protected onCancel(): void {
    if (this.productForm.dirty) {
      if (confirm('Are you sure you want to discard changes?')) {
        if (this.isEditMode && this.productId) {
          this.router.navigate(['/products', this.productId]);
        } else {
          this.router.navigate(['/products']);
        }
      }
    } else {
      if (this.isEditMode && this.productId) {
        this.router.navigate(['/products', this.productId]);
      } else {
        this.router.navigate(['/products']);
      }
    }
  }
}
