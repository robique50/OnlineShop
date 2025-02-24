import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { IconButtonComponent } from '../components/icon-button/icon-button.component';

@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    IconButtonComponent
  ],
  exports: [IconButtonComponent]
})
export class SharedModule { }