import { Component, OnInit, inject } from '@angular/core';
import { NgbActiveModal, NgbModalConfig } from '@ng-bootstrap/ng-bootstrap';
import { TagService } from '../../_services/tag.service';
import { Tag } from '../../models/tag.model';
import { ToastService } from '../../shared/toast/toast.service';

@Component({
  selector: 'app-tag-modal',
  templateUrl: './tag-modal.component.html',
  styleUrl: './tag-modal.component.css'
})
export class TagModalComponent implements OnInit {
  activeModal = inject(NgbActiveModal)

  tags?: Tag[]
  selectedTags: Tag[] = []

  constructor(
    private config: NgbModalConfig,
    private tagService: TagService,
    private toastService: ToastService
  ) {
    this.config.backdrop = 'static'
    this.config.keyboard = false
  }

  ngOnInit(): void {
    this.getAllTags()
  }

  getAllTags() {
    this.tagService.getAllTags().subscribe({
      next: data => {
        this.tags = data
      }, error: err => {
        this.toastService.showErrorToast("Error fetching tags: " + err.error.message)
        console.log(err)
      }
    })
  }

  selectTag(tag: Tag) {
    if (this.selectedTags.includes(tag)) {
      this.selectedTags = this.removeTagById(this.selectedTags, tag.id)
    } else {
      this.selectedTags.push(tag)
    }
  }

  removeTagById(tags: Tag[], removeId: any): Tag[] {
    return tags.filter(tag => tag.id !== removeId)
  }
}
