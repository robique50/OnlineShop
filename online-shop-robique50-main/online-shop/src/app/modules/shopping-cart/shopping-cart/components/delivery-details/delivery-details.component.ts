import { Component, EventEmitter, Output } from '@angular/core';
import { DeliveryDetails } from '../../../../shared/types/order.types';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-delivery-details',
  standalone: false,

  templateUrl: './delivery-details.component.html',
  styleUrl: './delivery-details.component.scss',
})
export class DeliveryDetailsComponent {
  @Output() submit = new EventEmitter<DeliveryDetails>();
  @Output() cancel = new EventEmitter<void>();

  protected deliveryForm: FormGroup;

  constructor(private fb: FormBuilder) {
    this.deliveryForm = this.fb.group({
      country: ['', Validators.required],
      city: ['', Validators.required],
      streetAddress: ['', Validators.required],
    });
  }

  protected onSubmit(): void {
    if (this.deliveryForm.valid) {
      this.submit.emit(this.deliveryForm.value);
    }
  }

  protected onCancel(): void {
    this.cancel.emit();
  }
}
