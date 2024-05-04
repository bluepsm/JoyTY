import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ConfirmDialogComponent } from './confirm-dialog/confirm-dialog.component';
import { ToastComponent } from './toast/toast.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { NgTemplateOutlet } from '@angular/common';
import { NgbToastModule } from '@ng-bootstrap/ng-bootstrap';

@NgModule({
  declarations: [ConfirmDialogComponent, ToastComponent],
  imports: [
    CommonModule,
    NgbModule,
    NgbToastModule,
    NgTemplateOutlet
  ],
  exports: [ConfirmDialogComponent, ToastComponent]
})
export class SharedModule { }
