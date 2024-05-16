import { Injectable } from '@angular/core';
import { HttpClient,HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

const AUTH_API = 'http://localhost:8080/api/auth'

const httpOptions = {
  headers: new HttpHeaders({
    'Content-Type': 'application/json'
  })
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private http: HttpClient) {}

  login(username: string, password: string): Observable<any> {
    //console.log("auth service")
    //console.log("username: " + username)
    //console.log("password: " + password)
    return this.http.post(
      AUTH_API + '/signin', {username, password}, httpOptions
    )
  }

  register(
    username: string, 
    password: string, 
    email: string,
    firstName: string,
    lastName: string,
    gender: string,
    dateOfBirth: Date,
    phoneNumber: string,
    country: string,
    state: string,
    city: string
  ): Observable<any> {
    return this.http.post(
      AUTH_API + '/signup', 
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
      }, httpOptions
    )
  }

  logout(): Observable<any> {
    return this.http.post(AUTH_API + '/signout', {}, httpOptions)
  }

  refreshToken() {
    return this.http.post(AUTH_API + '/refreshtoken', {}, httpOptions)
  }

  existsByUsername(username: string): Observable<any> {
    return this.http.get(AUTH_API + '/existsByUsername?username=' + username, httpOptions)
  }

  existsByEmail(email: string): Observable<any> {
    return this.http.get(AUTH_API + '/existsByEmail?email=' + email, httpOptions)
  }
}
