import { Component, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { formatDate } from '@angular/common';
import { AbstractControl, Form, FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';

import { Country, State, City, ICountry, IState, ICity } from 'country-state-city';
import { StorageService } from '../services/storage.service';
import { ProfileService } from '../services/profile.service';
import { User } from '../models/user.model';
import { ValidationService } from '../utils/validationService';
import { ConfirmDialogComponent } from '../shared/confirm-dialog/confirm-dialog.component';
import { AuthService } from '../services/auth.service';
import { Router } from '@angular/router';
import { ToastService } from '../shared/toast/toast.service';


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
  selectedCountry?: any = null
  selectedState?: any =  null
  selectedCity?: any = null

  usernameForm: FormGroup = new FormGroup({
    username: new FormControl('')
  })

  nameForm: FormGroup = new FormGroup({
    first_name: new FormControl(''),
    last_name: new FormControl('')
  })

  dateOfBirthForm: FormGroup = new FormGroup({
    date_of_birth: new FormControl(Date)
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

  constructor(
    private authService: AuthService,
    private storageService: StorageService, 
    private profileService: ProfileService,
    private router: Router, 
    private formBuilder: FormBuilder,
    private validationService: ValidationService,
    private toastService: ToastService,
  ) {}

  ngOnInit(): void {
      this.currentUser = this.storageService.getUser()
      //console.log(this.currentUser.id)

      this.profileService.getUserProfile(this.currentUser.id)
        .subscribe({
          next: data => {
            this.userData = data

            this.usernameForm = this.formBuilder.group({
              username: [this.userData.username, 
                [
                  Validators.required,
                  Validators.minLength(3),
                  Validators.maxLength(20),
                  //this.validationService.existsByUsername(this.authService)
                  this.validationService.existsByUsername()
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
                  Validators.email,
                  this.validationService.existsByEmail()
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
              validators: [this.validationService.match('password', 'confirm_password')]
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
                this.selectedCountry = country
                //console.log(this.selectedCountry)
              }
            }
            
            if (this.selectedCountry !== null) {
              this.states = State.getStatesOfCountry(this.selectedCountry.isoCode)
              for (let state of this.states) {
                if (state.name === this.lf['state'].value) {
                  this.selectedState = state
                  //console.log(this.selectedState)
                }
              }
            }
            
            if (this.selectedState !== null) {
              this.cities = City.getCitiesOfState(this.selectedCountry.isoCode, this.selectedState.isoCode)
              for (let city of this.cities) {
                if (city.name === this.lf['city'].value) {
                  this.selectedCity = city
                  //console.log(this.selectedCity)
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

  onCountryChange($event: Event): void {
    setTimeout(() => {
      if (this.selectedCountry !== null) {
        //console.log("selected country: " + this.selectedCountry)

        this.states = State.getStatesOfCountry(this.selectedCountry.isoCode)
        this.lf['country'].setValue(this.selectedCountry.name)
      } else {
        this.lf['country'].setValue("")
        this.lf['state'].setValue("")
        this.lf['city'].setValue("")
      }
      this.selectedState = null
      this.selectedCity = null
    })    
  }

  onStateChange($event: Event): void {
    setTimeout(() => {
      if (this.selectedState !== null) {
        //console.log("selected state: " + this.selectedState)

        this.cities = City.getCitiesOfState(this.selectedCountry.isoCode, this.selectedState.isoCode)
        this.lf['state'].setValue(this.selectedState.name)
      } else {
        this.lf['state'].setValue("")
        this.lf['city'].setValue("")
      }
      this.selectedCity = null
    })
  }

  onCityChange($event: Event): void {
    setTimeout(() => {
      if (this.selectedCity !== null) {
        //console.log("selected city: " + this.selectedCity.name)

        this.lf['city'].setValue(this.selectedCity.name)
      } else {
        this.lf['city'].setValue("")
      }

      // console.log("Location Form")
      // console.log("Country: " + this.lf['country'].value)
      // console.log("State: " + this.lf['state'].value)
      // console.log("City: " + this.lf['city'].value)
    })  
  }

  usernameFormSubmit(): void {
    this.profileService.updateUsername(this.currentUser.id, this.uf['username'].value)
     .subscribe({
        next: data => {
          console.log(data)
          this.showStatusToast("Username Update Successfully.")
          //this.ngOnInit()
        }, error: err => {
          console.log(err)
          this.showErrorToast("Cannot Update Username.")
        }
      })
  }

  nameFormSubmit(): void {
    this.profileService.updateName(this.currentUser.id, this.nf['first_name'].value, this.nf['last_name'].value)
     .subscribe({
        next: data => {
          console.log(data)
          this.showStatusToast("Name Update Successfully.")
          this.ngOnInit()
        }, error: err => {
          console.log(err)
          this.showErrorToast("Cannot Update Name.")
        }
      })
  }

  dateOfBirthFormSubmit(): void {
    let date = new Date(this.df['date_of_birth'].value)
    let formattedDate = formatDate(date, 'yyyy-MM-dd', 'en-US')
    console.log(formattedDate)
    this.profileService.updateDateOfBirth(this.currentUser.id, formattedDate)
     .subscribe({
        next: data => {
          console.log(data)
          this.showStatusToast("Date Of Birth Update Successfully.")
          this.ngOnInit()
        }, error: err => {
          console.log(err)
          this.showErrorToast("Cannot Update Date Of Birth.")
        }
      })
  }

  genderFormSubmit(): void {
    this.profileService.updateGender(this.currentUser.id, this.gf['gender'].value)
     .subscribe({
        next: data => {
          console.log(data)
          this.showStatusToast("Gender Update Successfully.")
          this.ngOnInit()
        }, error: err => {
          console.log(err)
          this.showErrorToast("Cannot Update Gender.")
        }
      })
  }

  phoneNumberFormSubmit(): void {
    this.profileService.updatePhoneNumber(this.currentUser.id, this.phf['phone_number'].value)
     .subscribe({
        next: data => {
          console.log(data)
          this.showStatusToast("Phone Number Update Successfully.")
          this.ngOnInit()
        }, error: err => {
          console.log(err)
          this.showErrorToast("Cannot Update Phone Number.")
        }
      })
  }

  emailFormSubmit(): void {
    this.profileService.updateEmail(this.currentUser.id, this.ef['email'].value)
     .subscribe({
        next: data => {
          console.log(data)
          this.showStatusToast("Email Update Successfully.")
          this.ngOnInit()
        }, error: err => {
          console.log(err)
          this.showErrorToast("Cannot Update Email.")
        }
      })
  }

  passwordFormSubmit(): void {
    console.log(this.pf['password'].value)
    this.profileService.updatePassword(this.currentUser.id, this.pf['password'].value)
     .subscribe({
        next: data => {
          console.log(data)
          this.showStatusToast("Password Update Successfully.")
          //this.ngOnInit()
        }, error: err => {
          console.log(err)
          this.showErrorToast("Cannot Update Password.")
        }
      })
  }

  locationFormSubmit(): void {
    this.profileService.updateLocation(this.currentUser.id, this.lf['country'].value, this.lf['state'].value, this.lf['city'].value)
     .subscribe({
        next: data => {
          console.log(data)
          this.showStatusToast("Location Update Successfully.")
          this.ngOnInit()
        }, error: err => {
          console.log(err)
          this.showErrorToast("Cannot Update Location.")
        }
      })
  }

  showStatusToast(message: String) {
    //console.log("Toast fn")
    this.toastService.show(message, { classname: 'bg-dark text-light', delay: 5000 })
  }

  showErrorToast(message: String) {
    //console.log("Toast fn")
    this.toastService.show(message, { classname: 'bg-danger text-light', delay: 15000 })
  }

  @ViewChild('updateUsernameConfirm') private updateUsernameConfirm!: ConfirmDialogComponent
  usernameConfirmModalStyle: string = 'modal-style-warning';
  usernameConfirmModalTitle: string = 'Update Confirmation';
  usernameConfirmModalBody: string = "You'll have to login again after the username updated.";
  usernameConfirmModalButtonColor: string = 'btn-warning';

  async openUpdateUsernameModal() {
    return await this.updateUsernameConfirm.open();
  }

  onUsernameSubmit() {
    this.openUpdateUsernameModal();
  }

  getUpdateUsernameConfirmation(value: any) {
    if (value == 'OK') {
      console.log("OK From getUpdateUsernameConfirmation");
      this.usernameFormSubmit();
      this.logOut();
    } else {
      console.log("CANCEL From getUpdateUsernameConfirmation");
    }
  }

  @ViewChild('updatePasswordConfirm') private updatePasswordConfirm!: ConfirmDialogComponent
  passwordConfirmModalStyle: string = 'modal-style-warning';
  passwordConfirmModalTitle: string = 'Update Confirmation';
  passwordConfirmModalBody: string = "You'll have to login again after the password updated.";
  passwordConfirmModalButtonColor: string = 'btn-warning';

  async openUpdatePasswordModal() {
    return await this.updatePasswordConfirm.open();
  }

  onPasswordSubmit() {
    this.openUpdatePasswordModal();
  }

  getUpdatePasswordConfirmation(value: any) {
    if (value == 'OK') {
      console.log("OK From getUpdatePasswordConfirmation");
      this.passwordFormSubmit();
      this.logOut();
    } else {
      console.log("CANCEL From getUpdatePasswordConfirmation");
    }
  }

  logOut(): void {
    this.authService.logout().subscribe({
      next: res => {
        console.log(res);
        this.storageService.clean();
        //window.location.reload()
        this.router.navigate(["/login"]);
      }, error: err => {
        console.log(err);
      }
    });
  }

}
