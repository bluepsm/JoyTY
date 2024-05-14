import { Component, Input, OnInit, inject } from '@angular/core';
import { Notification } from '../models/notification.model';
import { NgbActiveOffcanvas } from '@ng-bootstrap/ng-bootstrap';
import { animateListItems } from '../../animations/animation';
import { NotificationService } from '../services/notification.service';

@Component({
  selector: 'app-menu-notification',
  templateUrl: './menu-notification.component.html',
  styleUrl: './menu-notification.component.css',
  animations: [animateListItems]
})
export class MenuNotificationComponent implements OnInit {
  activeOffcanvas = inject(NgbActiveOffcanvas)
  @Input() public userId?: bigint

  notifications?: Notification[]

  constructor(
    private notificationService: NotificationService
  ) {}

  ngOnInit(): void {
    this.getAllNotifications()
  }

  getAllNotifications() {
    this.notificationService.getAllNotifications(this.userId!).subscribe({
      next: data => {
        this.notifications = data
        //console.log(data)
      }, error: err => {
        console.log(err)
      }
    })
  }
}
