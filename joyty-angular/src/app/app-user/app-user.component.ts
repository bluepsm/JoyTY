import { Component, OnInit } from '@angular/core';
import { UserService } from '../services/user.service';

@Component({
  selector: 'app-app-user',
  templateUrl: './app-user.component.html',
  styleUrl: './app-user.component.css'
})
export class AppUserComponent implements OnInit {
  content?: string

  constructor(private userService: UserService) {}

  ngOnInit(): void {
    this.userService.getUserContent().subscribe({
      next: data => {
        this.content = data
      }, error: err => {
        console.log(err)

        if (err.error) {
          this.content = JSON.parse(err.error).message
        } else {
          this.content = "Error with status: " + err.status
        }
      }
    })
  }
}