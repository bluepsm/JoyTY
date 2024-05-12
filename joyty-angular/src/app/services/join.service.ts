import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { JoinRequest } from '../models/joinRequest.model';

const JOIN_API = 'http://localhost:8080/api/request'

const httpOptions = {
  headers: new HttpHeaders({
    'Content-Type': 'application/json'
  })
}

@Injectable({
  providedIn: 'root'
})
export class JoinService {

  constructor(private http: HttpClient) { }

  createJoinRequest(postId: number, body: String): Observable<any> {
    return this.http.post(JOIN_API + '/create', {postId, body}, httpOptions)
  }

  getAllRequestByUserId(userId: number): Observable<JoinRequest[]> {
    return this.http.get<JoinRequest[]>(JOIN_API + '/getJoinRequestByUserId/' + userId, httpOptions)
  }

  getAllRequestByPostId(postId: number): Observable<JoinRequest[]> {
    return this.http.get<JoinRequest[]>(JOIN_API + '/getJoinRequestByPostId/' + postId, httpOptions)
  }

  respondToRequest(requestId: number, response: string): Observable<any> {
    return this.http.get(JOIN_API + '/respondToRequest/' + requestId + '?response=' + response, httpOptions)
  }
}
