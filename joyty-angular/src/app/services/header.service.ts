import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

export interface UserState {
  isLoggedIn: boolean
  userId?: bigint
  username: string
  userRoles: string[]
}

@Injectable({
  providedIn: 'root'
})
export class HeaderService {
  private userState = new BehaviorSubject<UserState>({
    isLoggedIn: false,
    userId: undefined,
    username: "",
    userRoles: [],
  })

  private userState$ = this.userState.asObservable()

  constructor() { }

  getUserState(): Observable<UserState> {
    return this.userState$
  }

  setUserState(latestValue: UserState) {
    return this.userState.next(latestValue)
  }

  clearUserState() {
    return this.userState.next({
      isLoggedIn: false,
      userId: undefined,
      username: "",
      userRoles: [],
    })
  }
}
