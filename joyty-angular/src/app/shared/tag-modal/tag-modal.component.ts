import { Component, OnInit, inject } from '@angular/core';
import { NgbActiveModal, NgbModalConfig } from '@ng-bootstrap/ng-bootstrap';
import { TagService } from '../../services/tag.service';
import { Tag } from '../../models/tag.model';

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
  ) {
    this.config.backdrop = 'static'
    this.config.keyboard = false
  }

  ngOnInit(): void {
    this.getAllTags()
  }

  getAllTags() {
    console.log("getAllTags")
    this.tagService.getAllTags().subscribe({
      next: data => {
        console.log(data)
        this.tags = data
      }, error: err => {
        console.log(err)
      }
    })
  }

  selectTag(tag: Tag) {
    //console.log(tag.tagname)
    if (this.selectedTags.includes(tag)) {
      this.selectedTags = this.removeTagById(this.selectedTags, tag.id)
    } else {
      this.selectedTags.push(tag)
    }
    //console.log(this.selectedTags)
  }

  removeTagById(tags: Tag[], removeId: any): Tag[] {
    //console.log("Delete")
    return tags.filter(tag => tag.id !== removeId)
  }
}
