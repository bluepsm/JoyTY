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

  deleteCommentById(commentId: bigint): Observable<any> {
    return this.http.delete(COMMENT_API + '/deleteByCommentId/' + commentId, httpOptions)
  }

  updateComment(commentId: bigint, body: string, postId: bigint): Observable<any> {
    return this.http.put(COMMENT_API + '/updateCommentById/' + commentId, {postId, body}, httpOptions)
  }

  getScrollComments(postId: bigint, latestComment: bigint): Observable<any> {
    return this.http.get<any>(COMMENT_API + '/getScrollComments/' + postId +'?latestComment=' + latestComment, httpOptions)
  }
}
