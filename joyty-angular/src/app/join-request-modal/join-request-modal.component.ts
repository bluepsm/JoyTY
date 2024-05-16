import { ChangeDetectorRef, Component, Input, OnInit, inject } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JoinService } from '../services/join.service';
import { JoinRequest } from '../models/joinRequest.model';
import { Post } from '../models/post.model';
import { PostService } from '../services/post.service';

@Component({
  selector: 'app-join-request-modal',
  templateUrl: './join-request-modal.component.html',
  styleUrl: './join-request-modal.component.css'
})
export class JoinRequestModalComponent implements OnInit {
  activeModal = inject(NgbActiveModal)
  @Input() public postId!: number

  post?: Post
  joinRequests?: JoinRequest[]

  constructor(
    private joinService: JoinService,
    private postService: PostService,
    private cd: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.getAllJoinRequestByPostId(this.postId)
    this.getPostByPostId(this.postId)
  }

  getAllJoinRequestByPostId(postId: number) {
    this.joinService.getAllRequestByPostId(postId).subscribe({
      next: data => {
        this.joinRequests = data
      }, error: err => {
        console.log(err)
      }
    })
  }

  respondToRequest(requestId: number, response: string) {
    this.joinService.respondToRequest(requestId, response).subscribe({
      next: data => {
        console.log(data)
        this.getPostByPostId(this.postId)
        this.getAllJoinRequestByPostId(this.postId)
        this.cd.detectChanges()
      }, error: err => {
        console.log(err)
      }
    })
  }

  getPostByPostId(postId: number) {
    this.postService.getPostByPostId(postId).subscribe({
      next: data => {
        this.post = data
        //console.log(data)
      }, error: err => {
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
