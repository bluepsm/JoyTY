import { Component, Input } from '@angular/core';
import { Notification } from '../../models/notification.model';
import { EnumEntity } from '../../_utils/enums/EnumEntity';
import { EnumType } from '../../_utils/enums/EnumType';

@Component({
  selector: 'app-card-notification',
  templateUrl: './card-notification.component.html',
  styleUrl: './card-notification.component.css'
})
export class CardNotificationComponent {
  @Input() notification?: Notification
  enumEntity = EnumEntity
  enumType = EnumType
}
