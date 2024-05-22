import { Component, OnInit } from '@angular/core';
import { UserService } from '../_services/user.service';

@Component({
  selector: 'app-moderator',
  templateUrl: './moderator.component.html',
  styleUrl: './moderator.component.css'
})
export class ModeratorComponent implements OnInit {
  content?: string

  constructor(
    private userService: UserService,
  ) {}

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
