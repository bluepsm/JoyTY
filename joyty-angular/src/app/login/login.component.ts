import { Component, OnInit } from '@angular/core';
import { AuthService } from '../services/auth.service';
import { StorageService } from '../services/storage.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent implements OnInit{
  form: any = {
    username: null,
    password: null
  }
  isLoggedIn = false
  fail = false
  errorMsg = ''
  roles: string[] = []

  constructor(private authService: AuthService, private storageService: StorageService, private router: Router) {}

  ngOnInit(): void {
      if (this.storageService.isLoggedIn()) {
        this.isLoggedIn = true
        this.roles = this.storageService.getUser().roles
      }
  }

  onSubmit(): void {
    const {username, password} = this.form

    this.authService.login(username, password).subscribe({
      next: data => {
        //console.log("login complete")
        this.storageService.saveUser(data)
        this.fail = false
        this.isLoggedIn = true
        this.roles = this.storageService.getUser().roles
        this.router.navigate(['/user'])
        this.reloadPage()
      }, error: err => {
        //console.log("login error")
        this.errorMsg = err.error.message
        this.fail = true
      }
    })
  }

  reloadPage(): void {
    window.location.reload()
  }
}
