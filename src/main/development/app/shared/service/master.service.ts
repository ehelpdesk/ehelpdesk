import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({ providedIn: 'root' })
export class MasterService {

  constructor(private http: HttpClient) {
  }

  getAvailableStates() {
    return this.http.get('api/availableStates', {});
  }
}
