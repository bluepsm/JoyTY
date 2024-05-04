import { Component, EventEmitter, Injectable, Input, OnInit, Output, TemplateRef, ViewChild } from '@angular/core';
import { NgbModal, NgbModalConfig, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-confirm-dialog',
  templateUrl: './confirm-dialog.component.html',
  styleUrl: './confirm-dialog.component.css',
  providers: [NgbModalConfig, NgbModal]
})

@Injectable()
export class ConfirmDialogComponent implements OnInit {
  @ViewChild('confirmationModal') private modalContent!: TemplateRef<ConfirmDialogComponent>
  @Output() newConfirmationEvent = new EventEmitter<string>();
  @Input() modalStyle: any;
  @Input() modalTitle: any;
  @Input() modalBody: any;
  @Input() modalButtonColor: any;

  private modalRef!: NgbModalRef;

  constructor(config: NgbModalConfig, private modalService: NgbModal) {
    config.backdrop = 'static';
    config.keyboard = false;
  }

  ngOnInit(): void {
  }

  open(): Promise<boolean> {
    return new Promise<boolean>(resolve => {
      this.modalRef = this.modalService.open(this.modalContent, { centered: true, size: 'md' })
      this.modalRef.result.then((accept) => {
        this.newConfirmationEvent.emit(accept);
      }, (reject) => {
        this.newConfirmationEvent.emit(reject);
      });
    })
  }
}
