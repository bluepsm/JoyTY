import { Component, Injectable, Input, OnInit, inject } from '@angular/core';
import { NgbDateStruct, NgbModal, NgbModalConfig, NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { NgbCalendar } from '@ng-bootstrap/ng-bootstrap';
import { AbstractControl, FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { TagModalComponent } from '../tag-modal/tag-modal.component';
import { Tag } from '../../models/tag.model';
import { PlaceSearchResult } from '../../shared/place-auto-complete/place-auto-complete.component';
import { Post } from '../../models/post.model';

@Component({
  selector: 'app-post-modal',
  templateUrl: './post-modal.component.html',
  styleUrl: './post-modal.component.css',
})

@Injectable()
export class PostModalComponent implements OnInit {
  @Input() public post?: Post
  
  private modalService = inject(NgbModal)
  activeModal = inject(NgbActiveModal)

  today = inject(NgbCalendar).getToday()
  ngbDate: NgbDateStruct = this.today
  time = { hour: 12, minute: 0 }

  postForm: FormGroup = new FormGroup({
    body: new FormControl(''),
    partySize: new FormControl(''),
    placeName: new FormControl(''),
    placeAddress: new FormControl(''),
    placeLatitude: new FormControl(''),
    placeLongtitude: new FormControl(''),
    meetingDate: new FormControl(''),
    meetingTime: new FormControl(''),
    costEstimate: new FormControl(''),
    costShare: new FormControl(false),
    tags: new FormControl(''),
  })

  selectedTags?: Tag[]

  place: PlaceSearchResult = {address: ''}

  edit: boolean = false

  constructor(
    private config: NgbModalConfig,
    private formBuilder: FormBuilder,
  ) {
    this.config.backdrop = 'static'
    this.config.keyboard = false
  }

  ngOnInit(): void {
    this.postForm = this.formBuilder.group({
      body: ['', [
        Validators.required
      ]],
      partySize: [1, [
        Validators.required
      ]],
      placeName: ['', [
        Validators.required
      ]],
      placeAddress: ['', [
        Validators.required
      ]],
      placeLatitude: ['', [
        Validators.required
      ]],
      placeLongtitude: ['', [
        Validators.required
      ]],
      meetingDate: [this.ngbDate, [
        Validators.required
      ]],
      meetingTime: [this.time, [
        Validators.required
      ]],
      costEstimate: [0, [
        Validators.required
      ]],
      costShare: [true, [
        Validators.required
      ]],
      tags: ['', [
        
      ]],
    })

    if (this.post) {   
      this.edit = true   
      this.pf['body'].setValue(this.post.body)
      this.pf['partySize'].setValue(this.post.partySize)
      this.pf['costEstimate'].setValue(this.post.costEstimate)
      this.pf['costShare'].setValue(this.post.costShare)
      this.pf['tags'].setValue(this.post.tags)

      this.place = {
        name: this.post.placeName!,
        address: this.post.placeAddress!,
        location: new google.maps.LatLng(this.post.placeLatitude!, this.post.placeLongtitude!)
      }      
      this.onPlaceChange(this.place)

      const datetime = new Date(this.post.meetingDatetime!)
      const ngbDate: NgbDateStruct = {day: datetime.getDate(), month: datetime.getMonth(), year: datetime.getFullYear()}
      const ngbTime = { hour: datetime.getHours(), minute:  datetime.getMinutes()}

      this.pf['meetingDate'].setValue(ngbDate)
      this.pf['meetingTime'].setValue(ngbTime)
    }
  }

  get pf(): { [key: string]: AbstractControl } {
    return this.postForm.controls
  }

  openTagModal() {
    const modalRef = this.modalService.open(TagModalComponent, { size: 'sm', centered: true, scrollable: true })
    modalRef.result.then((result) => {
      if (result) {
        this.selectedTags = result
        let tagsId: any[] = []
        for (let tag of result) {
          tagsId.push(tag.id)
        }
        this.pf['tags'].setValue(tagsId)
      }
    })
  }

  onPlaceChange(place: PlaceSearchResult) {
    this.pf['placeName'].setValue(place.name)
    this.pf['placeAddress'].setValue(place.address)
    this.pf['placeLatitude'].setValue(place.location?.lat())
    this.pf['placeLongtitude'].setValue(place.location?.lng())
  }
}
