import { Component, OnInit, inject } from '@angular/core';
import { StorageService } from '../services/storage.service';
import { AuthService } from '../services/auth.service';
import { Router } from '@angular/router';
import { EventBusService } from '../shared/event-bus.service';
import { Subscription } from 'rxjs';
import { NgbOffcanvas } from '@ng-bootstrap/ng-bootstrap';
import { MenuNotificationComponent } from '../menu-notification/menu-notification.component';
import { ToastService } from '../shared/toast/toast.service';

@Component({
  selector: 'app-app-header',
  templateUrl: './app-header.component.html',
  styleUrl: './app-header.component.css'
})
export class AppHeaderComponent implements OnInit {
  isLoggedIn = false
  username?: string
  userId?: bigint
  private roles: string[] = []
  showModApp = false
  showAdminApp = false
  eventBusSub?: Subscription

  private offcanvasService = inject(NgbOffcanvas)

  constructor(
    private router: Router,
    private storageService: StorageService,
    private authService: AuthService,
    private eventBusService: EventBusService,
    private toastService: ToastService,
  ) {}

  ngOnInit(): void {
    this.isLoggedIn = this.storageService.isLoggedIn()

    if (this.isLoggedIn) {
      const user = this.storageService.getUser()
      this.roles = user.roles

      this.showAdminApp = this.roles.includes('ROLE_ADMIN')
      this.showModApp = this.roles.includes('ROLE_MODERATOR')

      this.username = user.username
      this.userId = user.id
    }

    this.eventBusSub = this.eventBusService.on('logout', () => {
      this.logOut
    })
  }

  logOut(): void {
    this.authService.logout().subscribe({
      next: () => {
        this.toastService.showStatusToast("Logout successfully")
        this.storageService.clean()
        this.router.navigate(["/home"])
        this.ngOnInit()
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
