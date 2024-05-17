import { CommonModule } from '@angular/common';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Comment } from '../models/comment.model';

const COMMENT_API = 'http://localhost:8080/api/comment'

const httpOptions = {
  headers: new HttpHeaders({
    'Content-Type': 'application/json'
  })
}

@Injectable({
  providedIn: 'root'
})
export class CommentService {

  constructor(private http: HttpClient) { }

  getAllComments(postId: bigint): Observable<Comment[]> {
    return this.http.get<Comment[]>(COMMENT_API + '/getCommentByPost/' + postId, httpOptions)
  }

  createComment(postId: bigint, body: string): Observable<any> {
    return this.http.post(COMMENT_API + '/create', {postId, body}, httpOptions)
  }
}
