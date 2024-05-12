import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { Observable } from 'rxjs';
import { Post } from '../models/post.model';

const POST_API = 'http://localhost:8080/api/post'

const httpOptions = {
  headers: new HttpHeaders({
    'Content-Type': 'application/json'
  })
}

@Injectable({
  providedIn: 'root'
})
export class PostService {

  constructor(private http: HttpClient) { }

  createPost(postForm: FormGroup): Observable<any> {
    const body = postForm.controls['body'].value
    const meeting_location = postForm.controls['meeting_location'].value
    const meeting_city = postForm.controls['meeting_city'].value
    const meeting_state = postForm.controls['meeting_state'].value
    const meeting_country = postForm.controls['meeting_country'].value
    const meeting_datetime = postForm.controls['meeting_datetime'].value
    const party_size = postForm.controls['party_size'].value
    const cost_estimate = postForm.controls['cost_estimate'].value
    const cost_share = postForm.controls['cost_share'].value
    const tags = postForm.controls['tags'].value

    //console.log("In PostService")
    //console.log("In PostService")

    return this.http.post(POST_API + '/create', 
      {
        body,
        meeting_location,
        meeting_city,
        meeting_state,
        meeting_country,
        meeting_datetime,
        party_size,
        cost_estimate,
        cost_share,
        tags,
      }, httpOptions)
  }

  getAllPost(): Observable<Post[]> {
    return this.http.get<Post[]>(POST_API + '/all', httpOptions)
  }

  getPostByPostId(postId: number): Observable<Post> {
    return this.http.get<Post>(POST_API + '/' + postId, httpOptions)
  }
}
