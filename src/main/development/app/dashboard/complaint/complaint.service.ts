import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {HttpClient, HttpResponse} from '@angular/common/http';
import {createRequestOption} from 'app/shared/util/request-util';
import {SERVER_API_URL} from 'app/app.constants';

@Injectable({
    providedIn: 'root'
})
export class ComplaintService {
    public resourceUrl = SERVER_API_URL + 'api/complaints';

    constructor(private http: HttpClient) {
    }

    create(complaint: any): Observable<any> {
        return this.http.post<any>(this.resourceUrl, complaint);
    }

    update(complaint: any): Observable<any> {
        return this.http.put<any>(this.resourceUrl, complaint);
    }

    find(login: string): Observable<any> {
        return this.http.get<any>(`${this.resourceUrl}/${login}`);
    }

    delete(login: string): Observable<any> {
        return this.http.delete(`${this.resourceUrl}/${login}`);
    }

    getComplaints() {
        return this.http.get(this.resourceUrl);
    }

    getTechnicians() {
        return this.http.get('/api/getTechnicians');
    }

    assignComplaint(complaintDetails) {
        return this.http.post('/api/assignComplaint', complaintDetails);
    }

    resolveComplaint(complaintDetails) {
        return this.http.post('/api/resolveComplaint', complaintDetails);
    }
}
