import { Component, Input, OnInit, inject } from '@angular/core';
import { AbstractControl, FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JoinService } from '../services/join.service';
import { ToastService } from '../shared/toast/toast.service';

@Component({
  selector: 'app-join-modal',
  templateUrl: './join-modal.component.html',
  styleUrl: './join-modal.component.css'
})
export class JoinModalComponent implements OnInit {
  activeModal = inject(NgbActiveModal)
  @Input() public postId!: bigint;
  @Input() public userId!: bigint;

  joinForm: FormGroup = new FormGroup({
    body: new FormControl('')
  })

  constructor(
    private formBuilder: FormBuilder,
    private joinService: JoinService,
    private toastService: ToastService
  ) {}

  ngOnInit(): void {
    this.joinForm = this.formBuilder.group({
      body: ['', [Validators.required]]
    })
  }

  get jf(): { [key: string]: AbstractControl } {
    return this.joinForm.controls
  }

  joinFormSubmit() {
    this.joinService.createJoinRequest(this.postId, this.jf['body'].value).subscribe({
      next: () => {
        this.toastService.showStatusToast('Join request sent!')
      }, error: err => {
        this.toastService.showErrorToast('Error sending join request: ' + err.error.message)
        console.log(err)
      }
    })
  }
}
