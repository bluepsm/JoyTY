import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Notification } from '../models/notification.model';

const NOTIFICATION_API = 'http://localhost:8080/api/notification'

const httpOptions = {
  headers: new HttpHeaders({
    'Content-Type': 'application/json'
  })
}

@Injectable({
  providedIn: 'root'
})
export class NotificationService {

  constructor(private http: HttpClient) { }

  getAllNotifications(userId: bigint): Observable<Notification[]> {
    return this.http.get<Notification[]>(NOTIFICATION_API + '/getNotificationByUserId/' + userId, httpOptions)
  }
}
