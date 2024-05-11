import { Component, Input, OnInit, inject } from '@angular/core';
import { AbstractControl, FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JoinService } from '../services/join.service';

@Component({
  selector: 'app-join-modal',
  templateUrl: './join-modal.component.html',
  styleUrl: './join-modal.component.css'
})
export class JoinModalComponent implements OnInit {
  activeModal = inject(NgbActiveModal)
  @Input() public postId!: number;
  @Input() public userId!: number;

  joinForm: FormGroup = new FormGroup({
    body: new FormControl('')
  })

  constructor(
    private formBuilder: FormBuilder,
    private joinService: JoinService
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
      next: data => {
        console.log(data)
      }, error: err => {
        console.log(err)
      }
    })
  }
}
