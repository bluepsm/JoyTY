import { Component, OnInit, inject, ChangeDetectorRef } from '@angular/core';
import { PostModalComponent } from '../shared/post-modal/post-modal.component';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { FormControl, FormGroup } from '@angular/forms';
import { PostService } from '../services/post.service';
import { Post } from '../models/post.model';
import { CommentComponent } from '../comment/comment.component';
import { StorageService } from '../services/storage.service';
import { JoinModalComponent } from '../join-modal/join-modal.component';
import { JoinService } from '../services/join.service';
import { JoinRequestModalComponent } from '../join-request-modal/join-request-modal.component';
import { ToastService } from '../shared/toast/toast.service';

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
  joinRequest?: any
  joinRequestId: bigint[] = []
  private modalService = inject(NgbModal)

  constructor(
    private postService: PostService,
    private storageService: StorageService,
    private joinRequestService: JoinService,
    private toastService: ToastService,
    private cd: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.getAllPost()
    this.userData = this.storageService.getUser()
    this.getAllJoinRequest(this.userData.id)
  }

  openPostModal() {
    const modalRef = this.modalService.open(PostModalComponent, { size: 'lg', centered: true, scrollable: true })
    modalRef.result.then((form) => {
      if (form) {
        const ngbDate = form.controls['meetingDate'].value
        const ngbTime = form.controls['meetingTime'].value

        this.date.setFullYear(ngbDate.year, ngbDate.month, ngbDate.day)
        this.date.setHours(ngbTime.hour, ngbTime.minute, 0)

        const newPostForm: FormGroup = new FormGroup({
          body: new FormControl(form.controls['body'].value),
          partySize: new FormControl(form.controls['partySize'].value),
          placeName: new FormControl(form.controls['placeName'].value),
          placeAddress: new FormControl(form.controls['placeAddress'].value),
          placeLatitude: new FormControl(form.controls['placeLatitude'].value),
          placeLongtitude: new FormControl(form.controls['placeLongtitude'].value),
          meetingDatetime: new FormControl(this.date),
          costEstimate: new FormControl(form.controls['costEstimate'].value),
          costShare: new FormControl(form.controls['costShare'].value),
          tags: new FormControl(form.controls['tags'].value),
        })

        this.postService.createPost(newPostForm).subscribe({
          next: () => {
            this.toastService.showStatusToast("Post created successfully")
            this.getAllPost()
            this.cd.detectChanges()
          }, error: err => {
            this.toastService.showErrorToast("Error creating post: " + err.error.message)
            console.log(err)
          }
        })
      }
    })
  }

  getAllPost() {
    this.postService.getAllPost().subscribe({
      next: data => {
        this.postData = data
      }, error: err => {
        this.toastService.showErrorToast("Error fetching posts: " + err.error.message)
        console.log(err)
      }
    })
  }

  openCommentModal(postId: bigint) {
    const modalRef = this.modalService.open(CommentComponent, { size: 'xl', centered: true, scrollable: true })
    modalRef.componentInstance.postId = postId
    modalRef.componentInstance.userId = this.userData.id
  }

  openJoinModal(postId: bigint) {
    const modalRef = this.modalService.open(JoinModalComponent, { size: 'sm', centered: true, scrollable: true })
    modalRef.componentInstance.postId = postId
    modalRef.componentInstance.userId = this.userData.id
    modalRef.result.then((form) => {
      if (form) {
        this.joinRequestService.createJoinRequest(postId, form.controls['body'].value).subscribe({
          next: () => {
            this.toastService.showStatusToast("Join request sent successfully")
            this.getAllJoinRequest(this.userData.id)
            this.cd.detectChanges()
          }, error: err => {
            this.toastService.showErrorToast("Error sending join request: " + err.error.message)
            console.log(err)
          }
        })
      }
    })
  }

  getAllJoinRequest(userId: bigint) {
    this.joinRequestService.getAllRequestByUserId(userId).subscribe({
      next: data => {
        this.joinRequest = data
        for (let joinRequest of data) {
          this.joinRequestId.push(joinRequest.join.id)
        }
      }, error: err => {
        this.toastService.showErrorToast("Error fetching join requests: " + err.error.message)
        console.log(err)
      }
    })
  }

  checkJoinRequestHasBeenSent(postId: bigint): boolean {
    if (this.joinRequestId.includes(postId)) {
      return true
    } else {
      return false
    }
  }

  openJoinRequestModal(postId: bigint) {
    const modalRef = this.modalService.open(JoinRequestModalComponent, { size: 'lg', centered: true, scrollable: true })
    modalRef.componentInstance.postId = postId
  }

  shareCalculate(cost: bigint, numberOfPeople: number): number {
    if (cost === BigInt(0)) {
      return 0
    } else {
      numberOfPeople += 1;

      let costPerPersonInt: number = Math.ceil(Number(cost) / numberOfPeople);

      return costPerPersonInt
    }
  }
}
