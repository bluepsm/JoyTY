import { HttpClient, HttpEvent, HttpHeaders, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

const FILE_API = 'http://localhost:8080/api/file'

@Injectable({
  providedIn: 'root'
})
export class FileService {

  constructor(private http: HttpClient) { }

  upload(file: File): Observable<any> {
    const formData: FormData = new FormData()
    formData.append('file', file)

    return this.http.post(FILE_API + '/upload', formData)
  }

  getFiles(id: string): Observable<any> {
    return this.http.get(FILE_API + '/getFile/' + id)
  }
}
