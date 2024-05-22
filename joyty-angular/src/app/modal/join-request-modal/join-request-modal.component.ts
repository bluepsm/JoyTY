import { ChangeDetectorRef, Component, Input, OnInit, inject } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JoinService } from '../../_services/join.service';
import { JoinRequest } from '../../models/joinRequest.model';
import { Post } from '../../models/post.model';
import { PostService } from '../../_services/post.service';
import { ToastService } from '../../shared/toast/toast.service';

@Component({
  selector: 'app-join-request-modal',
  templateUrl: './join-request-modal.component.html',
  styleUrl: './join-request-modal.component.css'
})
export class JoinRequestModalComponent implements OnInit {
  activeModal = inject(NgbActiveModal)
  @Input() public postId!: bigint

  post?: Post
  joinRequests: JoinRequest[] = []

  latestRequest: bigint = BigInt(0)
  lastRequest: boolean = false
  requestLoading: boolean = false

  constructor(
    private joinService: JoinService,
    private postService: PostService,
    private cd: ChangeDetectorRef,
    private toastService: ToastService
  ) {}

  ngOnInit(): void {
    //this.getAllJoinRequestByPostId(this.postId)
    this.getScrollRequests(this.postId, this.latestRequest)
    this.getPostByPostId(this.postId)
  }

  getScrollRequests(postId: bigint, latestRequest: bigint) {
    this.joinService.getScrollRequests(postId, latestRequest).subscribe({
      next: data => {
        this.joinRequests = this.joinRequests.concat(data.content)
        this.latestRequest += BigInt(data.content.length)
        this.lastRequest = data.last
        this.requestLoading = false
      }, error: err => {
        this.toastService.showErrorToast("Error fetching requests: " + err.message)
        this.requestLoading = false
        console.log(err)
      }
    })
  }

  onScroll = () => {
    if (!this.lastRequest) {
      this.requestLoading = true
      this.getScrollRequests(this.postId, this.latestRequest) 
    }
  }

  getAllJoinRequestByPostId(postId: bigint) {
    this.joinService.getAllRequestByPostId(postId).subscribe({
      next: data => {
        this.joinRequests = data
      }, error: err => {
        this.toastService.showErrorToast("Error fetching join requests: " + err.error.message)
        console.log(err)
      }
    })
  }

  respondToRequest(requestId: bigint, response: string) {
    this.joinService.respondToRequest(requestId, response).subscribe({
      next: () => {
        this.toastService.showStatusToast('Request responded!')
        this.getPostByPostId(this.postId)
        this.getAllJoinRequestByPostId(this.postId)
        this.cd.detectChanges()
      }, error: err => {
        this.toastService.showErrorToast('Error responding to request: ' + err.error.message)
        console.log(err)
      }
    })
  }

  getPostByPostId(postId: bigint) {
    this.postService.getPostByPostId(postId).subscribe({
      next: data => {
        this.post = data
      }, error: err => {
        this.toastService.showErrorToast("Error fetching post: " + err.error.message)
        console.log(err)
      }
    })
  }

  checkAvailable(partySize: number | undefined, joinner: number | undefined): number | undefined {
    if (partySize !== undefined && joinner !== undefined) {
      return partySize - joinner
    } else {
      return undefined
    }
  }
}
