import { Component, OnInit, inject } from '@angular/core';
import { StorageService } from '../_services/storage.service';
import { AuthService } from '../_services/auth.service';
import { Router } from '@angular/router';
import { NgbOffcanvas } from '@ng-bootstrap/ng-bootstrap';
import { MenuNotificationComponent } from '../notification/menu-notification/menu-notification.component';
import { ToastService } from '../shared/toast/toast.service';
import { HeaderService } from '../_services/header.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent implements OnInit {
  userId?: bigint
  username?: string
  roles?: string[]
  profileImg?: any
  isLoggedIn = false
  showModApp = false
  showAdminApp = false

  private offcanvasService = inject(NgbOffcanvas)

  constructor(
    private router: Router,
    private storageService: StorageService,
    private authService: AuthService,
    private toastService: ToastService,
    private headerService: HeaderService,
  ) {}

  ngOnInit(): void {
    this.headerService.getUserState().subscribe((userState) => {
      this.isLoggedIn = userState.isLoggedIn
      this.userId = userState.userId
      this.username = userState.username
      this.roles = userState.userRoles
      this.showAdminApp = this.roles.includes('ROLE_ADMIN')
      this.showModApp = this.roles.includes('ROLE_MODERATOR')
    })
    this.headerService.getProfileImg().subscribe((profileImg) => {
      this.profileImg = profileImg
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

  openNotification() {
    const offcanvasRef = this.offcanvasService.open(MenuNotificationComponent, { position: 'end' })
    offcanvasRef.componentInstance.userId = this.userId
  }
}
