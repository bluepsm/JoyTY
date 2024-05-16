import { Component, EventEmitter, Injectable, Input, OnInit, Output, TemplateRef, ViewChild, inject } from '@angular/core';
import { NgbDateStruct, NgbModal, NgbModalConfig, NgbModalRef, NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Country, State, City, ICountry, IState, ICity } from 'country-state-city';
import { NgbCalendar } from '@ng-bootstrap/ng-bootstrap';
import { AbstractControl, FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { TagModalComponent } from '../tag-modal/tag-modal.component';
import { Tag } from '../../models/tag.model';
import { MapComponent } from '../../map/map.component';
import { PlaceSearchResult } from '../place-auto-complete/place-auto-complete.component';

@Component({
  selector: 'app-post-modal',
  templateUrl: './post-modal.component.html',
  styleUrl: './post-modal.component.css',
})

@Injectable()
export class PostModalComponent implements OnInit {
  @Input() public post?: any
  
  private modalService = inject(NgbModal)
  activeModal = inject(NgbActiveModal)

  // countries: ICountry[] = Country.getAllCountries()
  // states: IState[] = []
  // cities: ICity[] = []
  // selectedCountry?: any = null
  // selectedState?: any =  null
  // selectedCity?: any = null

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

  // mapOptions: google.maps.MapOptions = {
  //   disableDefaultUI: true,
  // }

  place: PlaceSearchResult = {address: ''}

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
      console.log(this.post)
      
      this.pf['body'].setValue(this.post['body'])
      this.pf['partySize'].setValue(this.post['partySize'])
      this.pf['placeName'].setValue(this.post['placeName'])
      this.pf['placeAddress'].setValue(this.post['placeAddress'])
      this.pf['placeLatitude'].setValue(this.post['placeLatitude'])
      this.pf['placeLongtitude'].setValue(this.post['placeLongtitude'])
      this.pf['meetingDate'].setValue(this.post['meetingDate'])
      this.pf['meetingTime'].setValue(this.post['meetingTime'])
      this.pf['costEstimate'].setValue(this.post['costEstimate'])
      this.pf['costShare'].setValue(this.post['costShare'])
      this.pf['tags'].setValue(this.post['tags'])

      // for (let country of this.countries) {
      //   if (country.name === this.pf['meeting_country'].value) {
      //     this.selectedCountry = country
      //     //console.log(this.selectedCountry)
      //   }
      // }
      
      // if (this.selectedCountry !== null) {
      //   this.states = State.getStatesOfCountry(this.selectedCountry.isoCode)
      //   for (let state of this.states) {
      //     if (state.name === this.pf['meeting_state'].value) {
      //       this.selectedState = state
      //       //console.log(this.selectedState)
      //     }
      //   }
      // }
      
      // if (this.selectedState !== null) {
      //   this.cities = City.getCitiesOfState(this.selectedCountry.isoCode, this.selectedState.isoCode)
      //   for (let city of this.cities) {
      //     if (city.name === this.pf['meeting_city'].value) {
      //       this.selectedCity = city
      //       //console.log(this.selectedCity)
      //     }
      //   }
      // }

    } else {
      console.log("no post data")
    }

  }

  get pf(): { [key: string]: AbstractControl } {
    return this.postForm.controls
  }

  // onCountryChange($event: Event): void {
  //   setTimeout(() => {
  //     if (this.selectedCountry !== null) {
  //       //console.log("selected country: " + this.selectedCountry)

  //       this.states = State.getStatesOfCountry(this.selectedCountry.isoCode)
  //       this.pf['meeting_country'].setValue(this.selectedCountry.name)
  //     } else {
  //       this.pf['meeting_country'].setValue("")
  //       this.pf['meeting_state'].setValue("")
  //       this.pf['meeting_city'].setValue("")
  //     }
  //     this.selectedState = null
  //     this.selectedCity = null
  //   })    
  // }

  // onStateChange($event: Event): void {
  //   setTimeout(() => {
  //     if (this.selectedState !== null) {
  //       //console.log("selected state: " + this.selectedState)

  //       this.cities = City.getCitiesOfState(this.selectedCountry.isoCode, this.selectedState.isoCode)
  //       this.pf['meeting_state'].setValue(this.selectedState.name)
  //     } else {
  //       this.pf['meeting_state'].setValue("")
  //       this.pf['meeting_city'].setValue("")
  //     }
  //     this.selectedCity = null
  //   })
  // }

  // onCityChange($event: Event): void {
  //   //console.log(this.selectedCity.name)
  //   setTimeout(() => {
  //     if (this.selectedCity !== null) {
  //       //console.log("selected city: " + this.selectedCity.name)

  //       this.pf['meeting_city'].setValue(this.selectedCity.name)
  //     } else {
  //       this.pf['meeting_city'].setValue("")
  //     }

  //     // console.log("Location Form")
  //     // console.log("Country: " + this.lf['country'].value)
  //     // console.log("State: " + this.lf['state'].value)
  //     // console.log("City: " + this.lf['city'].value)
  //   })  
  // }

  openTagModal() {
    const modalRef = this.modalService.open(TagModalComponent, { size: 'sm', centered: true, scrollable: true })
    modalRef.result.then((result) => {
      if (result) {
        //console.log(result)
        this.selectedTags = result
        let tagsId: any[] = []
        for (let tag of result) {
          tagsId.push(tag.id)
        }
        this.pf['tags'].setValue(tagsId)
        //console.log(this.pf['tags'].value)
      }
    })
  }

  onPlaceChange(place: PlaceSearchResult) {
    this.pf['placeName'].setValue(place.name)
    this.pf['placeAddress'].setValue(place.address)
    this.pf['placeLatitude'].setValue(place.location?.lat())
    this.pf['placeLongtitude'].setValue(place.location?.lng())

    console.log("In Post Form")
    console.log("Place Name: " + this.pf['placeName'].value)
    console.log("Place Address: " + this.pf['placeAddress'].value)
    console.log("Place Latitude: " + this.pf['placeLatitude'].value)
    console.log("Place Longtitude: " + this.pf['placeLongtitude'].value)
  }

}
