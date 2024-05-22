import { Component, Injectable, Input, OnInit, inject } from '@angular/core';
import { NgbActiveModal, NgbModal, NgbModalConfig } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-confirm-dialog',
  templateUrl: './confirm-dialog.component.html',
  styleUrl: './confirm-dialog.component.css',
  providers: [NgbModalConfig, NgbModal]
})

@Injectable()
export class ConfirmDialogComponent implements OnInit {
  activeModal = inject(NgbActiveModal)
  @Input() modalStyle: any;
  @Input() modalTitle: any;
  @Input() modalBody: any;
  @Input() modalButtonColor: any;

  constructor() {}

  ngOnInit(): void {
  }
}
