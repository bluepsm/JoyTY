import { ChangeDetectorRef, Component, OnInit, inject } from '@angular/core';
import { formatDate } from '@angular/common';
import { AbstractControl, FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { Country, State, City, ICountry, IState, ICity } from 'country-state-city';
import { StorageService } from '../_services/storage.service';
import { ProfileService } from '../_services/profile.service';
import { User } from '../models/user.model';
import { ValidationService } from '../_utils/validations/validationService';
import { ConfirmDialogComponent } from '../shared/confirm-dialog/confirm-dialog.component';
import { AuthService } from '../_services/auth.service';
import { Router } from '@angular/router';
import { ToastService } from '../shared/toast/toast.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs';
import { HeaderService } from '../_services/header.service';


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
    firstName: new FormControl(''),
    lastName: new FormControl('')
  })

  dateOfBirthForm: FormGroup = new FormGroup({
    dateOfBirth: new FormControl(Date)
  })

  genderForm: FormGroup = new FormGroup({
    gender: new FormControl('')
  })

  emailForm: FormGroup = new FormGroup({
    email: new FormControl('')
  })

  passwordForm: FormGroup = new FormGroup({
    password: new FormControl(''),
    confirmPassword: new FormControl('')
  })

  phoneNumberForm: FormGroup = new FormGroup({
    phoneNumber: new FormControl('')
  })

  locationForm: FormGroup = new FormGroup({
    country: new FormControl(''),
    state: new FormControl(''),
    city: new FormControl('')
  })

  private modalService = inject(NgbModal)

  currentFile?: File
  fileInfos?: Observable<any>

  profileImage?: any

  constructor(
    private authService: AuthService,
    private storageService: StorageService, 
    private profileService: ProfileService,
    private router: Router, 
    private formBuilder: FormBuilder,
    private validationService: ValidationService,
    private toastService: ToastService,
    private cd: ChangeDetectorRef,
    private headerService: HeaderService
  ) {}

  ngOnInit(): void {
    this.currentUser = this.storageService.getUser()
    this.getUserData(this.currentUser.id)
  }

  getUserData(userId: bigint) {
    this.profileService.getUserProfile(userId).subscribe({
      next: data => {
        //console.log(data)
        this.userData = data

        this.profileImage = 'data:image/jpeg;base64,' + this.userData.profileImg.data

        this.usernameForm = this.formBuilder.group({
          username: [this.userData.username, 
            [
              Validators.required,
              Validators.minLength(3),
              Validators.maxLength(20),
              this.validationService.existsByUsername()
            ]]
        })

        this.nameForm = this.formBuilder.group({
          firstName: [this.userData.firstName, 
            [
              Validators.required,
              Validators.minLength(3),
              Validators.maxLength(30)
            ]],
          lastName: [this.userData.lastName, 
            [
              Validators.required,
              Validators.minLength(3),
              Validators.maxLength(30)
            ]]
        })

        this.dateOfBirthForm = this.formBuilder.group({
          dateOfBirth: [this.userData.dateOfBirth, Validators.required]
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
          confirmPassword: ["", 
            [
              Validators.required
            ]]
        }, 
        {
          validators: [this.validationService.match('password', 'confirmPassword')]
        })

        this.phoneNumberForm = this.formBuilder.group({
          phoneNumber: [this.userData.phoneNumber, 
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
          }
        }
        
        if (this.selectedCountry !== null) {
          this.states = State.getStatesOfCountry(this.selectedCountry.isoCode)
          for (let state of this.states) {
            if (state.name === this.lf['state'].value) {
              this.selectedState = state
            }
          }
        }
        
        if (this.selectedState !== null) {
          this.cities = City.getCitiesOfState(this.selectedCountry.isoCode, this.selectedState.isoCode)
          for (let city of this.cities) {
            if (city.name === this.lf['city'].value) {
              this.selectedCity = city
            }
          }
        }

      }, error: err => {
        this.toastService.showErrorToast("Error fetching user data: " + err.error.message)
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
        this.lf['city'].setValue(this.selectedCity.name)
      } else {
        this.lf['city'].setValue("")
      }
    })  
  }

  usernameFormSubmit(): void {
    this.profileService.updateUsername(this.currentUser.id, this.uf['username'].value)
     .subscribe({
        next: () => {
          this.toastService.showStatusToast("Username Update Successfully.")
        }, error: err => {
          this.toastService.showErrorToast("Cannot Update Username.")
          console.log(err)
        }
      })
  }

  nameFormSubmit(): void {
    this.profileService.updateName(this.currentUser.id, this.nf['firstName'].value, this.nf['lastName'].value)
     .subscribe({
        next: () => {
          this.toastService.showStatusToast("Name Update Successfully.")
          this.getUserData(this.currentUser.id)
          this.cd.detectChanges()
        }, error: err => {
          this.toastService.showErrorToast("Cannot Update Name.")
          console.log(err)
        }
      })
  }

  dateOfBirthFormSubmit(): void {
    let date = new Date(this.df['dateOfBirth'].value)
    let formattedDate = formatDate(date, 'yyyy-MM-dd', 'en-US')
    console.log(formattedDate)
    this.profileService.updateDateOfBirth(this.currentUser.id, formattedDate)
     .subscribe({
        next: () => {
          this.toastService.showStatusToast("Date Of Birth Update Successfully.")
          this.getUserData(this.currentUser.id)
          this.cd.detectChanges()
        }, error: err => {
          this.toastService.showErrorToast("Cannot Update Date Of Birth.")
          console.log(err)
        }
      })
  }

  genderFormSubmit(): void {
    this.profileService.updateGender(this.currentUser.id, this.gf['gender'].value)
     .subscribe({
        next: () => {
          this.toastService.showStatusToast("Gender Update Successfully.")
          this.getUserData(this.currentUser.id)
          this.cd.detectChanges()
        }, error: err => {
          this.toastService.showErrorToast("Cannot Update Gender.")
          console.log(err)
        }
      })
  }

  phoneNumberFormSubmit(): void {
    this.profileService.updatePhoneNumber(this.currentUser.id, this.phf['phoneNumber'].value)
     .subscribe({
        next: () => {
          this.toastService.showStatusToast("Phone Number Update Successfully.")
          this.getUserData(this.currentUser.id)
          this.cd.detectChanges()
        }, error: err => {
          this.toastService.showErrorToast("Cannot Update Phone Number.")
          console.log(err)
        }
      })
  }

  emailFormSubmit(): void {
    this.profileService.updateEmail(this.currentUser.id, this.ef['email'].value)
     .subscribe({
        next: () => {
          this.toastService.showStatusToast("Email Update Successfully.")
          this.getUserData(this.currentUser.id)
          this.cd.detectChanges()
        }, error: err => {
          this.toastService.showErrorToast("Cannot Update Email.")
          console.log(err)
        }
      })
  }

  passwordFormSubmit(): void {
    console.log(this.pf['password'].value)
    this.profileService.updatePassword(this.currentUser.id, this.pf['password'].value)
     .subscribe({
        next: () => {
          this.toastService.showStatusToast("Password Update Successfully.")
        }, error: err => {
          this.toastService.showErrorToast("Cannot Update Password.")
          console.log(err)
        }
      })
  }

  locationFormSubmit(): void {
    this.profileService.updateLocation(this.currentUser.id, this.lf['country'].value, this.lf['state'].value, this.lf['city'].value)
     .subscribe({
        next: () => {
          this.toastService.showStatusToast("Location Update Successfully.")
          this.getUserData(this.currentUser.id)
          this.cd.detectChanges()
        }, error: err => {
          this.toastService.showErrorToast("Cannot Update Location.")
          console.log(err)
        }
      })
  }

  openUsernameConfirmModal() {
    const modalRef = this.modalService.open(ConfirmDialogComponent, { size: 'sm', centered: true})
    modalRef.componentInstance.modalStyle = "modal-style-warning"
    modalRef.componentInstance.modalTitle = "Update Confirmation"
    modalRef.componentInstance.modalBody = "You'll have to login again after the username updated."
    modalRef.componentInstance.modalButtonColor = "btn-warning"

    modalRef.result.then((confirm) => {
      if (confirm) {
        this.usernameFormSubmit()
        this.logOut()
      }
    })
  }

  openPasswordConfirmModal() {
    const modalRef = this.modalService.open(ConfirmDialogComponent, { size: 'sm', centered: true})
    modalRef.componentInstance.modalStyle = "modal-style-warning"
    modalRef.componentInstance.modalTitle = "Update Confirmation"
    modalRef.componentInstance.modalBody = "You'll have to login again after the password updated."
    modalRef.componentInstance.modalButtonColor = "btn-warning"

    modalRef.result.then((confirm) => {
      if (confirm) {
        this.passwordFormSubmit()
        this.logOut()
      }
    })
  }

  logOut(): void {
    this.authService.logout().subscribe({
      next: () => {
        this.toastService.showStatusToast("Logout Successfully.")
        this.storageService.clean();
        //window.location.reload()
        this.router.navigate(["/login"]);
      }, error: err => {
        this.toastService.showErrorToast("Logout Fail: " + err.error.message)
        console.log(err);
      }
    });
  }

  selectFile(event: any): void {
    this.currentFile = event.target.files.item(0)
  }

  updateProfileImg(): void {
    if (this.currentFile) {
      this.profileService.updateProfileImg(this.currentUser.id ,this.currentFile).subscribe({
        next: data => {
          this.toastService.showStatusToast("Update profile image successfully.")
          this.getUserData(this.currentUser.id)
          this.headerService.setProfileImg(data)
          this.storageService.saveProfileImg(data)
          this.cd.detectChanges()
          //console.log(data)
        }, error: err => {
          this.toastService.showErrorToast("Error update profile image: " + err.message)
          console.log(err)
        }, complete: () => {
          this.currentFile = undefined
        }
      })
    }
  }
}
