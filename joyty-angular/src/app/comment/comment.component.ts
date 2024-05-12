import { Component, Input, OnInit, inject } from '@angular/core';
import { NgbActiveModal, NgbModalConfig } from '@ng-bootstrap/ng-bootstrap';
import { CommentService } from '../services/comment.service';
import { AbstractControl, FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { Comment } from '../models/comment.model';

@Component({
  selector: 'app-comment',
  templateUrl: './comment.component.html',
  styleUrl: './comment.component.css'
})
export class CommentComponent implements OnInit {
  @Input() public postId!: number
  @Input() public userId!: number
  activeModal = inject(NgbActiveModal)

  comments?: Comment[]

  commentForm: FormGroup = new FormGroup({
    body: new FormControl('')
  })

  constructor(
    private config: NgbModalConfig,
    private commentService: CommentService,
    private formBuilder: FormBuilder
  ) {
    this.config.backdrop = 'static'
    this.config.keyboard = false
  }

  ngOnInit(): void {
    //console.log(this.postId)

    this.getAllComment(this.postId)

    //console.log(this.userId)
    //console.log(this.comments)

    this.commentForm = this.formBuilder.group({
      body: ['', [Validators.required]]
    })
  }

  getAllComment(postId: number) {
    this.commentService.getAllComments(postId).subscribe({
      next: data => {
        this.comments = data
        //console.log(data)
      }, error: err => {
        console.log(err)
      }
    })
  }

  get cf(): { [key: string]: AbstractControl } {
    return this.commentForm.controls
  }

  commentFormSubmit() {
    this.commentService.createComment(this.postId, this.cf['body'].value).subscribe({
      next: data => {
        console.log(data)
      }, error: err => {
        console.log(err)
      }
    })
  }
}
