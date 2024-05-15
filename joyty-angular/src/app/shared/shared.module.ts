import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ConfirmDialogComponent } from './confirm-dialog/confirm-dialog.component';
import { ToastComponent } from './toast/toast.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { NgTemplateOutlet } from '@angular/common';
import { NgbToastModule } from '@ng-bootstrap/ng-bootstrap';
import { PostModalComponent } from './post-modal/post-modal.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NgbDatepickerModule, NgbDatepickerConfig, NgbTimepickerModule, NgbTimepickerConfig } from '@ng-bootstrap/ng-bootstrap';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { TagModalComponent } from './tag-modal/tag-modal.component';
import { GoogleMapsModule } from '@angular/google-maps';
import { MapComponent } from '../map/map.component';
import { PlaceAutoCompleteComponent } from './place-auto-complete/place-auto-complete.component';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatToolbarModule } from '@angular/material/toolbar';

@NgModule({
  declarations: [ConfirmDialogComponent, ToastComponent, PostModalComponent, TagModalComponent, MapComponent, PlaceAutoCompleteComponent],
  imports: [
    CommonModule,
    NgbModule,
    NgbToastModule,
    NgTemplateOutlet,
    FormsModule,
    ReactiveFormsModule,
    BrowserAnimationsModule,
    NgbDatepickerModule,
    NgbTimepickerModule,
    GoogleMapsModule,
    MatInputModule,
    MatInputModule,
    MatToolbarModule,
  ],
  exports: [ConfirmDialogComponent, ToastComponent, PostModalComponent, TagModalComponent, MapComponent, PlaceAutoCompleteComponent],
})
export class SharedModule { }
