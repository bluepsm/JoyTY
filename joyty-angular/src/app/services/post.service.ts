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
    const place_name = postForm.controls['place_name'].value
    const place_address = postForm.controls['place_address'].value
    const place_latitude = postForm.controls['place_latitude'].value
    const place_longtitude = postForm.controls['place_longtitude'].value
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
        place_name,
        place_address,
        place_latitude,
        place_longtitude,
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
