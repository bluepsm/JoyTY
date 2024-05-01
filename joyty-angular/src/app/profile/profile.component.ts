import { Component, OnInit } from '@angular/core';
import { AbstractControl, Form, FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';

import { Country, State, City, ICountry, IState, ICity } from 'country-state-city';
import { StorageService } from '../services/storage.service';
import { ProfileService } from '../services/profile.service';
import { User } from '../models/user.model';
import Validation from '../utils/validation';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css'
})
export class ProfileComponent implements OnInit {
  currentUser: any
  userData: User = {}

  countries: ICountry[] = Country.getAllCountries()
  states: IState[] = []
  cities: ICity[] = []
  selectedCountry?: string | null = null
  selectedState?: string | null =  null
  selectedCity?: string | null = null

  usernameForm: FormGroup = new FormGroup({
    username: new FormControl('')
  })

  nameForm: FormGroup = new FormGroup({
    first_name: new FormControl(''),
    last_name: new FormControl('')
  })

  dateOfBirthForm: FormGroup = new FormGroup({
    date_of_birth: new FormControl('')
  })

  genderForm: FormGroup = new FormGroup({
    gender: new FormControl('')
  })

  emailForm: FormGroup = new FormGroup({
    email: new FormControl('')
  })

  passwordForm: FormGroup = new FormGroup({
    password: new FormControl(''),
    confirm_password: new FormControl('')
  })

  phoneNumberForm: FormGroup = new FormGroup({
    phone_number: new FormControl('')
  })

  locationForm: FormGroup = new FormGroup({
    country: new FormControl(''),
    state: new FormControl(''),
    city: new FormControl('')
  })

  constructor(private storageService: StorageService, private profileService: ProfileService, private formBuilder: FormBuilder) {}

  ngOnInit(): void {
      this.currentUser = this.storageService.getUser()
      console.log(this.currentUser.id)

      this.profileService.getUserProfile(this.currentUser.id)
        .subscribe({
          next: data => {
            this.userData = data

            this.usernameForm = this.formBuilder.group({
              username: [this.userData.username, 
                [
                  Validators.required,
                  Validators.minLength(3),
                  Validators.maxLength(20)
                ]]
            })

            this.nameForm = this.formBuilder.group({
              first_name: [this.userData.first_name, 
                [
                  Validators.required,
                  Validators.minLength(3),
                  Validators.maxLength(30)
                ]],
              last_name: [this.userData.last_name, 
                [
                  Validators.required,
                  Validators.minLength(3),
                  Validators.maxLength(30)
                ]]
            })

            this.dateOfBirthForm = this.formBuilder.group({
              date_of_birth: [this.userData.date_of_birth, Validators.required]
            })

            this.genderForm = this.formBuilder.group({
              gender: [this.userData.gender, Validators.required]
            })

            this.emailForm = this.formBuilder.group({
              email: [this.userData.email, 
                [
                  Validators.required,
                  Validators.email
                ]]
            })

            this.passwordForm = this.formBuilder.group({
              password: ["", 
                [
                  Validators.required,
                  Validators.minLength(8)
                ]],
              confirm_password: ["", 
                [
                  Validators.required
                ]]
            }, 
            {
              validators: [Validation.match('password', 'confirm_password')]
            })

            this.phoneNumberForm = this.formBuilder.group({
              phone_number: [this.userData.phone_number, 
                [
                  Validators.required,
                  Validators.pattern('[0-9]{3}-[0-9]{3}-[0-9]{4}')
                ]]
            })

            this.locationForm = this.formBuilder.group({
              country: [this.userData.country, Validators.required],
              state: [this.userData.state, Validators.required],
              city: [this.userData.city, Validators.required]
            })

            for (let country of this.countries) {
              if (country.name === this.lf['country'].value) {
                this.selectedCountry = country.isoCode
              }
            }
            
            this.states = State.getStatesOfCountry(this.selectedCountry!)
            for (let state of this.states) {
              if (state.name === this.lf['state'].value) {
                this.selectedState = state.isoCode
              }
            }
            
            if (this.selectedState !== null) {
              this.cities = City.getCitiesOfState(this.selectedCountry!, this.selectedState!)
              for (let city of this.cities) {
                if (city.name === this.lf['city'].value) {
                  this.selectedCity = city.name
                }
              }
            }

          }, error: err => {
            console.log(err)
          }
        })
  }

  get uf(): { [key: string]: AbstractControl } {
    return this.usernameForm.controls;
  }

  get nf(): { [key: string]: AbstractControl } {
    return this.nameForm.controls;
  }

  get df(): { [key: string]: AbstractControl } {
    return this.dateOfBirthForm.controls;
  }

  get gf(): { [key: string]: AbstractControl } {
    return this.genderForm.controls;
  }

  get ef(): { [key: string]: AbstractControl } {
    return this.emailForm.controls;
  }

  get pf(): { [key: string]: AbstractControl } {
    return this.passwordForm.controls;
  }

  get phf(): { [key: string]: AbstractControl } {
    return this.phoneNumberForm.controls;
  }

  get lf(): { [key: string]: AbstractControl } {
    return this.locationForm.controls;
  }

  onCountryChange(): void {
    setTimeout(() => {
      if (this.selectedCountry !== null) {
        this.states = State.getStatesOfCountry(this.selectedCountry)
      }
      this.selectedState = null
      this.selectedCity = null
    })
    
    console.log("selected country: " + this.selectedCountry)
    console.log("selected state: " + this.selectedState)
    console.log("selected city: " + this.selectedCity)
  }

  onStateChange(): void {
    setTimeout(() => {
      if (this.selectedState !== null) {
        this.cities = City.getCitiesOfState(this.selectedCountry!, this.selectedState!)
      }
      this.selectedCity = null
    })

    console.log("selected country: " + this.selectedCountry)
    console.log("selected state: " + this.selectedState)
    console.log("selected city: " + this.selectedCity)
  }

  onCityChange(): void {
    console.log("selected country: " + this.selectedCountry)
    console.log("selected state: " + this.selectedState)
    console.log("selected city: " + this.selectedCity)
  }

  usernameFormSubmit(): void {
    this.profileService.updateUsername(this.currentUser.id, this.uf['username'].value)
     .subscribe({
        next: data => {
          console.log(data)
          this.ngOnInit()
        }, error: err => {
          console.log(err)
        }
      })
  }

  nameFormSubmit(): void {
    this.profileService.updateName(this.currentUser.id, this.nf['first_name'].value, this.nf['last_name'].value)
     .subscribe({
        next: data => {
          console.log(data)
          this.ngOnInit()
        }, error: err => {
          console.log(err)
        }
      })
  }

  genderFormSubmit(): void {
    this.profileService.updateGender(this.currentUser.id, this.gf['gender'].value)
     .subscribe({
        next: data => {
          console.log(data)
          this.ngOnInit()
        }, error: err => {
          console.log(err)
        }
      })
  }

  phoneNumberFormSubmit(): void {
    this.profileService.updatePhoneNumber(this.currentUser.id, this.phf['phone_number'].value)
     .subscribe({
        next: data => {
          console.log(data)
          this.ngOnInit()
        }, error: err => {
          console.log(err)
        }
      })
  }

  emailFormSubmit(): void {
    this.profileService.updateEmail(this.currentUser.id, this.ef['email'].value)
     .subscribe({
        next: data => {
          console.log(data)
          this.ngOnInit()
        }, error: err => {
          console.log(err)
        }
      })
  }

  locationFormSubmit(): void {
    for (let country of this.countries) {
      if (country.isoCode === this.selectedCountry) {
        this.lf['country'].setValue(country.name)
      }
    }
    if (this.selectedState !== null) {
      for (let state of this.states) {
        if (state.isoCode === this.selectedState) {
          this.lf['state'].setValue(state.name)
        }
      }
    }
    if (this.selectedCity!== null) {
      this.lf['city'].setValue(this.selectedCity)
    }
    this.profileService.updateLocation(this.currentUser.id, this.lf['country'].value, this.lf['state'].value, this.lf['city'].value)
     .subscribe({
        next: data => {
          console.log(data)
          this.ngOnInit()
        }, error: err => {
          console.log(err)
        }
      })
  }

}
