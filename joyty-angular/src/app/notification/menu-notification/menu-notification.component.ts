import { Component, Input, OnInit, inject } from '@angular/core';
import { Notification } from '../../models/notification.model';
import { NgbActiveOffcanvas } from '@ng-bootstrap/ng-bootstrap';
import { animateListItems } from '../../../animations/animation';
import { NotificationService } from '../../_services/notification.service';
import { ToastService } from '../../shared/toast/toast.service';

@Component({
  selector: 'app-menu-notification',
  templateUrl: './menu-notification.component.html',
  styleUrl: './menu-notification.component.css',
  animations: [animateListItems]
})
export class MenuNotificationComponent implements OnInit {
  activeOffcanvas = inject(NgbActiveOffcanvas)
  @Input() public userId?: bigint

  notifications: Notification[] = []

  private notificationService = inject(NotificationService)

  latestNotification: bigint = BigInt(0)
  lastNotification: boolean = false
  notificationLoading: boolean = false

  constructor(
    private toastService: ToastService
  ) {}

  ngOnInit(): void {
    //this.getAllNotifications()
    this.getScrollNotifications(this.userId!, this.latestNotification)
  }

  getAllNotifications() {
    this.notificationService.getAllNotifications(this.userId!).subscribe({
      next: data => {
        this.notifications = data
      }, error: err => {
        this.toastService.showErrorToast("Error fetching notifications: " + err.error.message)
        console.log(err)
      }
    })
  }

  getScrollNotifications(userId: bigint, latestNotification: bigint) {
    this.notificationService.getScrollNotifications(userId, latestNotification).subscribe({
      next: data => {
        this.notifications = this.notifications.concat(data.content)
        this.latestNotification += BigInt(data.content.length)
        this.lastNotification = data.last
        this.notificationLoading = false
      }, error: err => {
        this.toastService.showErrorToast("Error fetching notifications: " + err.message)
        this.notificationLoading = false
        console.log(err)
      }
    })
  }

  onScroll = () => {
    if (!this.lastNotification) {
      this.notificationLoading = true
      this.getScrollNotifications(this.userId!, this.latestNotification) 
    }
  }
}
