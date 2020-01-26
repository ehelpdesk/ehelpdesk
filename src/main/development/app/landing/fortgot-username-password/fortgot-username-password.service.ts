import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class ForgotUsernamePasswordService {

  constructor(private http: HttpClient) { }

  sendUsername(mail: string) {
    return this.http.post('api/account/forgot-username', mail);
  }

  sendPassword(mail: string) {
    return this.http.post('api/account/reset-password/start', mail);
  }
}
