import { Component } from '@angular/core';
import { StorageService } from './services/storage.service';
import { AuthService } from './services/auth.service';
import { EventBusService } from './_shared/event-bus.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  private roles: string[] = []
  isLoggedIn = false
  showModApp = false
  showAdminApp = false
  username?: string

  eventBusSub?: Subscription

  constructor(private storageService: StorageService, private authService: AuthService, private eventBusService: EventBusService) {}

  ngOnInit(): void {
    this.isLoggedIn = this.storageService.isLoggedIn()

    if (this.isLoggedIn) {
      const user = this.storageService.getUser()
      this.roles = user.roles

      this.showAdminApp = this.roles.includes('ROLE_ADMIN')
      this.showModApp = this.roles.includes('ROLE_MODERATOR')

      this.username = user.username
    }

    this.eventBusSub = this.eventBusService.on('logout', () => {
      this.logOut
    })
  }

  logOut(): void {
    this.authService.logout().subscribe({
      next: res => {
        console.log(res)
        this.storageService.clean()
        window.location.reload()
      }, error: err => {
        console.log(err)
      }
    })
  }
}
