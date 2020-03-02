import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class AddProductPurchaseService {

  constructor(private http: HttpClient) { }

  saveProductPurchase(productPurchase) {
        return this.http.post('/api/addProductPurchase', productPurchase);
    }
}
