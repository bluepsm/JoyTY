import { ChangeDetectorRef, Component, Input, OnInit, inject } from '@angular/core';
import { NgbActiveModal, NgbModalConfig } from '@ng-bootstrap/ng-bootstrap';
import { CommentService } from '../services/comment.service';
import { AbstractControl, FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { Comment } from '../models/comment.model';
import { ToastService } from '../shared/toast/toast.service';

@Component({
  selector: 'app-comment',
  templateUrl: './comment.component.html',
  styleUrl: './comment.component.css'
})
export class CommentComponent implements OnInit {
  @Input() public postId!: bigint
  @Input() public userId!: bigint
  activeModal = inject(NgbActiveModal)

  comments?: Comment[]

  commentForm: FormGroup = new FormGroup({
    body: new FormControl('')
  })

  constructor(
    private config: NgbModalConfig,
    private commentService: CommentService,
    private formBuilder: FormBuilder,
    private cd: ChangeDetectorRef,
    private toastService: ToastService,
  ) {
    this.config.backdrop = 'static'
    this.config.keyboard = false
  }

  ngOnInit(): void {
    this.getAllComment(this.postId)
    this.commentForm = this.formBuilder.group({
      body: ['', [Validators.required]]
    })
  }

  getAllComment(postId: bigint) {
    this.commentService.getAllComments(postId).subscribe({
      next: data => {
        this.comments = data
      }, error: err => {
        this.toastService.showErrorToast("Error fetching comments: " + err.error.message)
        console.log(err)
      }
    })
  }

  get cf(): { [key: string]: AbstractControl } {
    return this.commentForm.controls
  }

  commentFormSubmit() {
    this.commentService.createComment(this.postId, this.cf['body'].value).subscribe({
      next: () => {
        this.toastService.showStatusToast("Comment created successfully")
        this.getAllComment(this.postId)
        this.cd.detectChanges()
      }, error: err => {
        this.toastService.showErrorToast("Error creating comment: " + err.error.message)
        console.log(err)
      }
    })
  }
}
