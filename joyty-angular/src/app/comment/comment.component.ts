import { ChangeDetectorRef, Component, ElementRef, Input, OnInit, ViewChild, inject } from '@angular/core';
import { NgbActiveModal, NgbModal, NgbModalConfig } from '@ng-bootstrap/ng-bootstrap';
import { CommentService } from '../services/comment.service';
import { AbstractControl, FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { Comment } from '../models/comment.model';
import { ToastService } from '../shared/toast/toast.service';
import { ConfirmDialogComponent } from '../shared/confirm-dialog/confirm-dialog.component';

@Component({
  selector: 'app-comment',
  templateUrl: './comment.component.html',
  styleUrl: './comment.component.css'
})
export class CommentComponent implements OnInit {
  @Input() public postId!: bigint
  @Input() public userId!: bigint
  activeModal = inject(NgbActiveModal)
  modalService = inject(NgbModal)

  comments?: Comment[]

  edit: boolean = false
  //@ViewChild('commentField') private commentField = ElementRef
  editingCommentId?: bigint

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
        this.toastService.showErrorToast("Error fetching comments: " + err.message)
        console.log(err)
      }
    })
  }

  get cf(): { [key: string]: AbstractControl } {
    return this.commentForm.controls
  }

  commentFormSubmit() {
    console.log(this.cf['body'].value)
    this.commentService.createComment(this.postId, this.cf['body'].value).subscribe({
      next: () => {
        this.toastService.showStatusToast("Comment created successfully")
        this.getAllComment(this.postId)
        this.cd.detectChanges()
      }, error: err => {
        this.toastService.showErrorToast("Error creating comment: " + err.message)
        console.log(err)
      }
    })
  }

  openDeleteConfirmModal(commentId: bigint) {
    const modalRef = this.modalService.open(ConfirmDialogComponent, { size: 'sm', centered: true })
    modalRef.componentInstance.modalStyle = "modal-style-danger"
    modalRef.componentInstance.modalTitle = "Delete Confirmation"
    modalRef.componentInstance.modalBody = "Are you sure you want to delete this comment?"
    modalRef.componentInstance.modalButtonColor = "btn-danger"

    modalRef.result.then((confirm) => {
      if (confirm) {
        this.deleteComment(commentId)
      }
    })
  }

  deleteComment(commentId: bigint) {
    this.commentService.deleteCommentById(commentId).subscribe({
      next: () => {
        this.toastService.showStatusToast("Delete comment successfully.")
        this.getAllComment(this.postId)
        this.cd.detectChanges()
      }, error: err => {
        this.toastService.showErrorToast("Error deleting comment: " + err.message)
        console.log(err)
      }
    })
  }

  editingComment(commentId: bigint, commentBody: string) {
    this.edit = true
    this.editingCommentId = commentId
    this.cf['body'].setValue(commentBody)
  }

  cancleEditingComment() {
    this.edit = false
    this.editingCommentId = undefined
    this.cf['body'].setValue("")
  }

  updateComment() {
    if (this.editingCommentId) {
      this.commentService.updateComment(this.editingCommentId, this.cf['body'].value, this.postId).subscribe({
        next: () => {
          this.toastService.showStatusToast("Comment Edited.")
          this.cancleEditingComment()
          this.getAllComment(this.postId)
          this.cd.detectChanges()
        }, error: err => {
          this.toastService.showErrorToast("Error editing comment: " + err.message)
          console.log(err)
        }
      })
    }
  }
}
