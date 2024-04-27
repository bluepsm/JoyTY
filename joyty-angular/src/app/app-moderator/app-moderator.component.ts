import { Component, OnInit } from '@angular/core';
import { UserService } from '../services/user.service';

@Component({
  selector: 'app-app-moderator',
  templateUrl: './app-moderator.component.html',
  styleUrl: './app-moderator.component.css'
})
export class AppModeratorComponent implements OnInit {
  content?: string

  constructor(private userService: UserService) {}

  ngOnInit(): void {
    this.userService.getModeratorContent().subscribe({
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
