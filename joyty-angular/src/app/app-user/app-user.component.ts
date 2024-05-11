import { Component, OnInit, TemplateRef, ViewChild, inject } from '@angular/core';
import { UserService } from '../services/user.service';
import { PostModalComponent } from '../shared/post-modal/post-modal.component';
import { NgbActiveModal, NgbDate, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { FormControl, FormGroup } from '@angular/forms';
import { PostService } from '../services/post.service';
import { Post } from '../models/post.model';
import { CommentComponent } from '../comment/comment.component';
import { StorageService } from '../services/storage.service';

@Component({
  selector: 'app-app-user',
  templateUrl: './app-user.component.html',
  styleUrl: './app-user.component.css',
})
export class AppUserComponent implements OnInit {
  content?: string
  postData?: Post[]
  date = new Date()
  userData?: any

  private modalService = inject(NgbModal)

  public post = {
    body: "Test",
    meeting_country: "United States",
    meeting_state: "California",
    meeting_city: "San Francisco",
    meeting_location: "Golden Gate bridge",
    meeting_date: new NgbDate(2024, 3, 18),
    meeting_time: { hour: 17, minute: 30 },
    party_size: 3,
    cost_estimate: 300,
    cost_share: false,
    tags: [1, 2],
  }

  constructor(
    private userService: UserService,
    private postService: PostService,
    private storageService: StorageService
  ) {}

  ngOnInit(): void {
    this.getAllPost()
    this.userService.getUserContent().subscribe({
      next: data => {
        this.content = data
      }, error: err => {
        console.log(err)

        if (err.error) {
          this.content = JSON.parse(err.error).message
        } else {
          this.content = "Error with status: " + err.status
        }
      }
    })

    console.log("Data from storage service: " + JSON.stringify(this.storageService.getUser()))
    this.userData = this.storageService.getUser()
  }

  openPostModal() {
    const modalRef = this.modalService.open(PostModalComponent, { size: 'lg', centered: true, scrollable: true })
    modalRef.componentInstance.post = this.post
    modalRef.result.then((form) => {
      if (form) {
        //console.log(form.value)
        const ngbDate = form.controls['meeting_date'].value
        const ngbTime = form.controls['meeting_time'].value

        this.date.setFullYear(ngbDate.year, ngbDate.month, ngbDate.day)
        this.date.setHours(ngbTime.hour, ngbTime.minute, 0)

        const newPostForm: FormGroup = new FormGroup({
          body: new FormControl(form.controls['body'].value),
          party_size: new FormControl(form.controls['party_size'].value),
          meeting_location: new FormControl(form.controls['meeting_location'].value),
          meeting_city: new FormControl(form.controls['meeting_city'].value),
          meeting_state: new FormControl(form.controls['meeting_state'].value),
          meeting_country: new FormControl(form.controls['meeting_country'].value),
          meeting_datetime: new FormControl(this.date),
          cost_estimate: new FormControl(form.controls['cost_estimate'].value),
          cost_share: new FormControl(form.controls['cost_share'].value),
          tags: new FormControl(form.controls['tags'].value),
        })

        console.log(newPostForm.value)

        this.postService.createPost(newPostForm).subscribe({
          next: data => {
            console.log(data)
            //this.ngOnInit()
          }, error: err => {
            console.log(err)
          }
        })
      }
    })
  }

  getAllPost() {
    console.log("getAllPost")
    this.postService.getAllPost().subscribe({
      next: data => {
        console.log(data)
        this.postData = data
      }, error: err => {
        console.log(err)
      }
    })
  }

  openCommentModal(postId: number) {
    const modalRef = this.modalService.open(CommentComponent, { size: 'xl', centered: true, scrollable: true })
    modalRef.componentInstance.postId = postId
    modalRef.componentInstance.userId = this.userData.id
  }
}
