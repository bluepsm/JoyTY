import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../_services/auth.service';
import { Country, State, City, ICountry, IState, ICity } from 'country-state-city';
import { Router } from '@angular/router';
import { ToastService } from '../../shared/toast/toast.service';
import { AbstractControl, FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { ValidationService } from '../../_utils/validations/validationService';


@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent implements OnInit {
  submitted = false;

  countries: ICountry[] = Country.getAllCountries()
  states: IState[] = []
  cities: ICity[] = []
  selectedCountry?: any
  selectedState?: any
  selectedCity?: any

  registerForm: FormGroup = new FormGroup({
    username: new FormControl(''),
    firstName: new FormControl(''),
    lastName: new FormControl(''),
    dateOfBirth: new FormControl(Date),
    gender: new FormControl(''),
    email: new FormControl(''),
    password: new FormControl(''),
    confirmPassword: new FormControl(''),
    phoneNumber: new FormControl(''),
    country: new FormControl(''),
    state: new FormControl(''),
    city: new FormControl(''),
    acceptTerms: new FormControl(false),
  })

  constructor(
    private authService: AuthService, 
    private router: Router,
    private toastService: ToastService,
    private formBuilder: FormBuilder,
    private validationService: ValidationService,
  ) {}

  ngOnInit(): void {
    //Called after the constructor, initializing input properties, and the first call to ngOnChanges.
    //Add 'implements OnInit' to the class.
    
    this.registerForm = this.formBuilder.group(
      {
        username: ['', 
          [
            Validators.required,
            Validators.minLength(3),
            Validators.maxLength(20),
            this.validationService.existsByUsername()
          ]
        ],
        firstName: ['', 
          [
            Validators.required,
            Validators.minLength(3),
            Validators.maxLength(30)
          ]
        ],
        lastName: ['', 
          [
            Validators.required,
            Validators.minLength(3),
            Validators.maxLength(30)
          ]
        ],
        dateOfBirth: ['', Validators.required],
        gender: ['', Validators.required],
        email: ['', 
          [
            Validators.required,
            Validators.email,
            this.validationService.existsByEmail()
          ]
        ],
        password: ['', 
          [
            Validators.required,
            Validators.minLength(8)
          ]
        ],
        confirmPassword: ['', Validators.required],
        phoneNumber: ['', 
          [
            Validators.required,
            Validators.pattern('[0-9]{3}-[0-9]{3}-[0-9]{4}')
          ]
        ],
        country: ['', Validators.required],
        state: ['', Validators.required],
        city: ['', Validators.required],
        acceptTerms: [false, Validators.requiredTrue]
      }, 
      {
        validators: [this.validationService.match('password', 'confirmPassword')],
      }
    )
  }

  get rf(): { [key: string]: AbstractControl } {
    return this.registerForm.controls;
  }

  onCountryChange(): void {
    this.states = State.getStatesOfCountry(JSON.parse(this.selectedCountry).isoCode)
    this.selectedState = null
    this.rf['country'].setValue(JSON.parse(this.selectedCountry).name)
  }

  onStateChange(): void {
    this.cities = City.getCitiesOfState(JSON.parse(this.selectedCountry).isoCode, JSON.parse(this.selectedState).isoCode)
    this.selectedCity = null
    this.rf['state'].setValue(JSON.parse(this.selectedState).name)
  }

  onCityChange(): void {
    this.rf['city'].setValue(JSON.parse(this.selectedCity).name)
  }

  clear(type: string): void {
    switch(type) {
      case "country":
        this.selectedCountry = this.selectedState = null
        this.states = []
        break
      case "state":
        this.selectedState = this.selectedCity = null
        this.cities = []
        break
      case "city":
        this.selectedCity = null
    }
  }

  onRegisterFormSubmit(): void {
    this.submitted = true;

    if (this.registerForm.invalid) {
      return;
    }

    const 
    {
      username, 
      password,
      email,
      firstName,
      lastName,
      gender,
      dateOfBirth,
      phoneNumber,
      country,
      state,
      city
    } = this.registerForm.value

    // console.log
    // (
    //   "Username: " + username + "\n" +
    //   "password: " + password + "\n" +
    //   "email: " + email + "\n" +
    //   "firstName: " + firstName + "\n" +
    //   "lastName: " + lastName + "\n" +
    //   "gender: " + gender + "\n" +
    //   "dateOfBirth: " + formatDate(dateOfBirth, 'dd-MM-yyyy', 'en-US') + "\n" +
    //   "phoneNumber: " + phoneNumber + "\n" +
    //   "country: " + country + "\n" +
    //   "state: " + state + "\n" +
    //   "city: " + city
    // )

    this.authService.register
    (
      username, 
      password,
      email,
      firstName,
      lastName,
      gender,
      dateOfBirth,
      phoneNumber,
      country,
      state,
      city
    ).subscribe({
      next: () => {
        this.toastService.showStatusToast("Registration successfully")
        this.router.navigate(['/login'])
      }, error: err => {
        this.toastService.showErrorToast("Registration fail: " + err.message)
        console.log(err)
      }
    })
  }
}
