import { Component, OnInit, inject } from '@angular/core';
import { StorageService } from '../services/storage.service';
import { AuthService } from '../services/auth.service';
import { Router } from '@angular/router';
import { EventBusService } from '../shared/event-bus.service';
import { Subscription } from 'rxjs';
import { NgbOffcanvas } from '@ng-bootstrap/ng-bootstrap';
import { MenuNotificationComponent } from '../menu-notification/menu-notification.component';

@Component({
  selector: 'app-app-header',
  templateUrl: './app-header.component.html',
  styleUrl: './app-header.component.css'
})
export class AppHeaderComponent implements OnInit {
  isLoggedIn = false
  username?: string
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
  ) {}

  ngOnInit(): void {
    //console.log("Header OnInit")

    this.isLoggedIn = this.storageService.isLoggedIn()

    if (this.isLoggedIn) {
      const user = this.storageService.getUser()
      this.roles = user.roles

      this.showAdminApp = this.roles.includes('ROLE_ADMIN')
      this.showModApp = this.roles.includes('ROLE_MODERATOR')

      this.username = user.username
      //console.log("Header username = " + this.username)
    }

    this.eventBusSub = this.eventBusService.on('logout', () => {
      this.logOut
    })
  }

  logOut(): void {
    //console.log("logOut")
    this.authService.logout().subscribe({
      next: res => {
        console.log(res)
        this.storageService.clean()
        this.router.navigate(["/home"])
        this.ngOnInit()
        //window.location.reload()
      }, error: err => {
        console.log(err)
      }
    })
  }

  openNotification() {
    const offcanvasRef = this.offcanvasService.open(MenuNotificationComponent, { position: 'end' })
  }
}
