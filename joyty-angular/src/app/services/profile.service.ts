import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from '../models/user.model';

const USER_API_URL = 'http://localhost:8080/api/user/'
const AUTH_API_URL = 'http://localhost:8080/api/auth/'


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

  getUserProfile(userId: bigint): Observable<User> {
    return this.http.get<User>(USER_API_URL + userId)
  }

  updateUsername(userId: bigint, username: string): Observable<any> {
    return this.http.patch<any>(AUTH_API_URL + 'updateUsername', { userId, username }, httpOptions)
  }

  updateEmail(userId: bigint, email: string): Observable<any> {
    return this.http.patch<any>(AUTH_API_URL + 'updateEmail', { userId, email }, httpOptions)
  }

  updatePassword(userId: bigint, password: string): Observable<any> {
    return this.http.patch<any>(AUTH_API_URL + 'resetPassword', { userId, password }, httpOptions)
  }

  updateName(userId: bigint, firstName: string, lastName: string): Observable<any> {
    return this.http.patch<any>(USER_API_URL + 'updateName', { userId, firstName, lastName }, httpOptions)
  }

  updateDateOfBirth(userId: bigint, dateOfBirth: any): Observable<any> {
    return this.http.patch<any>(USER_API_URL + 'updateDateOfBirth', { userId, dateOfBirth }, httpOptions)
  }

  updateGender(userId: bigint, gender: string): Observable<any> {
    return this.http.patch<any>(USER_API_URL + 'updateGender', { userId, gender }, httpOptions)
  }

  updatePhoneNumber(userId: bigint, phoneNumber: string): Observable<any> {
    return this.http.patch<any>(USER_API_URL + 'updatePhoneNumber', { userId, phoneNumber }, httpOptions)
  }

  updateLocation(userId: bigint, country: string, state: string, city: string): Observable<any> {
    return this.http.patch<any>(USER_API_URL + 'updateLocation', { userId, country, state, city }, httpOptions)
  }
}
