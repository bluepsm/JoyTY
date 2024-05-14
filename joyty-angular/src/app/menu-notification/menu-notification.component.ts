import { Component, OnInit, inject } from '@angular/core';
import { Notification } from '../models/notification.model';
import { NgbActiveOffcanvas } from '@ng-bootstrap/ng-bootstrap';
import { animateListItems } from '../../animations/animation';

@Component({
  selector: 'app-menu-notification',
  templateUrl: './menu-notification.component.html',
  styleUrl: './menu-notification.component.css',
  animations: [animateListItems]
})
export class MenuNotificationComponent implements OnInit {
  activeOffcanvas = inject(NgbActiveOffcanvas)

  ngOnInit(): void {
  }

  notificationsMockup: Notification[] = [
    {
      title: 'What is Lorem Ipsum?',
      created_at: new Date('2023-02-21'),
      description: 'Lorem Ipsum is simply dummy text of the printing and typesetting industry.',
    },
    {
      title: 'What is Lorem Ipsum?',
      created_at: new Date('2023-02-21'),
      description: 'Lorem Ipsum is simply dummy text of the printing and typesetting industry.',
    },
    {
      title: 'What is Lorem Ipsum?',
      created_at: new Date('2023-02-21'),
      description: 'Lorem Ipsum is simply dummy text of the printing and typesetting industry.',
    },
    {
      title: 'What is Lorem Ipsum?',
      created_at: new Date('2023-02-21'),
      description: 'Lorem Ipsum is simply dummy text of the printing and typesetting industry.',
    },
  ];
}
