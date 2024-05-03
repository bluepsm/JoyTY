import { Injectable } from "@angular/core";
import { HttpEvent, HttpInterceptor, HttpHandler, HttpRequest, HTTP_INTERCEPTORS, HttpErrorResponse } from "@angular/common/http";
import { Observable, catchError, switchMap, throwError } from "rxjs";
import { AuthService } from "../services/auth.service";
import { StorageService } from "../services/storage.service";
import { EventBusService } from "../shared/event-bus.service";
import { EventData } from "../shared/event.class";

@Injectable()
export class HttpRequestInterceptor implements HttpInterceptor {
    private isRefreshing = false

    constructor(
        private authService: AuthService,
        private storageService: StorageService,
        private eventBusService: EventBusService
    ) {}

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        req = req.clone({
            withCredentials: true
        });

        return next.handle(req).pipe(
            catchError((error) => {
                if (error instanceof HttpErrorResponse && !req.url.includes('auth/signin') && error.status === 401) {
                    return this.handle401Error(req, next)
                }

                return throwError(() => error)
            })
        );
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