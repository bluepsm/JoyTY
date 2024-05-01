import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from '../models/user.model';

const API_URL = 'http://localhost:8080/api/user/'

const httpOptions = {
  headers: new HttpHeaders({
    'Content-Type': 'application/json'
  })
}

@Injectable({
  providedIn: 'root'
})
export class ProfileService {

  constructor(private http: HttpClient) {}

  getUserProfile(userId: any): Observable<User> {
    return this.http.get<User>(API_URL + userId)
  }

  updateUsername(userId: any, username: string): Observable<User> {
    return this.http.patch<User>(API_URL + userId + '/updateUsername?username=' + username, {}, httpOptions)
  }

  updateName(userId: any, firstName: string, lastName: string): Observable<User> {
    return this.http.patch<User>(API_URL + userId + '/updateName?firstName=' + firstName + '&lastName=' + lastName, {}, httpOptions)
  }

  updateGender(userId: any, gender: string): Observable<User> {
    return this.http.patch<User>(API_URL + userId + '/updateGender?gender=' + gender, {}, httpOptions)
  }

  updateEmail(userId: any, email: string): Observable<User> {
    return this.http.patch<User>(API_URL + userId + '/updateEmail?email=' + email, {}, httpOptions)
  }

  updatePhoneNumber(userId: any, phoneNumber: string): Observable<User> {
    return this.http.patch<User>(API_URL + userId + '/updatePhoneNumber?phoneNumber=' + phoneNumber, {}, httpOptions)
  }

  updateLocation(userId: any, country: string, state: string, city: string): Observable<User> {
    return this.http.patch<User>(API_URL + userId + '/updateLocation?country=' + country + '&state=' + state + '&city=' + city, {}, httpOptions)
  }
}
