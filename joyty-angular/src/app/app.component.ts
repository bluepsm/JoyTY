import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { EventBusService } from './shared/event-bus.service';
import { Subscription, delay } from 'rxjs';
import { LoadingService } from './shared/loading.service';

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
  ) {}

  ngOnInit(): void {
    this.listenToLoading()
  }

  listenToLoading(): void {
    this.loadingService.loadingSub
      .pipe(delay(0)) // This prevents a ExpressionChangedAfterItHasBeenCheckedError for subsequent requests
      .subscribe((loading) => {
        this.loading = loading
      })
  }
}
