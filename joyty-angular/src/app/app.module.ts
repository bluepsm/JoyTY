import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { BsDatepickerModule } from 'ngx-bootstrap/datepicker';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { httpInterceptorProvider } from './_helpers/http.interceptor';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './auth/login/login.component';
import { RegisterComponent } from './auth/register/register.component';
import { HomeComponent } from './home/home.component';
import { ProfileComponent } from './profile/profile.component';
import { FeedComponent } from './feed/feed.component';
import { AdminComponent } from './admin/admin.component';
import { ModeratorComponent } from './moderator/moderator.component';
import { SharedModule } from './shared/shared.module';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { NgTemplateOutlet } from '@angular/common';
import { NgbToastModule } from '@ng-bootstrap/ng-bootstrap';
import { HeaderComponent } from './header/header.component';
import { CommentModalComponent } from './modal/comment-modal/comment-modal.component';
import { JoinModalComponent } from './modal/join-modal/join-modal.component';
import { JoinRequestModalComponent } from './modal/join-request-modal/join-request-modal.component';
import { CardNotificationComponent } from './notification/card-notification/card-notification.component';
import { MenuNotificationComponent } from './notification/menu-notification/menu-notification.component';
import { GoogleMapsModule } from '@angular/google-maps';
import { NgbDropdownModule } from '@ng-bootstrap/ng-bootstrap';
import { InfiniteScrollModule } from "ngx-infinite-scroll";

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RegisterComponent,
    HomeComponent,
    ProfileComponent,
    FeedComponent,
    AdminComponent,
    ModeratorComponent,
    HeaderComponent,
    CommentModalComponent,
    JoinModalComponent,
    JoinRequestModalComponent,
    CardNotificationComponent,
    MenuNotificationComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule,
    FormsModule,
    HttpClientModule,
    BsDatepickerModule.forRoot(),
    BrowserAnimationsModule,
    SharedModule,
    NgbModule,
    NgbToastModule,
    NgTemplateOutlet,
    GoogleMapsModule,
    NgbDropdownModule,
    InfiniteScrollModule,
  ],
  providers: [httpInterceptorProvider],
  bootstrap: [AppComponent]
})
export class AppModule { }
