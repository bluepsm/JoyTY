import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { EventBusService } from './shared/event-bus.service';
import { Subscription, delay } from 'rxjs';
import { LoadingService } from './shared/loading.service';
import { StorageService } from './services/storage.service';
import { HeaderService } from './services/header.service';
import { AuthService } from './services/auth.service';
import { ToastService } from './shared/toast/toast.service';
import { UserService } from './services/user.service';
import { ProfileService } from './services/profile.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit {
  loading: boolean = false
  eventBusSub?: Subscription

  constructor(
    private router: Router,  
    private eventBusService: EventBusService,
    private loadingService: LoadingService,
    private storageService: StorageService,
    private headerService: HeaderService,
    private authService: AuthService,
    private toastService: ToastService,
    private profileService: ProfileService,
  ) {}

  ngOnInit(): void {
    this.listenToLoading()
    if (this.storageService.isLoggedIn()) {
      let userState = {
        isLoggedIn: true,
        userId:  this.storageService.getUser().id,
        username: this.storageService.getUser().username,
        userRoles: this.storageService.getUser().roles,
      }
      this.headerService.setUserState(userState)
      this.router.navigate(['/user'])
    }

    this.eventBusSub = this.eventBusService.on('logout', () => {
      this.logOut()
    })
  }

  getUserProfileImg(userId: bigint): any {
    this.profileService.getProfileImgById(userId).subscribe({
      next: data => {
        console.log("Get Profile Image")
        console.log(data)
        return data
      }, error: err => {
        console.log(err)
        return null
      }
    })
  }

  listenToLoading(): void {
    this.loadingService.loadingSub
      .pipe(delay(0)) // This prevents a ExpressionChangedAfterItHasBeenCheckedError for subsequent requests
      .subscribe((loading) => {
        this.loading = loading
      })
  }

  logOut(): void {
    this.authService.logout().subscribe({
      next: () => {
        this.toastService.showStatusToast("Logout successfully")
        this.headerService.clearUserState()
        this.storageService.clean()
        this.router.navigate(["/home"])
        //this.ngOnInit()
        //window.location.reload()
      }, error: err => {
        this.toastService.showErrorToast("Logout fail: " + err.error.message)
        console.log(err)
      }
    })
  }

}
