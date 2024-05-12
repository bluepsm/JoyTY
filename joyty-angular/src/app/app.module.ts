import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { BsDatepickerModule } from 'ngx-bootstrap/datepicker';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { httpInterceptorProvider } from './_helper/http.interceptor';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { HomeComponent } from './home/home.component';
import { ProfileComponent } from './profile/profile.component';
import { AppUserComponent } from './app-user/app-user.component';
import { AppAdminComponent } from './app-admin/app-admin.component';
import { AppModeratorComponent } from './app-moderator/app-moderator.component';
import { SharedModule } from './shared/shared.module';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { NgTemplateOutlet } from '@angular/common';
import { NgbToastModule } from '@ng-bootstrap/ng-bootstrap';
import { AppHeaderComponent } from './app-header/app-header.component';
import { CommentComponent } from './comment/comment.component';
import { JoinModalComponent } from './join-modal/join-modal.component';
import { JoinRequestModalComponent } from './join-request-modal/join-request-modal.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RegisterComponent,
    HomeComponent,
    ProfileComponent,
    AppUserComponent,
    AppAdminComponent,
    AppModeratorComponent,
    AppHeaderComponent,
    CommentComponent,
    JoinModalComponent,
    JoinRequestModalComponent,
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
    NgTemplateOutlet
  ],
  providers: [httpInterceptorProvider],
  bootstrap: [AppComponent]
})
export class AppModule { }
