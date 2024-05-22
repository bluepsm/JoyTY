import { Component, OnInit, inject, ChangeDetectorRef } from '@angular/core';
import { PostModalComponent } from '../modal/post-modal/post-modal.component';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { FormControl, FormGroup } from '@angular/forms';
import { PostService } from '../_services/post.service';
import { Post } from '../models/post.model';
import { CommentModalComponent } from '../modal/comment-modal/comment-modal.component';
import { StorageService } from '../_services/storage.service';
import { JoinModalComponent } from '../modal/join-modal/join-modal.component';
import { JoinService } from '../_services/join.service';
import { JoinRequestModalComponent } from '../modal/join-request-modal/join-request-modal.component';
import { ToastService } from '../shared/toast/toast.service';
import { ConfirmDialogComponent } from '../shared/confirm-dialog/confirm-dialog.component';

@Component({
  selector: 'app-feed',
  templateUrl: './feed.component.html',
  styleUrl: './feed.component.css',
})
export class FeedComponent implements OnInit {
  content?: string
  postData: Post[] = []
  date = new Date()
  userData?: any
  joinRequest?: any
  joinRequestId: bigint[] = []
  private modalService = inject(NgbModal)

  latestPost: bigint = BigInt(0)
  lastPost: boolean = false
  postLoading: boolean = false

  constructor(
    private postService: PostService,
    private storageService: StorageService,
    private joinRequestService: JoinService,
    private toastService: ToastService,
    private cd: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.getFirst5Posts()
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

        this.createPost(newPostForm)
      }
    })
  }

  createPost(newPostForm: FormGroup) {
    this.postService.createPost(newPostForm).subscribe({
      next: () => {
        this.toastService.showStatusToast("Post created successfully")
        this.getFirst5Posts()
        this.cd.detectChanges()
      }, error: err => {
        this.toastService.showErrorToast("Error creating post: " + err.message)
        console.log(err)
      }
    })
  }

  getFirst5Posts() {
    this.postService.getFirst5Posts().subscribe({
      next: data => {
        this.postData = this.postData.concat(data.content)
        this.latestPost = BigInt(data.content.length)
        this.lastPost = data.last
        this.postLoading = false
      }, error: err => {
        this.toastService.showErrorToast("Error fetching posts: " + err.message)
        this.postLoading = false
        console.log(err)
      }
    })
  }

  getNext5Posts() {
    this.postService.getNext5Posts(this.latestPost).subscribe({
      next: data => {
        this.postData = this.postData.concat(data.content)
        this.latestPost += BigInt(data.content.length)
        this.lastPost = data.last
        this.postLoading = false
      }, error: err => {
        this.toastService.showErrorToast("Error fetching posts: " + err.message)
        this.postLoading = false
        console.log(err)
      }
    })
  }

  onScroll = () => {
    if (!this.lastPost) {
      this.postLoading = true
      this.getNext5Posts() 
    }
  }

  openCommentModal(postId: bigint) {
    const modalRef = this.modalService.open(CommentModalComponent, { size: 'xl', centered: true, scrollable: true })
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
            this.toastService.showErrorToast("Error sending join request: " + err.message)
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
        this.toastService.showErrorToast("Error fetching join requests: " + err.message)
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

  openDeleteConfirmModal(postId: bigint) {
    const modalRef = this.modalService.open(ConfirmDialogComponent, { size: 'sm' , centered: true })
    modalRef.componentInstance.modalStyle = "modal-style-danger"
    modalRef.componentInstance.modalTitle = "Delete Confirmation"
    modalRef.componentInstance.modalBody = "Are you sure you want to delete this post?"
    modalRef.componentInstance.modalButtonColor = "btn-danger"

    modalRef.result.then((confirm) => {
      if (confirm) {
        this.deletePost(postId)
      }
    })
  }

  openEditPostModal(post: Post) {
    const modalRef = this.modalService.open(PostModalComponent, { size: 'lg', centered: true, scrollable: true })
    modalRef.componentInstance.post = post
    modalRef.result.then((form) => {
      if (form) {
        const ngbDate = form.controls['meetingDate'].value
        const ngbTime = form.controls['meetingTime'].value

        this.date.setFullYear(ngbDate.year, ngbDate.month, ngbDate.day)
        this.date.setHours(ngbTime.hour, ngbTime.minute, 0)

        const editPostForm: FormGroup = new FormGroup({
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

        this.updatePost(post.id!, editPostForm)
      }
    })
  }

  updatePost(postId: bigint, editedPostForm: FormGroup) {
    this.postService.updatePost(postId, editedPostForm).subscribe({
      next: () => {
        this.toastService.showStatusToast("Post edited successfully")
        this.getFirst5Posts()
        this.cd.detectChanges()
      }, error: err => {
        this.toastService.showErrorToast("Error editing post: " + err.message)
        console.log(err)
      }
    })
  }

  deletePost(postId: bigint) {
    this.postService.deletePostById(postId).subscribe({
      next: () => {
        this.toastService.showStatusToast("Delete post successfully.")
        this.getFirst5Posts()
        this.cd.detectChanges()
      }, error: err => {
        this.toastService.showErrorToast("Error Delete Post: " + err.message)
        console.log(err)
      }
    })
  }
}
