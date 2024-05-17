import { Component } from '@angular/core';
import { AuthService } from '../services/auth.service';
import { Country, State, City, ICountry, IState, ICity } from 'country-state-city';
import { Router } from '@angular/router';
import { ToastService } from '../shared/toast/toast.service';


@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {
  form: any = {
    username: null, 
    password: null, 
    email: null,
    firstName: null,
    lastName: null,
    gender: null,
    dateOfBirth: null,
    phoneNumber: null,
    country: null,
    state: null,
    city: null
  }

  success = false
  fail = false
  errorMsg = ''

  countries: ICountry[] = Country.getAllCountries()
  states: IState[] = []
  cities: ICity[] = []
  selectedCountry?: any
  selectedState?: any
  selectedCity?: any

  constructor(
    private authService: AuthService, 
    private router: Router,
    private toastService: ToastService
  ) {}

  onCountryChange(): void {
    this.states = State.getStatesOfCountry(JSON.parse(this.selectedCountry).isoCode)
    this.selectedState = null
    this.form.country = JSON.parse(this.selectedCountry).name
  }

  onStateChange(): void {
    this.cities = City.getCitiesOfState(JSON.parse(this.selectedCountry).isoCode, JSON.parse(this.selectedState).isoCode)
    this.selectedCity = null
    this.form.state = JSON.parse(this.selectedState).name
  }

  onCityChange(): void {
    this.form.city = JSON.parse(this.selectedCity).name
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

  onSubmit(): void {
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
    } = this.form

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
        this.success = true
        this.fail = false
        this.router.navigate(['/login'])
      }, error: err => {
        this.errorMsg = err.error.message
        this.toastService.showErrorToast("Registration fail: " + this.errorMsg)
        this.fail = true
      }
    })
  }
}
