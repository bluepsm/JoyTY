import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../_services/auth.service';
import { StorageService } from '../../_services/storage.service';
import { Router } from '@angular/router';
import { ToastService } from '../../shared/toast/toast.service';
import { HeaderService } from '../../_services/header.service';
import { AbstractControl, FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent implements OnInit{
  submitted = false

  loginForm: FormGroup = new FormGroup({
    username: new FormControl(''),
    password: new FormControl('')
  })

  constructor(
    private authService: AuthService, 
    private storageService: StorageService, 
    private router: Router,
    private toastService: ToastService,
    private headerService: HeaderService,
    private formBuilder: FormBuilder,
  ) {}

  ngOnInit(): void {
    if (this.storageService.isLoggedIn()) {
      this.router.navigate(['/feed'])
    }

    this.loginForm = this.formBuilder.group({
      username: ['', [Validators.required]],
      password: ['', [Validators.required]]
    })
  }

  get lf(): { [key: string]: AbstractControl } {
    return this.loginForm.controls;
  }

  onLoginFormSubmit(): void {
    this.submitted = true;

    if (this.loginForm.invalid) {
      return;
    }

    const {username, password} = this.loginForm.value

    this.authService.login(username, password).subscribe({
      next: data => {
        this.toastService.showStatusToast("Login successfully")
        this.storageService.saveUser(data)

        let userState = {
          isLoggedIn: true,
          userId: data.id,
          username: data.username,
          userRoles: data.roles,
        }
        this.headerService.setUserState(userState)
        this.headerService.setProfileImg(data.profileImg)

        this.router.navigate(['/feed'])
        //this.reloadPage()
      }, error: err => {
        this.toastService.showErrorToast("Login fail: " + err.error.message)
        console.log(err)
      }
    })
  }

  reloadPage(): void {
    window.location.reload()
  }
}
