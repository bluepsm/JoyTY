import { Component } from '@angular/core';
import { AuthService } from '../services/auth.service';
import { Country, State, City, ICountry, IState, ICity } from 'country-state-city';
import { formatDate } from '@angular/common';


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
    first_name: null,
    last_name: null,
    gender: null,
    date_of_birth: null,
    phone_number: null,
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

  constructor(private authService: AuthService) {}

  onCountryChange(): void {
    //console.log("Selected country: " + this.selectedCountry)
    this.states = State.getStatesOfCountry(JSON.parse(this.selectedCountry).isoCode)
    this.selectedState = null
    this.form.country = JSON.parse(this.selectedCountry).name
    //console.log("State from selected country: " + this.states)
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
      first_name,
      last_name,
      gender,
      date_of_birth,
      phone_number,
      country,
      state,
      city
    } = this.form

    console.log
    (
      "Username: " + username + "\n" +
      "password: " + password + "\n" +
      "email: " + email + "\n" +
      "first_name: " + first_name + "\n" +
      "last_name: " + last_name + "\n" +
      "gender: " + gender + "\n" +
      "date_of_birth: " + formatDate(date_of_birth, 'dd-MM-yyyy', 'en-US') + "\n" +
      "phone_number: " + phone_number + "\n" +
      "country: " + country + "\n" +
      "state: " + state + "\n" +
      "city: " + city
    )

    this.authService.register
    (
      username, 
      password,
      email,
      first_name,
      last_name,
      gender,
      date_of_birth,
      phone_number,
      country,
      state,
      city
    ).subscribe({
      next: data => {
        console.log(data)
        this.success = true
        this.fail = false
      }, error: err => {
        this.errorMsg = err.error.message
        this.fail = true
      }
    })
  }
}
