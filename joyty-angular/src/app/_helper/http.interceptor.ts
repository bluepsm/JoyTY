import { Injectable } from "@angular/core";
import { HttpEvent, HttpInterceptor, HttpHandler, HttpRequest, HTTP_INTERCEPTORS, HttpErrorResponse, HttpResponse } from "@angular/common/http";
import { Observable, catchError, switchMap, throwError, map, timeout, finalize, of, delay, merge, first, debounceTime, combineLatest, timer, takeUntil, share, startWith, distinctUntilChanged } from "rxjs";
import { AuthService } from "../services/auth.service";
import { StorageService } from "../services/storage.service";
import { EventBusService } from "../shared/event-bus.service";
import { EventData } from "../shared/event.class";
import { LoadingService } from "../shared/loading.service";

@Injectable()
export class HttpRequestInterceptor implements HttpInterceptor {
    private isRefreshing = false

    constructor(
        private authService: AuthService,
        private storageService: StorageService,
        private eventBusService: EventBusService,
        private loadingService: LoadingService,
    ) {}

    intercept(req$: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        req$ = req$.clone({
            withCredentials: true
        });

        const res$ = next.handle(req$)
        .pipe(
            //delay(1050),
            timeout(10000),
            catchError((error) => {
                this.loadingService.setLoading(false, req$.url)

                if (error instanceof HttpErrorResponse && !req$.url.includes('auth/signin') && error.status === 401) {
                    return this.handle401Error(req$, next)
                }

                return throwError(() => error)
            }),
            share(),
        )

        const showLoading$ = merge(
            timer(1000).pipe(
                map(() => {
                    this.loadingService.setLoading(true, req$.url)
                }),
                takeUntil(res$),
            ),
            combineLatest([res$, timer(2000)]).pipe(
                map(() => {
                    this.loadingService.setLoading(false, req$.url)
                }),
            )
        ).pipe(
            startWith(
                this.loadingService.setLoading(false, req$.url)
            ),
            distinctUntilChanged(),
        )
        
        res$.subscribe()
        showLoading$.subscribe()

        return res$
    };

    private handle401Error(request: HttpRequest<any>, next: HttpHandler) {
        if (!this.isRefreshing) {
            this.isRefreshing = true
            
            if (this.storageService.isLoggedIn()) {
                return this.authService.refreshToken().pipe(
                    switchMap(() => {
                        this.isRefreshing = false

                        return next.handle(request)
                    }),
                    catchError((error) => {
                        this.isRefreshing = false

                        if (error.status == '403') {
                            this.eventBusService.emit(new EventData('logout', null))
                        }

                        return throwError(() => error)
                    })
                )
            }
        }

        return next.handle(request)
    }
};

export const httpInterceptorProvider = [
    { provide: HTTP_INTERCEPTORS, useClass: HttpRequestInterceptor, multi: true }
];