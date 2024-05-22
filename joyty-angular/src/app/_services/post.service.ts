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
    const placeName = postForm.controls['placeName'].value
    const placeAddress = postForm.controls['placeAddress'].value
    const placeLatitude = postForm.controls['placeLatitude'].value
    const placeLongtitude = postForm.controls['placeLongtitude'].value
    const meetingDatetime = postForm.controls['meetingDatetime'].value
    const partySize = postForm.controls['partySize'].value
    const costEstimate = postForm.controls['costEstimate'].value
    const costShare = postForm.controls['costShare'].value
    const tags = postForm.controls['tags'].value

    return this.http.post(POST_API + '/create', 
      {
        body,
        placeName,
        placeAddress,
        placeLatitude,
        placeLongtitude,
        meetingDatetime,
        partySize,
        costEstimate,
        costShare,
        tags,
      }, httpOptions)
  }

  getAllPost(): Observable<Post[]> {
    return this.http.get<Post[]>(POST_API + '/all', httpOptions)
  }

  getPostByPostId(postId: bigint): Observable<Post> {
    return this.http.get<Post>(POST_API + '/' + postId, httpOptions)
  }

  deletePostById(postId: bigint): Observable<any> {
    return this.http.delete(POST_API + '/deleteByPostId/' + postId, httpOptions)
  }

  updatePost(postId: bigint, postForm: FormGroup): Observable<any> {
    const body = postForm.controls['body'].value
    const placeName = postForm.controls['placeName'].value
    const placeAddress = postForm.controls['placeAddress'].value
    const placeLatitude = postForm.controls['placeLatitude'].value
    const placeLongtitude = postForm.controls['placeLongtitude'].value
    const meetingDatetime = postForm.controls['meetingDatetime'].value
    const partySize = postForm.controls['partySize'].value
    const costEstimate = postForm.controls['costEstimate'].value
    const costShare = postForm.controls['costShare'].value
    const tags = postForm.controls['tags'].value

    return this.http.put(POST_API + '/update/' + postId, 
      {
        body,
        placeName,
        placeAddress,
        placeLatitude,
        placeLongtitude,
        meetingDatetime,
        partySize,
        costEstimate,
        costShare,
        tags,
      }, httpOptions)
  }

  getFirst5Posts(): Observable<any> {
    return this.http.get<any>(POST_API + '/getFirst5Posts', httpOptions)
  }

  getNext5Posts(lastPost: bigint): Observable<any> {
    return this.http.get<any>(POST_API + '/getNext5Posts/' + lastPost, httpOptions)
  }
}
