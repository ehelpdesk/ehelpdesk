import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class ChangePasswordService {

  constructor(private http: HttpClient) {
  }

  save(newPassword: string, currentPassword: string): Observable<any> {
    return this.http.post('api/account/change-password', {currentPassword, newPassword});
  }
}
