import { Component, Input } from '@angular/core';
import { Notification } from '../models/notification.model';

@Component({
  selector: 'app-card-notification',
  templateUrl: './card-notification.component.html',
  styleUrl: './card-notification.component.css'
})
export class CardNotificationComponent {
  @Input() notification?: Notification
}
